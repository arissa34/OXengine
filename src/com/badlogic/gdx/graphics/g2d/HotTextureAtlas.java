package com.badlogic.gdx.graphics.g2d;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.HotTexturePacker;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;

import static com.badlogic.gdx.tools.texturepacker.HotTexturePacker.Page;
import static com.badlogic.gdx.tools.texturepacker.HotTexturePacker.Settings;
import static com.badlogic.gdx.tools.texturepacker.HotTexturePacker.Rect;

public class HotTextureAtlas extends TextureAtlas {

    public HotTextureAtlas(){

    }

    public void hotLoad(Settings settings, Array<Page> pages){
        for (int i = 0; i < pages.size; i++) {
            Page page = pages.get(i);
            page.texture.setFilter(settings.filterMin, settings.filterMag);
            page.texture.setWrap(settings.wrapX, settings.wrapY);
            if(!getTextures().contains(page.texture)){
                getTextures().add(page.texture);
            }

            for (int j =0; j < page.outputRects.size; j++) {
                Rect rect = page.outputRects.get(j);
                AtlasRegion atlasRegion = new AtlasRegion(page.texture, (page.x + rect.x), (page.y + page.height - rect.y - (rect.height - settings.paddingY)),
                        rect.regionWidth,  rect.regionHeight );
                atlasRegion.index = rect.index;
                atlasRegion.name = Rect.getAtlasName(rect.name, settings.flattenPaths);
                atlasRegion.offsetX = rect.offX;
                atlasRegion.offsetY = rect.offY;
                atlasRegion.originalWidth = rect.originalWidth;
                atlasRegion.originalHeight = rect.originalHeight;
                atlasRegion.rotate = rect.rotated;
                atlasRegion.degrees = rect.rotated ? 90 : 0;
                getRegions().add(atlasRegion);
            }
        }
    }

}
