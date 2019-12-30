package com.badlogic.gdx.assets;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.NoSqlDBLoader;
import com.badlogic.gdx.assets.loaders.SkeletonLoader;
import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.spine.Skeleton;

import rma.ox.data.bdd.NoSqlDB;

public class CustomManager extends AssetManager {

    public CustomManager() {
        super();
        setLoader(Skeleton.class, new SkeletonLoader(resolver));
        setLoader(NoSqlDB.class, new NoSqlDBLoader(resolver));
    }

    public <T> void addAsset (final String fileName, Class<T> type, T asset) {
        // add the asset to the filename lookup
        assetTypes.put(fileName, type);

        // add the asset to the type lookup
        ObjectMap<String, RefCountedContainer> typeToAssets = assets.get(type);
        if (typeToAssets == null) {
            typeToAssets = new ObjectMap<>();
            assets.put(type, typeToAssets);
        }
        typeToAssets.put(fileName, new RefCountedContainer(asset));
    }

    public FileHandleResolver getResolver(){
        return resolver;
    }
}
