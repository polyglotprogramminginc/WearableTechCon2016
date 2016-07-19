package com.mbientlab.metawear.cordova;

import com.mbientlab.metawear.AsyncOperation;
import android.util.Log;
import org.apache.cordova.PluginResult;
import com.mbientlab.metawear.cordova.MWDevice;
import com.mbientlab.metawear.RouteManager;
import com.mbientlab.metawear.Message;
import com.mbientlab.metawear.RouteManager.MessageHandler;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import com.mbientlab.metawear.module.Gpio;
import com.mbientlab.metawear.module.Gpio.AnalogReadMode;
import com.mbientlab.metawear.module.Gpio.PullMode;
import com.mbientlab.metawear.UnsupportedModuleException;

/**
 *
 * Created by Lance Gleason of Polyglot Programming LLC. on 10/11/2015.
 * http://www.polyglotprogramminginc.com
 * https://github.com/lgleasain
 * Twitter: @lgleasain
 *
 */

public class GpioModule{

    private MWDevice mwDevice;

    public GpioModule(MWDevice device){
        mwDevice = device;
    }

    private final AsyncOperation.CompletionHandler<RouteManager> getAnalogHandler(final String callbackStream, final Byte pin, final Gpio gpio){
        return new AsyncOperation.CompletionHandler<RouteManager>(){
            @Override
            public void success(RouteManager result){
                result.subscribe(callbackStream, new MessageHandler() {
                        @Override
                        public void process(Message msg){
                            Short analogValue = msg.getData(Short.class);
                            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK,
                                                                         (int) analogValue);
                            pluginResult.setKeepCallback(true);
                            String pinString = String.valueOf(pin);
                            mwDevice.getMwCallbackContexts().get(mwDevice.GPIO_READ_ANALOG + pinString).sendPluginResult(pluginResult);
                            Log.i("Metawear Cordova analogValue", String.valueOf(analogValue));
                        }
                    });
                gpio.readAnalogIn(pin, AnalogReadMode.ADC);
            }
        };
    }

    private final AsyncOperation.CompletionHandler<RouteManager> getDigitalHandler(final String callbackStream, final Byte pin, final Gpio gpio){
        return new AsyncOperation.CompletionHandler<RouteManager>(){
            @Override
            public void success(RouteManager result){
                result.subscribe(callbackStream, new MessageHandler() {
                        @Override
                        public void process(Message msg){
                            byte digitalValue = msg.getData(Byte.class);
                            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK,
                                                                         (int) digitalValue);
                            pluginResult.setKeepCallback(true);
                            mwDevice.getMwCallbackContexts().get(mwDevice.GPIO_READ_DIGITAL).sendPluginResult(pluginResult);
                            Log.i("Metawear Cordova Digital Value", String.valueOf(digitalValue));
                        }
                    });
                gpio.readDigitalIn(pin);
            }
        };
    }

    private Gpio getGpio(){
        Gpio gpioModule = null;

        try {
            gpioModule = mwDevice.getMwBoard().getModule(Gpio.class);
        }catch(UnsupportedModuleException e){
            Log.e("Metawear Cordova Error: ", e.toString());
        }
        return gpioModule;
    }

    public void readAnalogIn(int pin, PullMode pullMode, AnalogReadMode analogReadMode){
        byte pinNumber = (byte)pin;
        Gpio gpioModule = getGpio();
        gpioModule.setPinPullMode(pinNumber, pullMode);
        Log.i("Metawear Cordova readAnalogIn", String.valueOf(pin));
        gpioModule.routeData()
            .fromAnalogIn(pinNumber, analogReadMode)
            .stream("gpio_analog_stream_key_" + pin)
            .commit().onComplete(getAnalogHandler("gpio_analog_stream_key_" + pin,
                                              pinNumber, gpioModule));
    }

    public void readDigitalIn(int pin){
        byte pinNumber = (byte)pin;
        Gpio gpioModule = getGpio();
        gpioModule.routeData()
            .fromDigitalIn(pinNumber)
            .stream("gpio_digital_stream_key_" + pin)
        .commit().onComplete(getDigitalHandler("gpio_digital_stream_key_" + pin,
                                               pinNumber, gpioModule));
    }

    public void inputMonitoring(int pin){
        byte pinNumber = (byte)pin;
        Gpio gpioModule = getGpio();
        gpioModule.routeData()
            .fromDigitalIn(pinNumber)
            .stream("gpio_digital_stream_key_" + pin)
            .commit().onComplete(getDigitalHandler("gpio_digital_stream_key_" + pin,
                                                   pinNumber, gpioModule));
    }

}
