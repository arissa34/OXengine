package rma.ox.engine.ressource;

import com.badlogic.gdx.assets.CustomManager;

public class MyAssetManager extends CustomManager {

    private static MyAssetManager instance;

    public static MyAssetManager get() {
        if (instance == null) instance = new MyAssetManager();
        return instance;
    }

    public static synchronized <T> T pull(String fileName, Class<T> type) {
        return get().get(fileName, type);
    }

    public static synchronized <T> T pull(String fileName) {
        return get().get(fileName);
    }

    /*******************************/


    public MyAssetManager(){
    }
}
