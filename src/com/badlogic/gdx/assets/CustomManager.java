package com.badlogic.gdx.assets;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.FontGeneratorLoader;
import com.badlogic.gdx.assets.loaders.NoSqlDBLoader;
import com.badlogic.gdx.assets.loaders.SkeletonLoader;
import com.badlogic.gdx.assets.loaders.SkyboxLoader;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader2;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.spine.Skeleton;

import rma.ox.data.bdd.NoSqlDB;
import rma.ox.engine.renderable.obj.SkyboxModel;

public class CustomManager extends AssetManager {

    public CustomManager() {
        super();
        setLoader(Skeleton.class, new SkeletonLoader(resolver));
        setLoader(NoSqlDB.class, new NoSqlDBLoader(resolver));
        setLoader(BitmapFont.class, new FontGeneratorLoader(resolver));
        setLoader(SkyboxModel.class, new SkyboxLoader(resolver));
        setLoader(TextureAtlas.class, new TextureAtlasLoader2(resolver));
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
