package com.mbientlab.metawear.cordova;

import com.mbientlab.metawear.AsyncOperation;
import android.util.Log;
import org.apache.cordova.PluginResult;
import com.mbientlab.metawear.cordova.MWDevice;

/**
 *
 * Created by Lance Gleason of Polyglot Programming LLC. on 07/12/2016.
 * http://www.polyglotprogramminginc.com
 * https://github.com/lgleasain
 * Twitter: @lgleasain
 *
 */

public class BatteryLevel {

    private MWDevice mwDevice;

    public BatteryLevel(MWDevice device){
        mwDevice = device;
    }

    private final AsyncOperation.CompletionHandler<Byte> readBatteryLevelHandler =
        new AsyncOperation.CompletionHandler<Byte>() {
            @Override
            public void success(final Byte result){
                Log.i("Metawear Cordova BatteryLevel: ", result.toString());
                PluginResult pluginResult = new PluginResult(PluginResult.Status.OK,
                                                             result.intValue());
                mwDevice.getMwCallbackContexts().get(mwDevice.READ_BATTERY_LEVEL).sendPluginResult(pluginResult);
            }

            @Override
            public void failure(Throwable error){
                Log.e("Metawear Cordova Error: ", error.toString());
                PluginResult pluginResult = new PluginResult(PluginResult.Status.ERROR,
                                                             "ERROR");
                mwDevice.getMwCallbackContexts().get(mwDevice.READ_BATTERY_LEVEL).sendPluginResult(pluginResult);
            }
        };
        
    public void readBatteryLevel() {
        AsyncOperation<Byte> result = mwDevice.getMwBoard().readBatteryLevel();
        result.onComplete(readBatteryLevelHandler);
    }

}
