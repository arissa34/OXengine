package rma.ox.engine.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SettingsHelper {

    private static SettingsHelper instance;

    public static SettingsHelper get() {
        if (instance == null) instance = new SettingsHelper();
        return instance;
    }

    /*******************************/

    private final Preferences prefs;

    public SettingsHelper(){
        prefs = Gdx.app.getPreferences("$$etting$$");
    }

    public void defaultVal(String key, int val){
        if(!prefs.contains(key)){
            prefs.putInteger(key, val);
        }
        prefs.flush();
    }

    public int getFOV(){
        return prefs.getInteger("FOV");
    }

    public int getWidth(){
        return prefs.getInteger("WIDTH");
    }

    public int getHeight(){
        return prefs.getInteger("HEIGHT");
    }

    public float getGuiWidth(){
        return prefs.getFloat("GUI_WIDTH");
    }

    public float getGuiHeight(){
        return prefs.getFloat("GUI_HEIGHT");
    }


}
