package com.badlogic.gdx.tools.texturepacker;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Array;

import java.util.regex.Pattern;

import rma.ox.engine.utils.Logx;

public class PixmapProcessor {

    static private final Pixmap emptyImage = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
    static private Pattern indexPattern = Pattern.compile("(.+)_(\\d+)$");
    private final TexturePacker.Settings settings;
    private final Array<HotTexturePacker.Rect> rects = new Array();
    private float scale = 1;

    public PixmapProcessor(TexturePacker.Settings settings) {
        this.settings = settings;
    }

    public HotTexturePacker.Rect addImage(HotTexturePacker.InputImage inputImage) {
        HotTexturePacker.Rect rect = processImage(inputImage);

        if (rect == null) {
            if (!settings.silent)
                System.out.println("Ignoring blank input image: " + inputImage.name);
            return null;
        }

        rects.add(rect);
        return rect;
    }

    protected HotTexturePacker.Rect processImage(HotTexturePacker.InputImage inputImage) {
        if (scale <= 0) throw new IllegalArgumentException("scale cannot be <= 0: " + scale);

        // Strip digits off end of name and use as index.
        int index = -1;

        HotTexturePacker.Rect rect = stripWhitespace(inputImage);
        if (rect == null) return null;

        rect.name = inputImage.name;
        rect.rotated = inputImage.isRotated;
        rect.originalWidth = inputImage.origW;
        rect.originalHeight = inputImage.origH;
        rect.offX = inputImage.offsetX;
        rect.offY = inputImage.offsetY;
        //Logx.e("++++ name : "+rect.name+" rect.offsetX : "+rect.offX+ " rect.offsetY : "+rect.offY);
        rect.index = index;
        return rect;
    }

    public void clear() {
        rects.clear();
    }

    static public interface Packer {
        public Array<HotTexturePacker.Page> pack(Array<HotTexturePacker.Rect> inputRects);

        public Array<HotTexturePacker.Page> pack(HotTexturePacker.ProgressListener progress, Array<HotTexturePacker.Rect> inputRects);
    }

    public Array<HotTexturePacker.Rect> getImages() {
        return rects;
    }

    /**
     * Strips whitespace and returns the rect, or null if the image should be ignored.
     */
    protected HotTexturePacker.Rect stripWhitespace(HotTexturePacker.InputImage inputImage) {
        int left = inputImage.regionX - settings.paddingX;
        int top = inputImage.regionY - settings.paddingY;
        int newWidth = inputImage.packedWidth + settings.paddingX * 2;
        int newHeight = inputImage.packedHeight + settings.paddingY * 2;
        //Logx.e("++++ name : " + inputImage.name + " left : " + left + " top : " + top + " newWidth : " + newWidth + " newHeight : " + newHeight);
        return new HotTexturePacker.Rect(inputImage.image, left, top, newWidth, newHeight, false);
    }

}
