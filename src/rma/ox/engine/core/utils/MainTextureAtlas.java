package rma.ox.engine.core.utils;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

import rma.ox.engine.utils.Logx;

public class MainTextureAtlas {

    private static MainTextureAtlas instance;

    public static MainTextureAtlas get() {
        if (instance == null) instance = new MainTextureAtlas();
        return instance;
    }

    /*******************************/

    private PixmapPacker packer = new PixmapPacker(2048, 2048, Pixmap.Format.RGBA8888, 1, false);
    private ArrayMap<String, TextureAtlas> listAtlas = new ArrayMap<>();
    private TextureAtlas mainAltas = new TextureAtlas();

    public TextureAtlas getMainAltas() {
        Logx.e(MainTextureAtlas.class, "===+++ getMainAltas ");
        return mainAltas;
    }

    public void addTextureAtals(String path, TextureAtlas atlas){
        Logx.e(MainTextureAtlas.class, "===+++ ADD atlas : "+path);
        listAtlas.put(path, atlas);
    }

    public TextureAtlas updateTheMainTextureAtlas(){
        Logx.e(MainTextureAtlas.class, "===+++ updateTheMainTextureAtlas ");
        //packer.dispose();
        //packer = new PixmapPacker(4096, 4096, Pixmap.Format.RGBA8888, 1, false);
        for(int j = 0; j < listAtlas.size; j ++){
            TextureAtlas atlas = listAtlas.getValueAt(j);
            Array<TextureAtlas.AtlasRegion> mainRegions = atlas.getRegions();
            for (int i =0; i < mainRegions.size; i ++) {
                TextureAtlas.AtlasRegion region = mainRegions.get(i);
                String name = region.name;

                Pixmap pixmap = extractPixmapFromTextureRegion(region);
                //Logx.e(MainTextureAtlas.class, "===+++ packer.pack : " + name);
                packer.pack(name, pixmap);
                pixmap.dispose();

            }
        }
        listAtlas.clear();
        Logx.e(MainTextureAtlas.class, "===+++ updateTextureAtlas ");
        packer.updateTextureAtlas(mainAltas, Texture.TextureFilter.MipMap, Texture.TextureFilter.Linear, true, false);
        return mainAltas;
    }


    public static Pixmap extractPixmapFromTextureRegion(TextureAtlas.AtlasRegion region) {
        TextureData textureData = region.getTexture().getTextureData();
        if (!textureData.isPrepared()) {
            textureData.prepare();
        }

        Pixmap pixmap = new Pixmap(
                region.packedWidth,
                region.packedHeight,
                textureData.getFormat()
        );
        pixmap.drawPixmap(
                textureData.consumePixmap(), // The other Pixmap
                0, // The target x-coordinate (top left corner)
                0, // The target y-coordinate (top left corner)
                region.getRegionX(), // The source x-coordinate (top left corner)
                region.getRegionY(), // The source y-coordinate (top left corner)
                region.packedWidth, // The width of the area from the other Pixmap in pixels
                region.packedHeight // The height of the area from the other Pixmap in pixels
        );

        if(region.rotate){
            pixmap = flipPixmap(rotatePixmap(pixmap));
        }
        return pixmap;
    }

    private static Pixmap rotatePixmap (Pixmap srcPix){
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

    public static Pixmap flipPixmap( Pixmap p ){
        int w = p.getWidth();
        int h = p.getHeight();
        int hold;

        //change blending to 'none' so that alpha areas will not show
        //previous orientation of image
        p.setBlending(Pixmap.Blending.None);
        for (int y = 0; y < h / 2; y++) {
            for (int x = 0; x < w / 2; x++) {
                //get color of current pixel
                hold = p.getPixel(x,y);
                //draw color of pixel from opposite side of pixmap to current position
                p.drawPixel(x,y, p.getPixel(w-x-1, y));
                //draw saved color to other side of pixmap
                p.drawPixel(w-x-1,y, hold);
                //repeat for height/width inverted pixels
                hold = p.getPixel(x, h-y-1);
                p.drawPixel(x,h-y-1, p.getPixel(w-x-1,h-y-1));
                p.drawPixel(w-x-1,h-y-1, hold);
            }
        }
        //set blending back to default
        p.setBlending(Pixmap.Blending.SourceOver);
        return p;
    }

}
