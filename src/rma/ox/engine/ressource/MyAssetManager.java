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

    private String currentLoadDescription;

    public MyAssetManager(){
        currentLoadDescription = new String();
    }

    public String getCurrentLoadDescription() {
        return currentLoadDescription;
    }

    public void setCurrentLoadDescription(String currentLoadDescription) {
        this.currentLoadDescription = currentLoadDescription;
    }
}
