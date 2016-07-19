package com.mbientlab.metawear.cordova;

import com.mbientlab.metawear.AsyncOperation;
import android.util.Log;
import org.apache.cordova.PluginResult;
import com.mbientlab.metawear.cordova.MWDevice;
import com.mbientlab.metawear.RouteManager;
import com.mbientlab.metawear.Message;
import com.mbientlab.metawear.data.CartesianFloat;
import com.mbientlab.metawear.RouteManager.MessageHandler;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import com.mbientlab.metawear.module.MultiChannelTemperature;
import com.mbientlab.metawear.module.MultiChannelTemperature.*;
import com.mbientlab.metawear.UnsupportedModuleException;
import java.util.List;

/**
 *
 * Created by Lance Gleason of Polyglot Programming LLC. on 10/11/2015.
 * http://www.polyglotprogramminginc.com
 * https://github.com/lgleasain
 * Twitter: @lgleasain
 *
 */

public class MWMultiChannelTemperature{

    private MWDevice mwDevice;
    MultiChannelTemperature temperatureModule;
    List<Source> tempSources;
    byte rawSource;

    public MWMultiChannelTemperature(MWDevice device){
        mwDevice = device;
    }

    private final AsyncOperation.CompletionHandler<RouteManager> temperatureHandler =
        new AsyncOperation.CompletionHandler<RouteManager>(){
            @Override
            public void success(RouteManager result){
                Log.i("MWMultiChannelTemperature", "route success");
                result.subscribe("temperature_stream_key", new MessageHandler() {
                        @Override
                        public void process(Message msg){
                            Float temperature = msg.getData(Float.class);
                            
                            JSONObject resultObject = new JSONObject();
                            try {
                                resultObject.put("temperature", temperature);
                            } catch (JSONException e){
                                Log.e("Metawear Cordova Error: ", e.toString());
                            }
                            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK,
                                                                         resultObject);
                            mwDevice.getMwCallbackContexts().get(mwDevice.READ_TEMPERATURE).sendPluginResult(pluginResult);
                            Log.i("Metawear Cordova Temperature", temperature.toString());
                        }
                    });
                temperatureModule.readTemperature(tempSources.get(rawSource));
            }
        };

    private MultiChannelTemperature getMultiChannelTemperature(){
        MultiChannelTemperature temperatureModule = null;

        try {
            temperatureModule= mwDevice.getMwBoard().getModule(MultiChannelTemperature.class);
        }catch(UnsupportedModuleException e){
            Log.e("Metawear Cordova Error: ", e.toString());
        }
        return temperatureModule;
    }

    public void readTemperature(JSONArray arguments){
        JSONObject argumentObject = null;
        String source = null;

        try {
            argumentObject = arguments.getJSONObject(0);
            source = argumentObject.getString("sensor");
        } catch(JSONException e){}

        Log.i("MultiChannelTemperature Read Temperature", source);
        rawSource = 0;

        if(source.equals("PRO_BMP_280")){
            rawSource = MetaWearProChannel.BMP_280;
        }else if(source.equals("PRO_EXT_THERMISTOR")){
            rawSource = MetaWearProChannel.EXT_THERMISTOR;
        }else if(source.equals("PRO_NRF_DIE")){
            rawSource = MetaWearProChannel.NRF_DIE;
        }else if(source.equals("PRO_ON_BOARD_THERMISTOR")){
            rawSource = MetaWearProChannel.ON_BOARD_THERMISTOR;
        }else if(source.equals("R_EXT_THERMISTOR")){
            rawSource = MetaWearProChannel.EXT_THERMISTOR;
        }else if(source.equals("R_NRF_DIE")){
            rawSource = MetaWearProChannel.NRF_DIE;
        }


        temperatureModule = getMultiChannelTemperature();
        tempSources= temperatureModule.getSources();
        temperatureModule.routeData()
            .fromSource(tempSources.get(rawSource)).stream("temperature_stream_key")
            .commit().onComplete(temperatureHandler);
    }

}
