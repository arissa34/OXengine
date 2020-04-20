package com.badlogic.gdx.assets.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData.Page;
import com.badlogic.gdx.utils.Array;

import rma.ox.engine.core.utils.MainTextureAtlas;
import rma.ox.engine.utils.Logx;

public class TextureAtlasLoader2 extends SynchronousAssetLoader<TextureAtlas, TextureAtlasLoader2.TextureAtlasParameter> {
    public TextureAtlasLoader2 (FileHandleResolver resolver) {
        super(resolver);
    }

    TextureAtlasData data;

    @Override
    public TextureAtlas load (AssetManager assetManager, String fileName, FileHandle file, TextureAtlasParameter parameter) {
        for (Page page : data.getPages()) {
            Texture texture = assetManager.get(page.textureFile.path().replaceAll("\\\\", "/"), Texture.class);
            page.texture = texture;
        }

        TextureAtlas atlas = new TextureAtlas(data);
        data = null;
        if(parameter!= null && parameter.merge) {
            MainTextureAtlas.get().addTextureAtals(fileName, atlas);
            MainTextureAtlas.get().updateTheMainTextureAtlas();
        }
        return atlas;
    }

    @Override
    public Array<AssetDescriptor> getDependencies (String fileName, FileHandle atlasFile, TextureAtlasParameter parameter) {
        FileHandle imgDir = atlasFile.parent();

        if (parameter != null)
            data = new TextureAtlasData(atlasFile, imgDir, parameter.flip);
        else {
            data = new TextureAtlasData(atlasFile, imgDir, false);
        }

        Array<AssetDescriptor> dependencies = new Array();
        for (Page page : data.getPages()) {
            TextureParameter params = new TextureParameter();
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
        public boolean merge = false;

        public TextureAtlasParameter () {
        }

        public TextureAtlasParameter (boolean flip) {
            this.flip = flip;
        }

        public TextureAtlasParameter (boolean flip, boolean merge) {
            this.flip = flip;
            this.merge = merge;
        }
    }
}