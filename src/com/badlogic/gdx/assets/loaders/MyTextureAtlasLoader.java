package com.badlogic.gdx.assets.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.HotTextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

import rma.ox.engine.core.utils.MainTextureAtlas;
import rma.ox.engine.utils.Logx;

public class MyTextureAtlasLoader extends AsynchronousAssetLoader<TextureAtlas, MyTextureAtlasLoader.TextureAtlasParameter> {

    TextureAtlas.TextureAtlasData data;

    public MyTextureAtlasLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, MyTextureAtlasLoader.TextureAtlasParameter parameter) {
        if(parameter != null && parameter.useInMainTextureAtlas){
            Logx.e( getClass(), "#### fileName "+fileName);
            HotTextureAtlas.FakeTextureAtlasData fakeTextureAtlasData = new HotTextureAtlas.FakeTextureAtlasData(file);
            MainTextureAtlas.get().getMainAltas().addFakeRegion(fakeTextureAtlasData);
            MainTextureAtlas.get().addAtlasToLoad(fileName);
        }
    }

    @Override
    public TextureAtlas loadSync(AssetManager manager, String fileName, FileHandle file, MyTextureAtlasLoader.TextureAtlasParameter parameter) {
        if(parameter != null && parameter.useInMainTextureAtlas){
            return MainTextureAtlas.get().getMainAltas();
        }
        for (TextureAtlas.TextureAtlasData.Page page : data.getPages()) {
            Texture texture = manager.get(page.textureFile.path().replaceAll("\\\\", "/"), Texture.class);
            page.texture = texture;
        }

        TextureAtlas atlas = new TextureAtlas(data);
        data = null;
        return atlas;
    }

    @Override
    public Array<AssetDescriptor> getDependencies (String fileName, FileHandle atlasFile, MyTextureAtlasLoader.TextureAtlasParameter parameter) {
        if(parameter != null && parameter.useInMainTextureAtlas) return null;

        FileHandle imgDir = atlasFile.parent();

        if (parameter != null)
            data = new TextureAtlas.TextureAtlasData(atlasFile, imgDir, parameter.flip);
        else {
            data = new TextureAtlas.TextureAtlasData(atlasFile, imgDir, false);
        }

        Array<AssetDescriptor> dependencies = new Array();
        for (TextureAtlas.TextureAtlasData.Page page : data.getPages()) {
            TextureLoader.TextureParameter params = new TextureLoader.TextureParameter();
            params.format = page.format;
            params.genMipMaps = page.useMipMaps;
            params.minFilter = page.minFilter;
            params.magFilter = page.magFilter;
            dependencies.add(new AssetDescriptor(page.textureFile, Texture.class, params));
        }
        return dependencies;
    }

    static public class TextureAtlasParameter extends AssetLoaderParameters<TextureAtlas> {
        /** whether to flip the texture atlas vertically **/
        public boolean flip = false;
        public boolean useInMainTextureAtlas = false;

        public TextureAtlasParameter () {
        }

        public TextureAtlasParameter setFlip(boolean flip) {
            this.flip = flip;
            return this;
        }

        public TextureAtlasParameter setUseInMainTextureAtlas(boolean useInMainTextureAtlas) {
            this.useInMainTextureAtlas = useInMainTextureAtlas;
            return this;
        }
    }
}
