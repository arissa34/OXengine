package rma.ox.engine.ressource;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.utils.Json;

public abstract class AbsAssets {

    public static final Json json = new Json();

    public static boolean isLoaded (String fileName) {
        return MyAssetManager.get().isLoaded(fileName);
    }

    public static <T> void load (String fileName, Class<T> type) {
        if(!isLoaded(fileName))
            MyAssetManager.get().load(fileName, type);
    }

    public static <T> void load (String fileName, Class<T> type, AssetLoaderParameters<T> parameter ) {
        if(!isLoaded(fileName))
            MyAssetManager.get().load(fileName, type, parameter);
    }

    public static void unload (String fileName) {
        if(isLoaded(fileName))
            MyAssetManager.get().unload(fileName);
    }
}
