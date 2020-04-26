package com.badlogic.gdx.assets.loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.UBJsonReader;

import rma.ox.engine.renderable.obj.SkyboxModel;

public class SkyboxLoader extends AsynchronousAssetLoader<SkyboxModel, SkyboxLoader.SkyboxParameter> {

    private static final String TAG = SkyboxLoader.class.getSimpleName();

    public SkyboxLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, SkyboxParameter parameter) {
        return null;
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, SkyboxParameter parameter) {

    }

    @Override
    public SkyboxModel loadSync(AssetManager manager, String fileName, FileHandle file, SkyboxParameter parameter) {
        UBJsonReader jsonReader = new UBJsonReader();
        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
        return new SkyboxModel(modelLoader.loadModel(Gdx.files.internal(fileName)));
    }

    static public class SkyboxParameter extends AssetLoaderParameters<SkyboxModel> {
    }
}
