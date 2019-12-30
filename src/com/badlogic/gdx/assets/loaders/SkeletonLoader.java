package com.badlogic.gdx.assets.loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;

import rma.ox.engine.ressource.MyAssetManager;

public class SkeletonLoader extends AsynchronousAssetLoader<Skeleton, SkeletonLoader.SkeletonParameter> {

    private static final String TAG = SkeletonLoader.class.getSimpleName();

    private FileHandle atlasFile;
    private FileHandle jsonFile;
    private String atlasFileName;

    public SkeletonLoader (FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, SkeletonParameter parameter) {
        return null;
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, SkeletonParameter parameter) {
        int i = fileName.lastIndexOf('.');
        if(parameter==null || parameter.atlasFileName == null){
            atlasFileName = fileName.substring(0,i) + ".atlas";
        }else {
            atlasFileName = parameter.atlasFileName;
        }
        atlasFile = Gdx.files.internal(atlasFileName);
        jsonFile = Gdx.files.internal(fileName);
    }

    @Override
    public Skeleton loadSync(AssetManager manager, String fileName, FileHandle file, SkeletonParameter parameter) {
        TextureAtlas atlas;
        if(!MyAssetManager.get().contains(atlasFileName)){
            atlas = new TextureAtlas(atlasFile);
            MyAssetManager.get().addAsset(atlasFileName, TextureAtlas.class, atlas);
        }else{
            atlas = MyAssetManager.get().get(atlasFileName, TextureAtlas.class);
        }
        SkeletonData skeletonData = new SkeletonJson(atlas).readSkeletonData(jsonFile);
        return new Skeleton(skeletonData);
    }

    static public class SkeletonParameter extends AssetLoaderParameters<Skeleton> {
        protected String atlasFileName;

        public SkeletonParameter(String atlasFileName){
            this.atlasFileName = atlasFileName;
        }
    }
}
