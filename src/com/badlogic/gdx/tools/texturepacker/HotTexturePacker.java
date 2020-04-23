/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.tools.texturepacker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData.Region;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.ProgressListener;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import rma.ox.engine.utils.Logx;

/**
 * @author Nathan Sweet
 */
public class HotTexturePacker extends TexturePacker {

    protected Packer packer;
    protected PixmapProcessor imageProcessor;
    protected Array<InputImage> inputImages = new Array();

    public HotTexturePacker(Settings settings) {
        rootPath = Gdx.files.getLocalStoragePath() + "/cache";
        this.settings = settings;

        if (settings.pot) {
            if (settings.maxWidth != MathUtils.nextPowerOfTwo(settings.maxWidth))
                throw new RuntimeException("If pot is true, maxWidth must be a power of two: " + settings.maxWidth);
            if (settings.maxHeight != MathUtils.nextPowerOfTwo(settings.maxHeight))
                throw new RuntimeException("If pot is true, maxHeight must be a power of two: " + settings.maxHeight);
        }

        if (settings.multipleOfFour) {
            if (settings.maxWidth % 4 != 0)
                throw new RuntimeException("If mod4 is true, maxWidth must be evenly divisible by 4: " + settings.maxWidth);
            if (settings.maxHeight % 4 != 0)
                throw new RuntimeException("If mod4 is true, maxHeight must be evenly divisible by 4: " + settings.maxHeight);
        }

        if (settings.grid)
            packer = new GridPacker2(settings);
        else
            packer = new MaxRectsPacker2(settings);

        imageProcessor = new PixmapProcessor(settings);
    }

    protected ImageProcessor newImageProcessor(Settings settings) {
        return new ImageProcessor(settings);
    }

    static final class InputImage {
        File file;
        String name;
        Pixmap image;
        int packedWidth;
        int packedHeight;
        int regionX;
        int regionY;
    }

    public void addImage(Pixmap image, String name, int w, int h, int rX, int rY) {
        InputImage inputImage = new InputImage();
        inputImage.image = image;
        inputImage.name = name;
        inputImage.packedWidth = w;
        inputImage.packedHeight = h;
        inputImage.regionX = rX;
        inputImage.regionY = rY;
        inputImages.add(inputImage);
    }

    public void addImage(Pixmap image, String name) {
        InputImage inputImage = new InputImage();
        inputImage.image = image;
        inputImage.name = name;
        inputImages.add(inputImage);
    }

    public void packOnMemory(String packFileName) {
        Logx.e(this.getClass(), "++++ packOnMemory packFileName : " + packFileName);
        if (progress == null) {
            progress = new ProgressListener() {
                public void progress(float progress) {
                }
            };
        }

        progress.start(1);
        int n = settings.scale.length;
        for (int i = 0; i < n; i++) {
            progress.start(1f / n);

            //imageProcessor.setScale(settings.scale[i]);

            //if (settings.scaleResampling != null && settings.scaleResampling.length > i && settings.scaleResampling[i] != null)
            //	imageProcessor.setResampling(settings.scaleResampling[i]);

            progress.start(0.35f);
            progress.count = 0;
            progress.total = inputImages.size;
            for (int ii = 0, nn = inputImages.size; ii < nn; ii++, progress.count++) {
                InputImage inputImage = inputImages.get(ii);
                imageProcessor.addImage(inputImage);
                if (progress.update(ii + 1, nn)) return;
            }
            progress.end();

            progress.start(0.35f);
            progress.count = 0;
            progress.total = imageProcessor.getImages().size;
            Array<Page> pages = packer.pack(progress, imageProcessor.getImages());
            progress.end();

            progress.start(0.29f);
            progress.count = 0;
            progress.total = pages.size;
            String scaledPackFileName = settings.getScaledPackFileName(packFileName, i);
            Array<Pixmap> canvasList = writeImages(scaledPackFileName, pages);
            Logx.e(this.getClass(), "++++ packOnMemory canvasList : " + canvasList.size);
            progress.end();

            progress.start(0.01f);
            //try {
            //    writePackFile(scaledPackFileName, pages);
            //} catch (IOException ex) {
            //    throw new RuntimeException("Error writing pack file.", ex);
            //}

            TextureAtlas atlas = new TextureAtlas();
            imageProcessor.clear();
            progress.end();

            progress.end();

            if (progress.update(i + 1, n)) return;
        }
        progress.end();
    }

    private Array<Pixmap> writeImages(String scaledPackFileName, Array<Page> pages) {

        File packFileNoExt = new File(rootPath, scaledPackFileName);
        File packDir = packFileNoExt.getParentFile();
        String imageName = packFileNoExt.getName();

        Logx.e(this.getClass(), "++++ writeImages imageName : " + imageName);
        Array<Pixmap> canvasList = new Array();
        int fileIndex = 0;
        for (int p = 0, pn = pages.size; p < pn; p++) {
            Page page = pages.get(p);

            int width = page.width, height = page.height;
            int edgePadX = 0, edgePadY = 0;
            if (settings.edgePadding) {
                edgePadX = settings.paddingX;
                edgePadY = settings.paddingY;
                if (settings.duplicatePadding) {
                    edgePadX /= 2;
                    edgePadY /= 2;
                }
                page.x = edgePadX;
                page.y = edgePadY;
                width += edgePadX * 2;
                height += edgePadY * 2;
            }
            if (settings.pot) {
                width = MathUtils.nextPowerOfTwo(width);
                height = MathUtils.nextPowerOfTwo(height);
            }
            if (settings.multipleOfFour) {
                width = width % 4 == 0 ? width : width + 4 - (width % 4);
                height = height % 4 == 0 ? height : height + 4 - (height % 4);
            }
            width = Math.max(settings.minWidth, width);
            height = Math.max(settings.minHeight, height);
            page.imageWidth = width;
            page.imageHeight = height;

            File outputFile;
            while (true) {
                outputFile = new File(packDir, imageName + (fileIndex++ == 0 ? "" : fileIndex) + "." + settings.outputFormat);
                if (!outputFile.exists()) break;
            }
            new FileHandle(outputFile).parent().mkdirs();
            page.imageName = outputFile.getName();

            Pixmap canvas = new Pixmap(width, height, Format.RGBA8888);
            canvasList.add(canvas);
            //if (!settings.silent) System.out.println("Writing " + canvas.getWidth() + "x" + canvas.getHeight() + ": " + outputFile);

            progress.start(1 / (float) pn);
            for (int r = 0, rn = page.outputRects.size; r < rn; r++) {
                Rect rect = page.outputRects.get(r);
                Pixmap image = rect.getImage();
                int iw = rect.regionWidth;
                int ih = rect.regionHeight;
                int rectX = page.x + rect.x, rectY = page.y + page.height - rect.y - (rect.regionHeight - settings.paddingY);
                copy(image, rect.offsetX + 0, rect.offsetY + 0, iw, ih, canvas, rectX, rectY, rect.rotated);

                if (progress.update(r + 1, rn)) return canvasList;
            }
            progress.end();

            Logx.e("++++ TEXTURE ATLAS MERGE DONE");
            //PixmapIO.writePNG(new FileHandle(outputFile), canvas);
            //try {
            //	//if (settings.premultiplyAlpha) canvas.getColorModel().coerceData(canvas.getRaster(), true);
            //	//ImageIO.write(canvas, "png", outputFile);
            //} catch (IOException ex) {
            //	throw new RuntimeException("Error writing file: " + outputFile, ex);
            //}

            if (progress.update(p + 1, pn)) return canvasList;
            progress.count++;
        }
        return canvasList;
    }

    static private void plot(Pixmap dst, int x, int y, int argb) {
        if (0 <= x && x < dst.getWidth() && 0 <= y && y < dst.getHeight())
            dst.drawPixel(x, y, argb);
    }

    static private void copy(Pixmap src, int x, int y, int w, int h, Pixmap dst, int dx, int dy, boolean rotated) {
        if (rotated) {
            for (int i = 0; i < w; i++)
                for (int j = 0; j < h; j++)
                    plot(dst, dx + j, dy + w - i - 1, src.getPixel(x + i, y + j));
        } else {
            for (int i = 0; i < w; i++)
                for (int j = 0; j < h; j++)
                    plot(dst, dx + i, dy + j, src.getPixel(x + i, y + j));
        }
    }

    private void writePackFile(String scaledPackFileName, Array<Page> pages) throws IOException {

        String atlasName = rootPath + "/" + scaledPackFileName + settings.atlasExtension;
        Logx.e(this.getClass(), "++++ writePackFile atlasName : " + atlasName);

        Writer writer = new OutputStreamWriter(new FileOutputStream(atlasName, true), "UTF-8");
        for (Page page : pages) {
            writer.write("\n" + page.imageName + "\n");
            writer.write("size: " + page.imageWidth + "," + page.imageHeight + "\n");
            writer.write("format: " + settings.format + "\n");
            writer.write("filter: " + settings.filterMin + "," + settings.filterMag + "\n");
            writer.write("repeat: " + getRepeatValue() + "\n");

            page.outputRects.sort();
            for (Rect rect : page.outputRects) {
                writeRect(writer, page, rect, rect.name);
                Array<Alias> aliases = new Array(rect.aliases.toArray());
                aliases.sort();
                for (Alias alias : aliases) {
                    Rect aliasRect = new Rect();
                    aliasRect.set(rect);
                    alias.apply(aliasRect);
                    writeRect(writer, page, aliasRect, alias.name);
                }
            }
        }
        writer.close();
    }

    private void writeRect(Writer writer, Page page, Rect rect, String name) throws IOException {
        writer.write(Rect.getAtlasName(name, settings.flattenPaths) + "\n");
        writer.write("  rotate: " + rect.rotated + "\n");
        writer
                .write("  xy: " + (page.x + rect.x) + ", " + (page.y + page.height - rect.y - (rect.height - settings.paddingY)) + "\n");

        writer.write("  size: " + rect.regionWidth + ", " + rect.regionHeight + "\n");
        if (rect.splits != null) {
            writer.write("  split: " //
                    + rect.splits[0] + ", " + rect.splits[1] + ", " + rect.splits[2] + ", " + rect.splits[3] + "\n");
        }
        if (rect.pads != null) {
            if (rect.splits == null) writer.write("  split: 0, 0, 0, 0\n");
            writer.write("  pad: " + rect.pads[0] + ", " + rect.pads[1] + ", " + rect.pads[2] + ", " + rect.pads[3] + "\n");
        }
        writer.write("  orig: " + rect.originalWidth + ", " + rect.originalHeight + "\n");
        writer.write("  offset: " + rect.offsetX + ", " + (rect.originalHeight - rect.regionHeight - rect.offsetY) + "\n");
        writer.write("  index: " + rect.index + "\n");
    }

    private String getRepeatValue() {
        if (settings.wrapX == TextureWrap.Repeat && settings.wrapY == TextureWrap.Repeat)
            return "xy";
        if (settings.wrapX == TextureWrap.Repeat && settings.wrapY == TextureWrap.ClampToEdge)
            return "x";
        if (settings.wrapX == TextureWrap.ClampToEdge && settings.wrapY == TextureWrap.Repeat)
            return "y";
        return "none";
    }

    /**
     * @param input        Directory containing individual images to be packed.
     * @param output       Directory where the pack file and page images will be written.
     * @param packFileName The name of the pack file. Also used to name the page images.
     * @param progress     May be null.
     */
    static public void process(Settings settings, String input, String output, String packFileName,
                               final ProgressListener progress) {
        try {
            TexturePackerFileProcessor processor = new TexturePackerFileProcessor(settings, packFileName, progress);
            processor.process(new File(input), new File(output));
        } catch (Exception ex) {
            throw new RuntimeException("Error packing images.", ex);
        }
    }

    /**
     * @return true if the output file does not yet exist or its last modification date is before the last modification date of
     * the input file
     */
    static public boolean isModified(String input, String output, String packFileName, Settings settings) {
        String packFullFileName = output;

        if (!packFullFileName.endsWith("/")) {
            packFullFileName += "/";
        }

        // Check against the only file we know for sure will exist and will be changed if any asset changes:
        // the atlas file
        packFullFileName += packFileName;
        packFullFileName += settings.atlasExtension;
        File outputFile = new File(packFullFileName);

        if (!outputFile.exists()) {
            return true;
        }

        File inputFile = new File(input);
        if (!inputFile.exists()) {
            throw new IllegalArgumentException("Input file does not exist: " + inputFile.getAbsolutePath());
        }

        return isModified(inputFile, outputFile.lastModified());
    }

    static private boolean isModified(File file, long lastModified) {
        if (file.lastModified() > lastModified) return true;
        File[] children = file.listFiles();
        if (children != null) {
            for (File child : children)
                if (isModified(child, lastModified)) return true;
        }
        return false;
    }

    static public boolean processIfModified(String input, String output, String packFileName) {
        // Default settings (Needed to access the default atlas extension string)
        Settings settings = new Settings();

        if (isModified(input, output, packFileName, settings)) {
            process(settings, input, output, packFileName);
            return true;
        }
        return false;
    }

    static public boolean processIfModified(Settings settings, String input, String output, String packFileName) {
        if (isModified(input, output, packFileName, settings)) {
            process(settings, input, output, packFileName);
            return true;
        }
        return false;
    }

    static public interface Packer {
        public Array<Page> pack(Array<Rect> inputRects);

        public Array<Page> pack(ProgressListener progress, Array<Rect> inputRects);
    }


    /**
     * @author Regnarock
     * @author Nathan Sweet
     */
    static public class Alias implements Comparable<Alias> {
        public String name;
        public int index;
        public int[] splits;
        public int[] pads;
        public int offsetX, offsetY, originalWidth, originalHeight;

        public Alias(Rect rect) {
            name = rect.name;
            index = rect.index;
            splits = rect.splits;
            pads = rect.pads;
            offsetX = rect.offsetX;
            offsetY = rect.offsetY;
            originalWidth = rect.originalWidth;
            originalHeight = rect.originalHeight;
        }

        public void apply(Rect rect) {
            rect.name = name;
            rect.index = index;
            rect.splits = splits;
            rect.pads = pads;
            rect.offsetX = offsetX;
            rect.offsetY = offsetY;
            rect.originalWidth = originalWidth;
            rect.originalHeight = originalHeight;
        }

        public int compareTo(Alias o) {
            return name.compareTo(o.name);
        }
    }

    /**
     * @author Nathan Sweet
     */
    static public class Page {
        public String imageName;
        public Array<Rect> outputRects, remainingRects;
        public float occupancy;
        public int x, y, width, height, imageWidth, imageHeight;
    }

    /**
     * @author Nathan Sweet
     */
    static public class Rect implements Comparable<Rect> {
        public String name;
        public int offsetX, offsetY, regionWidth, regionHeight, originalWidth, originalHeight;
        public int x, y;
        public int width, height; // Portion of page taken by this region, including padding.
        public int index;
        public boolean rotated;
        public Set<Alias> aliases = new HashSet<Alias>();
        public int[] splits;
        public int[] pads;
        public boolean canRotate = true;

        private boolean isPatch;
        private Pixmap image;
        int score1, score2;

        public Rect(Pixmap source, int left, int top, int newWidth, int newHeight, boolean isPatch) {
            image = source;
            offsetX = left;
            offsetY = top;
            regionWidth = newWidth;
            regionHeight = newHeight;
            originalWidth = source.getWidth();
            originalHeight = source.getHeight();
            width = newWidth;
            height = newHeight;
            this.isPatch = isPatch;
        }


        public Pixmap getImage() {
            return image;
        }

        Rect() {
        }

        Rect(Rect rect) {
            x = rect.x;
            y = rect.y;
            width = rect.width;
            height = rect.height;
        }

        void set(Rect rect) {
            name = rect.name;
            image = rect.image;
            offsetX = rect.offsetX;
            offsetY = rect.offsetY;
            regionWidth = rect.regionWidth;
            regionHeight = rect.regionHeight;
            originalWidth = rect.originalWidth;
            originalHeight = rect.originalHeight;
            x = rect.x;
            y = rect.y;
            width = rect.width;
            height = rect.height;
            index = rect.index;
            rotated = rect.rotated;
            aliases = rect.aliases;
            splits = rect.splits;
            pads = rect.pads;
            canRotate = rect.canRotate;
            score1 = rect.score1;
            score2 = rect.score2;
            isPatch = rect.isPatch;
        }

        public int compareTo(Rect o) {
            return name.compareTo(o.name);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            Rect other = (Rect) obj;
            if (name == null) {
                if (other.name != null) return false;
            } else if (!name.equals(other.name)) return false;
            return true;
        }

        @Override
        public String toString() {
            return name + (index != -1 ? "_" + index : "") + "[" + x + "," + y + " " + width + "x" + height + "]";
        }

        static public String getAtlasName(String name, boolean flattenPaths) {
            return flattenPaths ? new FileHandle(name).name() : name;
        }
    }
}
