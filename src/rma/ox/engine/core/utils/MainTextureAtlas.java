package rma.ox.engine.core.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.tools.texturepacker.HotTexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;

import rma.ox.engine.utils.Logx;

public class MainTextureAtlas {

    private static MainTextureAtlas instance;

    public static MainTextureAtlas get() {
        if (instance == null) {
            instance = new MainTextureAtlas();
            settings = new TexturePacker.Settings();
            settings.fast = true;
            settings.pot = true;
            settings.multipleOfFour = false;
            settings.maxWidth = 4096;
            settings.maxHeight = 4096;
            settings.rotation = false;
            settings.grid = false;
            settings.paddingX = 0;
            settings.paddingY = 0;
            settings.square = true;
            settings.bleed = false;
            settings.filterMin = Texture.TextureFilter.MipMap;
            settings.filterMag = Texture.TextureFilter.Linear;
            settings.premultiplyAlpha = true;
            settings.format = Pixmap.Format.RGBA8888;
            texturePacker = new HotTexturePacker(settings);
        } ;
        return instance;
    }

    /*******************************/
    private static TexturePacker.Settings settings;
    private ArrayMap<String, TextureAtlas> listAtlas = new ArrayMap<>();
    private static HotTexturePacker texturePacker ;
    private TextureAtlas mainAltas = new TextureAtlas();

    public TextureAtlas getMainAltas() {
        Logx.e(MainTextureAtlas.class, "===+++ getMainAltas ");
        return mainAltas;
    }

    public void setMainAltas(TextureAtlas mainAltas) {
        Logx.e(MainTextureAtlas.class, "===+++ setMainAltas ");
        this.mainAltas = mainAltas;
    }

    public void addTextureAtals(String path, TextureAtlas atlas) {
        Logx.e(MainTextureAtlas.class, "===+++ ADD atlas : " + path);

        if (listAtlas.containsKey(path)) return;

        Array<TextureAtlas.AtlasRegion> mainRegions = atlas.getRegions();
        TextureData textureData = mainRegions.get(0).getTexture().getTextureData();
        if (!textureData.isPrepared()) {
            textureData.prepare();
        }
        Pixmap pixmap = textureData.consumePixmap();
        for (int i = 0; i < mainRegions.size; i++) {
            TextureAtlas.AtlasRegion region = mainRegions.get(i);
            String name = region.name;

            Logx.e(MainTextureAtlas.class, "===+++ packer.pack : " + name);
            extractPixmapFromTextureRegion(pixmap, region);
        }

        listAtlas.put(path, atlas);
    }

    public TextureAtlas updateTheMainTextureAtlas() {
        Logx.e(MainTextureAtlas.class, "===+++ updateTheMainTextureAtlas ");
        texturePacker.packOnMemory("mainAtlas");
        return mainAltas;
    }

    public static void extractPixmapFromTextureRegion(Pixmap pixmap, TextureAtlas.AtlasRegion region) {

        texturePacker.addImage(pixmap, region.name, region.packedWidth, region.packedHeight, (int)region.getRegionX(), (int)region.getRegionY());

        //Pixmap pixmap = ;
       // Pixmap pixmap = new Pixmap(
       //         region.packedWidth,
       //         region.packedHeight,
       //         textureData.getFormat()
       // );
//
       // pixmap.drawPixmap(
       //         textureData.consumePixmap(), // The other Pixmap
       //         0, // The target x-coordinate (top left corner)
       //         0, // The target y-coordinate (top left corner)
       //         region.getRegionX(), // The source x-coordinate (top left corner)
       //         region.getRegionY(), // The source y-coordinate (top left corner)
       //         region.packedWidth, // The width of the area from the other Pixmap in pixels
       //         region.packedHeight // The height of the area from the other Pixmap in pixels
       // );

/*
        BufferedImage bufferedImage = new BufferedImage(region.packedWidth,
                region.packedHeight,
                BufferedImage.TYPE_INT_ARGB);

        Color color = new Color();
        for (int x = 0; x < region.getRegionWidth(); x++) {
            for (int y = 0; y < region.getRegionHeight(); y++) {
                int colorInt = pixmap.getPixel(region.getRegionX() + x, region.getRegionY() + y);
                color.set(colorInt);
                plot(bufferedImage, x, y, Color.argb8888(color));
                // you could now draw that color at (x, y) of another pixmap of the size (regionWidth, regionHeight)
            }
        }
 */
        //if(region.rotate){
        //    pixmap = flipPixmap(rotatePixmap(pixmap));
        //}
        //return pixmap;
    }

    static private void plot (BufferedImage dst, int x, int y, int argb) {
        if (0 <= x && x < dst.getWidth() && 0 <= y && y < dst.getHeight()) dst.setRGB(x, y, argb);
    }

    private static Pixmap rotatePixmap(Pixmap srcPix) {
        final int width = srcPix.getWidth();
        final int height = srcPix.getHeight();
        Pixmap rotatedPix = new Pixmap(height, width, srcPix.getFormat());

        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                rotatedPix.drawPixel(x, y, srcPix.getPixel(y, x));
            }
        }

        srcPix.dispose();
        return rotatedPix;
    }

    public static Pixmap flipPixmap(Pixmap p) {
        int w = p.getWidth();
        int h = p.getHeight();
        int hold;

        //change blending to 'none' so that alpha areas will not show
        //previous orientation of image
        p.setBlending(Pixmap.Blending.None);
        for (int y = 0; y < h / 2; y++) {
            for (int x = 0; x < w / 2; x++) {
                //get color of current pixel
                hold = p.getPixel(x, y);
                //draw color of pixel from opposite side of pixmap to current position
                p.drawPixel(x, y, p.getPixel(w - x - 1, y));
                //draw saved color to other side of pixmap
                p.drawPixel(w - x - 1, y, hold);
                //repeat for height/width inverted pixels
                hold = p.getPixel(x, h - y - 1);
                p.drawPixel(x, h - y - 1, p.getPixel(w - x - 1, h - y - 1));
                p.drawPixel(w - x - 1, h - y - 1, hold);
            }
        }
        //set blending back to default
        p.setBlending(Pixmap.Blending.SourceOver);
        return p;
    }

}
