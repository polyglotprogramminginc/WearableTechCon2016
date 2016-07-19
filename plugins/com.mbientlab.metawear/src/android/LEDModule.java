package com.mbientlab.metawear.cordova;

import com.mbientlab.metawear.AsyncOperation;
import android.util.Log;
import com.mbientlab.metawear.cordova.MWDevice;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import com.mbientlab.metawear.module.Led;
import com.mbientlab.metawear.module.Led.ColorChannelEditor;
import com.mbientlab.metawear.UnsupportedModuleException;

/**
 *
 * Created by Lance Gleason of Polyglot Programming LLC. on 7/10/2016.
 * http://www.polyglotprogramminginc.com
 * https://github.com/lgleasain
 * Twitter: @lgleasain
 *
 */

public class LEDModule {

    private MWDevice mwDevice;

    public LEDModule(MWDevice device){
        mwDevice = device;
    }
       
    private Led getLed(){
        Led ledModule = null;

        try {
            ledModule= mwDevice.getMwBoard().getModule(Led.class);
        }catch(UnsupportedModuleException e){
            Log.e("Metawear Cordova Error: ", e.toString());
        }
        return ledModule;
    }

    public void playLED(JSONArray arguments) {
        Log.i("LEDModule playing ", arguments.toString());
        JSONObject argumentObject = null;

        try {
            argumentObject = arguments.getJSONObject(0);
        } catch(JSONException e){}
        /*channel, highIntensity, lowIntensity,
          repeatCount, pulseDuration, riseTime,
          highTime, fallTime*/

        Led ledModule = getLed();
        ColorChannelEditor colorChannelEditor = null;

        try{
            if(argumentObject.getString("channel").equals("RED")){
                colorChannelEditor = ledModule.configureColorChannel(Led.ColorChannel.RED);
            }else if(argumentObject.getString("channel").equals("GREEN")){
                colorChannelEditor = ledModule.configureColorChannel(Led.ColorChannel.GREEN);
            } if(argumentObject.getString("channel").equals("BLUE")){
                colorChannelEditor = ledModule.configureColorChannel(Led.ColorChannel.BLUE);
            }
        }catch(JSONException e){}
        
        try{
            colorChannelEditor = colorChannelEditor.
                setRiseTime((short) argumentObject.getInt("riseTime"));
            Log.i("LED Play riseTime ", String.valueOf(argumentObject.getInt("riseTime")));
        }catch(JSONException e){}
        
        try{
            colorChannelEditor = colorChannelEditor.
                setPulseDuration((short) argumentObject.getInt("pulseDuration"));
            Log.i("LED Play pulseDuration ", String.valueOf(argumentObject.getInt("pulseDuration")));
        }catch(JSONException e){}

        try{
            colorChannelEditor = colorChannelEditor.
                setRepeatCount((byte) argumentObject.getInt("repeatCount"));
            Log.i("LED Play repeat count ", String.valueOf(argumentObject.getInt("repeatCount")));
        }catch(JSONException e){}
      
        try{
            colorChannelEditor = colorChannelEditor.
                setHighTime((short) argumentObject.getInt("highTime"));
            Log.i("LED Play high time ", String.valueOf(argumentObject.getInt("highTime")));
        }catch(JSONException e){}
       
        try{
            colorChannelEditor = colorChannelEditor.
                setFallTime((short) argumentObject.getInt("fallTime"));
            Log.i("LED Play fall time ", String.valueOf(argumentObject.getInt("fallTime")));
        }catch(JSONException e){}

        try{
            colorChannelEditor = colorChannelEditor.
                setLowIntensity((byte) argumentObject.getInt("lowIntensity"));
            Log.i("LED Play low Intensity ", String.valueOf(argumentObject.getInt("lowIntensity")));
        }catch(JSONException e){}

        try{
            colorChannelEditor = colorChannelEditor.
                setHighIntensity((byte) argumentObject.getInt("highIntensity"));
            Log.i("LED Play high Intensity ", String.valueOf(argumentObject.getInt("highIntensity")));
        }catch(JSONException e){}

        colorChannelEditor.commit();
        ledModule.play(true);
    }

    public void stopLED(){
        getLed().stop(true);
    }
}
