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

public class SkeletonLoader extends AsynchronousAssetLoader<Skeleton, SkeletonLoader.SkeletonParameter> {

    private static final String TAG = SkeletonLoader.class.getSimpleName();

    private FileHandle atlasFile;
    private FileHandle jsonFile;

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
        String atlasFileName = fileName.substring(0,i) + ".atlas";
        atlasFile = Gdx.files.internal(atlasFileName);
        jsonFile = Gdx.files.internal(fileName);
    }

    @Override
    public Skeleton loadSync(AssetManager manager, String fileName, FileHandle file, SkeletonParameter parameter) {
        TextureAtlas atlas = new TextureAtlas(atlasFile);
        SkeletonData skeletonData = new SkeletonJson(atlas).readSkeletonData(jsonFile);
        return new Skeleton(skeletonData);
    }

    static public class SkeletonParameter extends AssetLoaderParameters<Skeleton> {

    }
}
