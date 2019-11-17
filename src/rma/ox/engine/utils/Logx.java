package rma.ox.engine.utils;

import com.badlogic.gdx.Gdx;

import rma.ox.engine.settings.Config;

public class Logx {

    public static void l(Class type, String msg){
        if(Config.TYPE == Config.Type.DEBUG){
            Gdx.app.log(type.getSimpleName(), msg);
        }
        //TODO REPORT TO ANALYTICS
    }

    public static void d(Class type, String msg){
        if(Config.TYPE == Config.Type.DEBUG){
            Gdx.app.debug(type.getSimpleName(), msg);
        }
        //TODO REPORT TO ANALYTICS
    }

    public static void e(Class type, String msg){
        if(Config.TYPE == Config.Type.DEBUG){
            Gdx.app.error(type.getSimpleName(), msg);
        }
        //TODO REPORT TO ANALYTICS
    }

}
