package com.badlogic.gdx.assets.loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Array;

public class FontGeneratorLoader  extends AsynchronousAssetLoader<BitmapFont, FontGeneratorLoader.FontGeneratorLoaderarameter> {

    private FreeTypeFontGenerator generator;

    public FontGeneratorLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, FontGeneratorLoaderarameter parameter) {
        generator = new FreeTypeFontGenerator(file);
    }

    @Override
    public BitmapFont loadSync(AssetManager manager, String fileName, FileHandle file, FontGeneratorLoaderarameter parameter) {
        return generator.generateFont(parameter.freeTypeFontParameter);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, FontGeneratorLoaderarameter parameter) {
        return null;
    }

    public static class FontGeneratorLoaderarameter extends AssetLoaderParameters<BitmapFont> {
        private FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter;

        public FontGeneratorLoaderarameter(FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter) {
            this.freeTypeFontParameter = freeTypeFontParameter;
        }
    }
}
