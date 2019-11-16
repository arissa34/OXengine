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

    public SkeletonLoader (FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, SkeletonParameter parameter) {
        return null;
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, SkeletonParameter parameter) {
        //Gdx.app.log(TAG, "===> loadAsync : "+fileName);
    }

    @Override
    public Skeleton loadSync(AssetManager manager, String fileName, FileHandle file, SkeletonParameter parameter) {
        //Gdx.app.log(TAG, "===> loadSync : "+fileName);


        int i = fileName.lastIndexOf('.');
        String atlasFileName = fileName.substring(0,i) + ".atlas";
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(atlasFileName));
        //MyAssetManager.get().addAsset(atlasFileName, TextureAtlas.class, atlas);

        SkeletonJson jsonSkeleton = new SkeletonJson(atlas);
        SkeletonData skeletonData = jsonSkeleton.readSkeletonData(Gdx.files.internal(fileName));
        Skeleton skeleton = new Skeleton(skeletonData);

        return skeleton;
    }

    static public class SkeletonParameter extends AssetLoaderParameters<Skeleton> {

    }
}
