package com.badlogic.gdx.assets.loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

import java.io.File;

import rma.ox.data.bdd.NoSqlDB;
import rma.ox.engine.utils.Logx;

public class NoSqlDBLoader extends AsynchronousAssetLoader<NoSqlDB, NoSqlDBLoader.NoSqlDBLoaderParameter> {

    private NoSqlDB noSqlDB;

    public NoSqlDBLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, NoSqlDBLoaderParameter parameter) {
        checkIfFolderExistAndCreate(fileName);
        boolean isWritable = parameter != null ? parameter.isWritable : true;
        boolean compressed = parameter != null ? parameter.compressed : true;
        Array<Class<?>> repositoriesAllowed = parameter != null ? parameter.repositoriesAllowed : null;
        noSqlDB = new NoSqlDB(Gdx.files.getLocalStoragePath()+fileName, isWritable, compressed, repositoriesAllowed);
    }

    @Override
    public NoSqlDB loadSync(AssetManager manager, String fileName, FileHandle file, NoSqlDBLoaderParameter parameter) {
        return noSqlDB;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, NoSqlDBLoaderParameter parameter) {
        return null;
    }

    static public class NoSqlDBLoaderParameter extends AssetLoaderParameters<NoSqlDB> {

        protected boolean isWritable = true;
        protected boolean compressed = true;
        protected Array<Class<?>> repositoriesAllowed;

        public NoSqlDBLoaderParameter(Array<Class<?>> repositoriesAllowed) {
            this.repositoriesAllowed = repositoriesAllowed;
        }

        public NoSqlDBLoaderParameter(boolean isWritable, boolean compressed, Array<Class<?>> repositoriesAllowed) {
            this.isWritable = isWritable;
            this.compressed = compressed;
            this.repositoriesAllowed = repositoriesAllowed;
        }
    }

    public static void checkIfFolderExistAndCreate(String pathDataBase) {
        // CHECK IF FILE EXIST IN INTERNAL AND THEM COPY IT TO LOCAL
        // BECAUSE NITRITE CAN OPEN ONLY FILE IN LOCAL
        FileHandle localHandler = Gdx.files.local(pathDataBase);
        FileHandle internalHandler = Gdx.files.internal(pathDataBase);
        if(!localHandler.exists()){
            localHandler.parent().mkdirs();
        }
        if(internalHandler.exists() && !localHandler.exists()){
            Gdx.files.internal(pathDataBase).copyTo(localHandler);
        }
    }
}
