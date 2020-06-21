package com.badlogic.gdx.assets.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

import rma.ox.engine.core.utils.MainTextureAtlas;
import rma.ox.engine.ressource.MyAssetManager;

public class MainTextureAtlasLoader extends AsynchronousAssetLoader<MainTextureAtlas, MainTextureAtlasLoader.MainTextureAtlasParameter> {

    public MainTextureAtlasLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, MainTextureAtlasLoader.MainTextureAtlasParameter parameter) {
        return null;
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, MainTextureAtlasLoader.MainTextureAtlasParameter parameter) {
        MyAssetManager.get().setCurrentLoadDescription("Build main texture atlas");
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MainTextureAtlas loadSync(AssetManager manager, String fileName, FileHandle file, MainTextureAtlasLoader.MainTextureAtlasParameter parameter) {
        MainTextureAtlas.get().buildAllTextureAtlas();
        return MainTextureAtlas.get();
    }

    static public class MainTextureAtlasParameter extends AssetLoaderParameters<MainTextureAtlas> {
    }
}
