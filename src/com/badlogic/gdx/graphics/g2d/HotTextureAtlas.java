package com.badlogic.gdx.graphics.g2d;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.StreamUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import rma.ox.engine.utils.Logx;

import static com.badlogic.gdx.graphics.Texture.TextureWrap.ClampToEdge;
import static com.badlogic.gdx.graphics.Texture.TextureWrap.Repeat;
import static com.badlogic.gdx.tools.texturepacker.HotTexturePacker.Page;
import static com.badlogic.gdx.tools.texturepacker.HotTexturePacker.Settings;
import static com.badlogic.gdx.tools.texturepacker.HotTexturePacker.Rect;

public class HotTextureAtlas extends TextureAtlas {

    public static class FakeTextureAtlasData {
        protected Array<TextureAtlasData.Region> regions = new Array();

        public FakeTextureAtlasData(FileHandle packFile) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(packFile.read()), 64);
            try {
                TextureAtlasData.Page pageImage = null;
                while (true) {
                    String line = reader.readLine();
                    if (line == null) break;
                    if (line.trim().length() == 0)
                        pageImage = null;
                    else if (pageImage == null) {

                        float width = 0, height = 0;
                        if (readTuple(reader) == 2) { // size is only optional for an atlas packed with an old TexturePacker.
                            width = Integer.parseInt(tuple[0]);
                            height = Integer.parseInt(tuple[1]);
                            readTuple(reader);
                        }
                        Pixmap.Format format = Pixmap.Format.valueOf(tuple[0]);

                        readTuple(reader);
                        Texture.TextureFilter min = Texture.TextureFilter.valueOf(tuple[0]);
                        Texture.TextureFilter max = Texture.TextureFilter.valueOf(tuple[1]);

                        String direction = readValue(reader);
                        Texture.TextureWrap repeatX = ClampToEdge;
                        Texture.TextureWrap repeatY = ClampToEdge;
                        if (direction.equals("x"))
                            repeatX = Repeat;
                        else if (direction.equals("y"))
                            repeatY = Repeat;
                        else if (direction.equals("xy")) {
                            repeatX = Repeat;
                            repeatY = Repeat;
                        }

                        pageImage = new TextureAtlasData.Page(null, width, height, min.isMipMap(), format, min, max, repeatX, repeatY);

                    } else {
                        String rotateValue = readValue(reader);
                        int degrees;
                        if (rotateValue.equalsIgnoreCase("true"))
                            degrees = 90;
                        else if (rotateValue.equalsIgnoreCase("false"))
                            degrees = 0;
                        else
                            degrees = Integer.valueOf(rotateValue);

                        readTuple(reader);
                        int left = Integer.parseInt(tuple[0]);
                        int top = Integer.parseInt(tuple[1]);

                        readTuple(reader);
                        int width = Integer.parseInt(tuple[0]);
                        int height = Integer.parseInt(tuple[1]);

                        TextureAtlasData.Region region = new TextureAtlasData.Region();
                        region.page = pageImage;
                        region.left = left;
                        region.top = top;
                        region.width = width;
                        region.height = height;
                        region.name = line;
                        region.rotate = degrees == 90;
                        region.degrees = degrees;

                        if (readTuple(reader) == 4) { // split is optional
                            region.splits = new int[]{Integer.parseInt(tuple[0]), Integer.parseInt(tuple[1]),
                                    Integer.parseInt(tuple[2]), Integer.parseInt(tuple[3])};

                            if (readTuple(reader) == 4) { // pad is optional, but only present with splits
                                region.pads = new int[]{Integer.parseInt(tuple[0]), Integer.parseInt(tuple[1]),
                                        Integer.parseInt(tuple[2]), Integer.parseInt(tuple[3])};

                                readTuple(reader);
                            }
                        }

                        region.originalWidth = Integer.parseInt(tuple[0]);
                        region.originalHeight = Integer.parseInt(tuple[1]);

                        readTuple(reader);
                        region.offsetX = Integer.parseInt(tuple[0]);
                        region.offsetY = Integer.parseInt(tuple[1]);

                        region.index = Integer.parseInt(readValue(reader));

                        regions.add(region);
                    }
                }
            } catch (Exception ex) {
                throw new GdxRuntimeException("Error reading pack file: " + packFile, ex);
            } finally {
                StreamUtils.closeQuietly(reader);
            }
        }
    }

    private static Texture emptyTexture = new Texture(new Pixmap(1, 1, Pixmap.Format.RGBA8888)) ;

    public HotTextureAtlas() {
    }

    public void addFakeRegion(FakeTextureAtlasData data) {
        if (!getTextures().contains(emptyTexture)) {
            getTextures().add(emptyTexture);
        }

        for (int j = 0; j < data.regions.size; j++) {
            TextureAtlas.TextureAtlasData.Region region = data.regions.get(j);

            AtlasRegion atlasRegion = new AtlasRegion(emptyTexture, 0, 0,
                    1,  1);
            atlasRegion.index = region.index;
            atlasRegion.name = region.name;
            atlasRegion.rotate = region.rotate;
            atlasRegion.degrees = region.degrees;
            getRegions().add(atlasRegion);
        }
    }

    public void hotLoad(Settings settings, Array<Page> pages) {
        Logx.e(getClass(), "#### hotLoad");
        getTextures().clear();
        getRegions().clear();
        for (int i = 0; i < pages.size; i++) {
            Page page = pages.get(i);
            page.texture.setFilter(settings.filterMin, settings.filterMag);
            page.texture.setWrap(settings.wrapX, settings.wrapY);
            if (!getTextures().contains(page.texture)) {
                getTextures().add(page.texture);
            }
            for (int j = 0; j < page.outputRects.size; j++) {
                Rect rect = page.outputRects.get(j);
                AtlasRegion atlasRegion = new AtlasRegion(page.texture, (page.x + rect.x), (page.y + page.height - rect.y - (rect.height - settings.paddingY)),
                            rect.regionWidth, rect.regionHeight);
                atlasRegion.index = rect.index;
                atlasRegion.name = Rect.getAtlasName(rect.name, settings.flattenPaths);
                atlasRegion.offsetX = rect.offX;
                atlasRegion.offsetY = rect.offY;
                atlasRegion.originalWidth = rect.originalWidth;
                atlasRegion.originalHeight = rect.originalHeight;
                atlasRegion.packedWidth = rect.regionWidth;
                atlasRegion.packedHeight = rect.regionHeight;
                atlasRegion.rotate = rect.rotated;
                atlasRegion.degrees = rect.rotated ? 90 : 0;
                int index = getRegions().indexOf(getRegion(atlasRegion.name), true);
                if(index > -1){
                    getRegions().set(index, atlasRegion);
                }else {
                    getRegions().add(atlasRegion);
                }
            }
        }
    }

    public AtlasRegion getRegion(String name){
        for(int i =0; i < getRegions().size; i ++){
            if(getRegions().get(i).name.equals(name)){
                return getRegions().get(i);
            }
        }
        return null;
    }

}
