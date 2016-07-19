package com.mbientlab.metawear.cordova;

import com.mbientlab.metawear.AsyncOperation;
import android.util.Log;
import org.apache.cordova.PluginResult;
import com.mbientlab.metawear.cordova.MWDevice;

/**
 *
 * Created by Lance Gleason of Polyglot Programming LLC. on 10/11/2015.
 * http://www.polyglotprogramminginc.com
 * https://github.com/lgleasain
 * Twitter: @lgleasain
 *
 */

public class RSSI {

    private MWDevice mwDevice;

    public RSSI(MWDevice device){
        mwDevice = device;
    }

    private final AsyncOperation.CompletionHandler<Integer> readRssiHandler =
        new AsyncOperation.CompletionHandler<Integer>() {
            @Override
            public void success(final Integer result){
                Log.i("Metawear Cordova RSSI: ", result.toString());
                PluginResult pluginResult = new PluginResult(PluginResult.Status.OK,
                                                             result.toString());
                mwDevice.getMwCallbackContexts().get(mwDevice.READ_RSSI).sendPluginResult(pluginResult);
            }

            @Override
            public void failure(Throwable error){
                Log.e("Metawear Cordova Error: ", error.toString());
                PluginResult pluginResult = new PluginResult(PluginResult.Status.ERROR,
                                                             "ERROR");
                mwDevice.getMwCallbackContexts().get(mwDevice.READ_RSSI).sendPluginResult(pluginResult);
            }
        };
        
    public void readRssi() {
        AsyncOperation<Integer> result = mwDevice.getMwBoard().readRssi();
        result.onComplete(readRssiHandler);
    }

}
