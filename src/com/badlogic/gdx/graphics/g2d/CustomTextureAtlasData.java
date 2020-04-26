package com.badlogic.gdx.graphics.g2d;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData;

public class CustomTextureAtlasData extends TextureAtlasData {

    public FileHandle textureFile;
    public Texture texture;
    public float width, height;
    public boolean useMipMaps;
    public Pixmap.Format format;
    public Texture.TextureFilter minFilter;
    public Texture.TextureFilter magFilter;
    public Texture.TextureWrap uWrap;
    public Texture.TextureWrap vWrap;


    public CustomTextureAtlasData(FileHandle packFile, FileHandle imagesDir, boolean flip) {
        super(packFile, imagesDir, flip);
    }
}
