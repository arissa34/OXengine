package rma.ox.engine.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.Telegram;

import rma.ox.engine.core.observer.Observable;
import rma.ox.engine.core.observer.ObservableState;
import rma.ox.engine.settings.Config;

public class Logx {

    private static StringBuffer allLog = new StringBuffer();
    public enum LogState implements ObservableState{
        ERROR, DEBUG, LOG;

        @Override
        public void enter(Object entity) {

        }
        public void enter(Object entity, Object object) {

        }

        @Override
        public void update(Object entity) {

        }

        @Override
        public void exit(Object entity) {

        }

        @Override
        public boolean onMessage(Object entity, Telegram telegram) {
            return false;
        }
    }
    public static Observable observable = new Observable();
    public static int logCount, errorCount, debugCount;

    public static void l(String msg){
        l(null, msg);
    }
    public static void l(Class type, String msg){
        if(Config.TYPE == Config.Type.DEBUG){
            if(type == null) type = Logx.class;
            Gdx.app.log(type.getSimpleName(), msg);
            allLog.append("[WHITE][LOG] [").append(type.getSimpleName()).append("][WHITE] ").append(msg).append("\n");
            logCount++;
            observable.notifyObsv(LogState.LOG, allLog);
        }
        //TODO REPORT TO ANALYTICS
    }

    public static void d(String msg){
        d(null, msg);
    }
    public static void d(Class type, String msg){
        if(Config.TYPE == Config.Type.DEBUG){
            if(type == null) type = Logx.class;
            Gdx.app.debug(type.getSimpleName(), msg);
            allLog.append("[ORANGE][DEBUG] [").append(type.getSimpleName()).append("][ORANGE] ").append(msg).append("\n");
            debugCount++;
            observable.notifyObsv(LogState.DEBUG, allLog);
        }
        //TODO REPORT TO ANALYTICS
    }

    public static void e(String msg){
        e(null, msg);
    }
    public static void e(Class type, String msg){
        if(Config.TYPE == Config.Type.DEBUG){
            if(type == null) type = Logx.class;
            Gdx.app.error(type.getSimpleName(), msg);
            allLog.append("[RED][ERROR] [").append(type.getSimpleName()).append("][RED] ").append(msg).append("\n");
            errorCount++;
            observable.notifyObsv(LogState.ERROR, allLog);
        }
        //TODO REPORT TO ANALYTICS
    }

    public static void clearLogs() {
        allLog.setLength(0);
    }
}
