package rma.ox.engine.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.Locale;

public class SettingsHelper {

    private static SettingsHelper instance;

    public static SettingsHelper get() {
        if (instance == null) instance = new SettingsHelper();
        return instance;
    }

    /*******************************/

    private final Preferences prefs;

    public SettingsHelper() {
        prefs = Gdx.app.getPreferences("$$etting$$");
    }

    public boolean isEmpty() {
        return !prefs.contains("WIDTH");
    }

    public int getFOV() {
        return prefs.getInteger("FOV", Config.DEFAULT_FOV);
    }

    public int getWidth() {
        return prefs.getInteger("WIDTH");
    }

    public int getHeight() {
        return prefs.getInteger("HEIGHT");
    }

    public void setScreenSize(int width, int height) {
        prefs.putInteger("WIDTH", width);
        prefs.putInteger("HEIGHT", height);
        prefs.flush();
    }

    public void setGuiSize(int width, int height) {
        prefs.putInteger("GUI_WIDTH", width);
        prefs.putInteger("GUI_HEIGHT", height);
        prefs.flush();
    }

    public int getGuiWidth() {
        return prefs.getInteger("GUI_WIDTH");
    }

    public int getGuiHeight() {
        return prefs.getInteger("GUI_HEIGHT");
    }

    public int getSoundVolume() {
        return prefs.getInteger("SOUND_VOLUME");
    }

    public int getMusicVolume() {
        return prefs.getInteger("MUSIC_VOLUME");
    }

    public void setFullScreen(boolean isFullScreen) {
        prefs.putBoolean("FULL_SCREEN", isFullScreen);
        prefs.flush();
    }

    public boolean isFullScreen() {
        return prefs.getBoolean("FULL_SCREEN", false);
    }

    public boolean isSoundEnabled() {
        return prefs.getBoolean("SOUND", true);
    }

    public boolean isMusicEnabled() {
        return prefs.getBoolean("MUSIC", true);
    }

    public void setLanguage(Locale locale){
        prefs.putString("LANGUAGE", locale.getLanguage());
        prefs.flush();
    }

    public String getLanguage(){
        return prefs.getString("LANGUAGE", "default");
    }
}
