package rma.ox.engine.settings;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

public class Config {

    public enum Type{
        DEBUG, RELEASE;
    }

    /**
     * APP
     */
    public static Type TYPE = Type.DEBUG;
    public static String VERSION = "";
    public static int GAME_VERSIONNUMBER = 0;

    public static boolean isDebug(){
        return TYPE == Type.DEBUG;
    }

    public static boolean isDesktop(){
        return Gdx.app.getType() == Application.ApplicationType.Desktop;
    }

    public static boolean isAndroid(){
        return Gdx.app.getType() == Application.ApplicationType.Android;
    }

    public static boolean isiOS(){
        return Gdx.app.getType() == Application.ApplicationType.iOS;
    }

    /**
     * Game
     */
    public static float SPEED = 1;

    static {
        if(TYPE == Type.DEBUG){
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        }else {
            Gdx.app.setLogLevel(Application.LOG_NONE);
        }
    }

    /**
     * RESOLUTION
     */
    public static int DEFAULT_FOV = 47;
    public static int DEFAULT_WIDTH = 1400;
    public static int DEFAULT_HEIGHT = 900;

}
