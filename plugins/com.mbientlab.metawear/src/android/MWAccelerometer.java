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
import com.mbientlab.metawear.module.Accelerometer;
import com.mbientlab.metawear.UnsupportedModuleException;

/**
 *
 * Created by Lance Gleason of Polyglot Programming LLC. on 10/11/2015.
 * http://www.polyglotprogramminginc.com
 * https://github.com/lgleasain
 * Twitter: @lgleasain
 *
 */

public class MWAccelerometer{

    private MWDevice mwDevice;

    public MWAccelerometer(MWDevice device){
        mwDevice = device;
    }

    private final AsyncOperation.CompletionHandler<RouteManager> accelerometerHandler =
        new AsyncOperation.CompletionHandler<RouteManager>(){
            @Override
            public void success(RouteManager result){
                result.subscribe("accel_stream_key", new MessageHandler() {
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
                            mwDevice.getMwCallbackContexts().get(mwDevice.START_ACCELEROMETER).sendPluginResult(pluginResult);
                            Log.i("Metawear Cordova Axis", axes.toString());
                        }
                    });
            }
        };

    private Accelerometer getAccelerometer(){
        Accelerometer accelModule = null;

        try {
            accelModule= mwDevice.getMwBoard().getModule(Accelerometer.class);
        }catch(UnsupportedModuleException e){
            Log.e("Metawear Cordova Error: ", e.toString());
        }
        return accelModule;
    }

    public void startAccelerometer(){
        Accelerometer accelModule = getAccelerometer();
        accelModule.routeData()
            .fromAxes().stream("accel_stream_key")
            .commit().onComplete(accelerometerHandler);


        // Set the sampling frequency to 50Hz, or closest valid ODR
        accelModule.setOutputDataRate(50.f);
        // Set the measurement range to +/- 4g, or closet valid range
        accelModule.setAxisSamplingRange(4.0f);

        // enable axis sampling
        accelModule.enableAxisSampling();

        // Switch the accelerometer to active mode
        accelModule.start();
    }

    public void stopAccelerometer(){
        getAccelerometer().stop();
    }
}
