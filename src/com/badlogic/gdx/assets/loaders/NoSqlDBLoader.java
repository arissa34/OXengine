package com.badlogic.gdx.assets.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

import java.io.File;

import rma.ox.data.bdd.db.NoSqlDB;
import rma.ox.engine.utils.Logx;

public class NoSqlDBLoader extends AsynchronousAssetLoader<NoSqlDB, NoSqlDBLoader.NoSqlDBLoaderParameter> {

    private static final String TAG = NoSqlDBLoader.class.getSimpleName();

    private NoSqlDB noSqlDB;

    public NoSqlDBLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, NoSqlDBLoaderParameter parameter) {
        return null;
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, NoSqlDBLoaderParameter parameter) {
        checkIfFolderExistAndCreate(fileName);
        noSqlDB = new NoSqlDB(fileName, parameter.isWritable, parameter.compressed);
    }

    @Override
    public NoSqlDB loadSync(AssetManager manager, String fileName, FileHandle file, NoSqlDBLoaderParameter parameter) {
        return noSqlDB;
    }

    static public class NoSqlDBLoaderParameter extends AssetLoaderParameters<NoSqlDB> {

        public boolean isWritable = true;
        public boolean compressed = true;

        public NoSqlDBLoaderParameter() {

        }

        public NoSqlDBLoaderParameter(boolean isWritable, boolean compressed) {
            this.isWritable = isWritable;
            this.compressed = compressed;
        }
    }

    private void checkIfFolderExistAndCreate(String fileName) {
        String nativeDir = fileName.substring(0, fileName.lastIndexOf(File.separator));
        File folder = new File(nativeDir);
        if (!folder.exists()) {
            Logx.d(this.getClass(), "NoSqlDB db file location not exist " + nativeDir);
            if (folder.mkdir()) {
                Logx.d(this.getClass(), "NoSqlDB db file location mkdir " + nativeDir);
            }
        }
    }
}
