package rma.ox.engine.core.utils;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.tools.texturepacker.HotTexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

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
            settings.duplicatePadding = false;
            settings.maxWidth = 4096;
            settings.maxHeight = 4096;
            settings.rotation = false;
            settings.grid = false;
            settings.paddingX = 1;
            settings.paddingY = 1;
            settings.square = true;
            settings.bleed = true;
            settings.filterMin = Texture.TextureFilter.MipMap;
            settings.filterMag = Texture.TextureFilter.Linear;
            settings.premultiplyAlpha = true;
            settings.format = Pixmap.Format.RGBA8888;
            texturePacker = new HotTexturePacker(settings);
        }
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
            texturePacker.addImage(pixmap, region.name, region.packedWidth, region.packedHeight, region.originalWidth, region.originalHeight, region.getRegionX(), region.getRegionY(), region.offsetX, region.offsetY, region.rotate);
            //Logx.e(MainTextureAtlas.class, "===+++ packer.pack : " + name+ " is rotated "+region.rotate);
        }

        listAtlas.put(path, atlas);
    }

    public TextureAtlas updateTheMainTextureAtlas() {
        Logx.e(MainTextureAtlas.class, "===+++ updateTheMainTextureAtlas ");
        texturePacker.packOnMemory("mainAtlas");
        return mainAltas;
    }

}
