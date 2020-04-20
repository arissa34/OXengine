package rma.ox.engine.core.utils;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class PixmapUtils {
    public static Pixmap CreatePixmap(TextureRegion textureRegion) {
        int widht = textureRegion.getRegionWidth();
        int height = textureRegion.getRegionHeight();

        //
        Pixmap pixmap = new Pixmap(widht, height, Pixmap.Format.RGBA8888);

        //
        Texture texture = textureRegion.getTexture();
        if (!texture.getTextureData().isPrepared()) {
            texture.getTextureData().prepare();
        }

        //
        Pixmap base_pixmap = texture.getTextureData().consumePixmap();

        for (int x = 0; x < widht; x++) {
            for (int y = 0; y < height; y++) {
                int colorInt = base_pixmap.getPixel(textureRegion.getRegionX() + x, textureRegion.getRegionY() + y);
                pixmap.drawPixel(x, y, colorInt);
            }
        }

        //
        base_pixmap.dispose();

        //
        return pixmap;
    }

    public static TextureAtlas CombineTextureAtlases(TextureAtlas... textureAtlases) {
        return CombineTextureAtlases(2048, 2048, Pixmap.Format.RGBA8888, 2, true, textureAtlases);
    }

    public static TextureAtlas CombineTextureAtlases(int width, int height, Pixmap.Format format, int padding, boolean duplicateBorder, TextureAtlas... textureAtlases) {
        //
        PixmapPacker pixmapPacker = new PixmapPacker(width, height, format, padding, duplicateBorder);

        //
        for (TextureAtlas textureAtlas : textureAtlases) {
            Array<TextureAtlas.AtlasRegion> main_regions = textureAtlas.getRegions();
            for (TextureAtlas.AtlasRegion region : main_regions) {
                String name = region.name;

                //
                Pixmap pixmap = CreatePixmap(region);
                pixmapPacker.pack(name, pixmap);

                pixmap.dispose();
            }
        }

        //
        TextureAtlas newTextureAtlas = pixmapPacker.generateTextureAtlas(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest, false);
        pixmapPacker.dispose();

        //
        return newTextureAtlas;
    }
}
