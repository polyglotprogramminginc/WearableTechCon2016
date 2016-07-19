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
import com.mbientlab.metawear.module.Bmi160Accelerometer;
import com.mbientlab.metawear.UnsupportedModuleException;

/**
 *
 * Created by Lance Gleason of Polyglot Programming LLC. on 10/11/2015.
 * http://www.polyglotprogramminginc.com
 * https://github.com/lgleasain
 * Twitter: @lgleasain
 *
 */

public class StepCounter{

    private MWDevice mwDevice;

    public StepCounter(MWDevice device){
        mwDevice = device;
    }

    private final AsyncOperation.CompletionHandler<RouteManager> stepCountHandler =
        new AsyncOperation.CompletionHandler<RouteManager>(){
            @Override
            public void success(RouteManager result){
                Log.i("step counter", "setup callbacks");
                result.subscribe("step_counter_stream_key", new MessageHandler() {
                        @Override
                        public void process(Message msg){
                            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK,
                                                                         "TOOK_A_STEP");
                            pluginResult.setKeepCallback(true);
                            mwDevice.getMwCallbackContexts().get(mwDevice.START_STEP_COUNTER).sendPluginResult(pluginResult);
                            Log.i("Metawear Cordova step ", msg.toString());
                        }
                    });
            }
        };

    private Bmi160Accelerometer getAccelerometer(){
        Bmi160Accelerometer accelModule = null;

        try {
            accelModule= mwDevice.getMwBoard().getModule(Bmi160Accelerometer.class);
        }catch(UnsupportedModuleException e){
            Log.e("Metawear Cordova Error: ", e.toString());
        }
        return accelModule;
    }
   
    public void startStepCounter(){
        Bmi160Accelerometer accelModule = getAccelerometer();
        accelModule.enableStepDetection();
        accelModule.routeData()
            .fromStepDetection().stream("step_counter_stream_key")
            .commit().onComplete(stepCountHandler);

        accelModule.configureStepDetection().commit();
        // Switch the accelerometer to active mode
        accelModule.start();
        Log.i("Step Counter", "Started Detection");
    }

    public void stopStepCounter(){
        getAccelerometer().stop();
    }
}
