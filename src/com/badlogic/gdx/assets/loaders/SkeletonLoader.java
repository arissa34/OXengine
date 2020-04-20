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

import rma.ox.engine.core.utils.MainTextureAtlas;
import rma.ox.engine.ressource.MyAssetManager;
import rma.ox.engine.utils.Logx;

public class SkeletonLoader extends AsynchronousAssetLoader<Skeleton, SkeletonLoader.SkeletonParameter> {

    private static final String TAG = SkeletonLoader.class.getSimpleName();

    //private FileHandle atlasFile;
    private FileHandle jsonFile;
    //private String atlasFileName;

    public SkeletonLoader (FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, SkeletonParameter parameter) {
        Array<AssetDescriptor> dependencies = new Array();
        String atlasFileName;
        int i = fileName.lastIndexOf('.');
        if(parameter==null || parameter.atlasFileName == null){
            atlasFileName = fileName.substring(0,i) + ".atlas";
        }else {
            atlasFileName = parameter.atlasFileName;
        }
        Logx.e(getClass(), "+++ loadAsync SKELETON ATLAS "+atlasFileName);
        dependencies.add(new AssetDescriptor(atlasFileName, TextureAtlas.class, new TextureAtlasLoader2.TextureAtlasParameter(false, true)));
        return dependencies;
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, SkeletonParameter parameter) {
        Logx.e(getClass(), "+++ loadAsync fileName "+fileName);
        jsonFile = Gdx.files.internal(fileName);
    }

    @Override
    public Skeleton loadSync(AssetManager manager, String fileName, FileHandle file, SkeletonParameter parameter) {
        Logx.e(getClass(), "+++ loadSync fileName "+fileName);
        Logx.e(getClass(), "+++ loadSync fileName getMainAltas "+fileName);
        SkeletonData skeletonData = new SkeletonJson(MainTextureAtlas.get().getMainAltas()).readSkeletonData(jsonFile);
        return new Skeleton(skeletonData);
    }

    static public class SkeletonParameter extends AssetLoaderParameters<Skeleton> {
        protected String atlasFileName;

        public SkeletonParameter(String atlasFileName){
            this.atlasFileName = atlasFileName;
        }
    }
}
