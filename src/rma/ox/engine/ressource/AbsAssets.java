package rma.ox.engine.ressource;

import com.badlogic.gdx.assets.AssetLoaderParameters;

public abstract class AbsAssets {

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
}
