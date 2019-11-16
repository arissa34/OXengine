package rma.ox.engine.ressource;

import com.badlogic.gdx.assets.CustomManager;

public class MyAssetManager extends CustomManager {

    private static MyAssetManager instance;

    public static MyAssetManager get() {
        if (instance == null) instance = new MyAssetManager();
        return instance;
    }

    /*******************************/


    public MyAssetManager(){
    }
}
