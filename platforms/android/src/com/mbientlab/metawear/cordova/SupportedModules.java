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
import com.mbientlab.metawear.module.Accelerometer;

/**
 *
 * Created by Lance Gleason of Polyglot Programming LLC. on 7/10/2016.
 * http://www.polyglotprogramminginc.com
 * https://github.com/lgleasain
 * Twitter: @lgleasain
 *
 */

public class SupportedModules {

    private MWDevice mwDevice;

    public SupportedModules(MWDevice device){
        mwDevice = device;
    }
       
    public void getSupportedModules() {
        boolean accelerometerSupported = true;
        boolean gyroscopeSupported = true;
        boolean stepCounterSupported = true;

        try {
            mwDevice.getMwBoard().getModule(Accelerometer.class);
        }catch(UnsupportedModuleException e){
            accelerometerSupported = false;
        }

        try {
            mwDevice.getMwBoard().getModule(Bmi160Gyro.class);
        }catch(UnsupportedModuleException e){
            gyroscopeSupported = false;
            stepCounterSupported = false;
        }

        JSONObject resultObject = new JSONObject();
        try {
            resultObject.put("gpio", true);
            resultObject.put("accelerometer", accelerometerSupported);
            resultObject.put("gyroscope", gyroscopeSupported);
            resultObject.put("stepCounter", stepCounterSupported);
        } catch (JSONException e){
            Log.e("Metawear Cordova Error: ", e.toString());
        }
        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK,
                                                     resultObject);
        mwDevice.getMwCallbackContexts().get(mwDevice.SUPPORTED_MODULES).sendPluginResult(pluginResult);
 
    }

}
