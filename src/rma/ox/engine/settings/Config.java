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

}
