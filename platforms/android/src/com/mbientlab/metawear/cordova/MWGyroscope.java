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
import com.mbientlab.metawear.module.Bmi160Gyro;
import com.mbientlab.metawear.UnsupportedModuleException;

/**
 *
 * Created by Lance Gleason of Polyglot Programming LLC. on 10/11/2015.
 * http://www.polyglotprogramminginc.com
 * https://github.com/lgleasain
 * Twitter: @lgleasain
 *
 */

public class MWGyroscope{

    private MWDevice mwDevice;

    public MWGyroscope(MWDevice device){
        mwDevice = device;
    }

    private final AsyncOperation.CompletionHandler<RouteManager> gyroscopeHandler =
        new AsyncOperation.CompletionHandler<RouteManager>(){
            @Override
            public void success(RouteManager result){
                result.subscribe("gyro_stream_key", new MessageHandler() {
                        @Override
                        public void process(Message msg){
                            CartesianFloat axes = msg.getData(CartesianFloat.class);
                            JSONObject resultObject = new JSONObject();
                            try {
                                resultObject.put("x", axes.x());
                                resultObject.put("y", axes.y());
                                resultObject.put("z", axes.z());
                            } catch (JSONException e){
                                Log.e("Metawear Cordova Error: ", e.toString());
                            }
                            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK,
                                                                         resultObject);
                            pluginResult.setKeepCallback(true);
                            mwDevice.getMwCallbackContexts().get(mwDevice.START_GYROSCOPE).sendPluginResult(pluginResult);
                            Log.i("Metawear Cordova Axis", axes.toString());
                        }
                    });
            }
        };

    private Bmi160Gyro getGyroscope(){
        Bmi160Gyro gyroModule = null;

        try {
            gyroModule= mwDevice.getMwBoard().getModule(Bmi160Gyro.class);
        }catch(UnsupportedModuleException e){
            Log.e("Metawear Cordova Error: ", e.toString());

            JSONObject resultObject = new JSONObject();
            try {
                resultObject.put(MWDevice.STATUS, MWDevice.MODULE_NOT_SUPPORTED);
                mwDevice.getMwCallbackContexts().get(mwDevice.START_GYROSCOPE).error(resultObject);
            } catch (JSONException jsonException){
                Log.e("Metawear Cordova Error: ", jsonException.toString());
            }

        }
        return gyroModule;
    }

    public void startGyroscope(){
        Log.v("MetaWear", " start Gryoscope");
        Bmi160Gyro gyroModule = getGyroscope();
        
        if(gyroModule != null){
            gyroModule.configure()
                .setFullScaleRange(Bmi160Gyro.FullScaleRange.FSR_2000)
                .setOutputDataRate(Bmi160Gyro.OutputDataRate.ODR_100_HZ)
                .commit();

            gyroModule.routeData()
                .fromAxes().stream("gyro_stream_key")
                .commit().onComplete(gyroscopeHandler);
            gyroModule.start();
        }
    }

    public void stopGyroscope(){
        getGyroscope().stop();
    }
}
