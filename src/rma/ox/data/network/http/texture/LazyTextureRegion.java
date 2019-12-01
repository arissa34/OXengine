package rma.ox.data.network.http.texture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.FlushablePool;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

import java.io.File;

import rma.ox.engine.ressource.MyAssetManager;
import rma.ox.engine.utils.Logx;

public class LazyTextureRegion {

    private static final LazyTextureRegion instance = new LazyTextureRegion();
    private static String placeHolderTexturePath;
    private static boolean cacheEnable = false;
    private static boolean assetEnable = false;
    private static final String basePath = "cache/img/";

    public static LazyTextureRegion initPlaceHolder(String placeHolderPath) {
        if(placeHolderPath == null || placeHolderPath.isEmpty()){
            throw new GdxRuntimeException("placeHolderPath null or empty !");
        }

        placeHolderTexturePath = placeHolderPath;
        if (!Gdx.files.local(placeHolderPath).exists()) {
            placeHolderTexturePath = null;
            throw new GdxRuntimeException("placeHolderPath not found !");
        }
        return instance;
    }

    public static LazyTextureRegion enableCache(){
        cacheEnable = true;
        return instance;
    }

    public static LazyTextureRegion enableAssets(){
        assetEnable = true;
        return instance;
    }

    public static TextureRegion load(String placeHolderPath, String url) {
        if(placeHolderPath == null || placeHolderPath.isEmpty()){
            throw new GdxRuntimeException("placeHolderPath null or empty !");
        }
        if(url == null || url.isEmpty()){
            throw new GdxRuntimeException("url null or empty !");
        }
        TextureRegion textureRegion = new TextureRegion();
        if (assetEnable && MyAssetManager.get().contains(url, Texture.class)) {
            Logx.l("EXIST IN MANAGER");
            textureRegion.setRegion(MyAssetManager.get().get(url, Texture.class));
            return textureRegion;
        } else if (cacheEnable && Gdx.files.local(getSafeUrl(url)).exists()) {
            Logx.l("EXIST IN CACHE");
            instance.loadFromCache(textureRegion, url);
        } else {
            Logx.l("NEED REQUEST");
            Texture placeHolder;
            if (MyAssetManager.get().contains(placeHolderPath, Texture.class)) {
                placeHolder = MyAssetManager.get().get(placeHolderPath, Texture.class);
            } else {
                placeHolder = new Texture(placeHolderPath);
                MyAssetManager.get().addAsset(placeHolderPath, Texture.class, placeHolder);
            }
            textureRegion.setRegion(placeHolder);
            instance.launchRequest(textureRegion, url);
        }
        return textureRegion;
    }

    public static TextureRegion load(String url) {
        return load(placeHolderTexturePath, url);
    }

    private static String getSafeUrl(String url) {
        return url.replaceAll(":", "").replaceAll("/", "_");
    }

    public static void flushPool() {
        instance.flush();
    }

    /*******************************/

    private HttpRequestBuilder httpRequestBuilder;

    public LazyTextureRegion() {
        httpRequestBuilder = new HttpRequestBuilder();
    }

    private void launchRequest(TextureRegion placeHolder, String url) {
        httpRequestBuilder.newRequest()
                .method(Net.HttpMethods.GET)
                .url(url);
        Net.HttpRequest request = httpRequestBuilder.build();
        Gdx.net.sendHttpRequest(request, pool.obtain().init(request, placeHolder));
    }

    protected TextureRegion loadFromCache(TextureRegion textureRegion, String url) {
        Texture texture = new Texture(Gdx.files.local(getSafeUrl(url)));
        textureRegion.setRegion(texture);
        MyAssetManager.get().addAsset(url, Texture.class, texture);
        return textureRegion;
    }

    public void flush() {
        pool.flush();
    }

    /*******************************/

    protected FlushablePool<Response> pool = new FlushablePool<Response>() {
        @Override
        protected Response newObject() {
            return new Response();
        }
    };

    /*******************************/

    protected class Response implements Net.HttpResponseListener, Runnable, Pool.Poolable {

        private byte[] rawImageBytes;
        private TextureRegion placeHolder;
        private Net.HttpRequest request;
        private Pixmap pixmap;

        public Response init(Net.HttpRequest request, TextureRegion placeHolder) {
            this.request = request;
            this.placeHolder = placeHolder;
            return this;
        }

        @Override
        public void handleHttpResponse(Net.HttpResponse httpResponse) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int statusCode = httpResponse.getStatus().getStatusCode();
            if (statusCode == 200) {
                rawImageBytes = httpResponse.getResult();
                pixmap = new Pixmap(rawImageBytes, 0, rawImageBytes.length);
                Gdx.app.postRunnable(
                        Response.this::run
                );
            } else {
                Logx.e(LazyTextureRegion.class, "load " + request.getUrl() + " failed status code not 200");
                Pools.free(request);
                pool.free(this);
            }
        }

        @Override
        public void failed(Throwable t) {
            Logx.e(LazyTextureRegion.class, "load " + request.getUrl() + " failed " + t.getMessage());
            Pools.free(request);
            pool.free(this);
        }

        @Override
        public void cancelled() {
            Pools.free(request);
            pool.free(this);
        }

        @Override
        public void reset() {
            rawImageBytes = null;
            placeHolder = null;
            request = null;
            if(pixmap != null && !pixmap.isDisposed()){
                pixmap.dispose();
            }
            pixmap = null;
        }

        @Override
        public void run() {
            Texture texture = new Texture(pixmap);
            MyAssetManager.get().addAsset(request.getUrl(), Texture.class, texture);
            if(cacheEnable) {
                checkIfFolderExistAndCreate();
                PixmapIO.writePNG(Gdx.files.local(basePath+getSafeUrl(request.getUrl())), pixmap);
            }
            placeHolder.setRegion(texture);
            Pools.free(request);
            pool.free(this);
        }
    }

    private void checkIfFolderExistAndCreate() {
        File folder = new File(basePath);
        if (!folder.exists()) {
            Logx.d(this.getClass(), "location not exist " + basePath);
            if (folder.mkdir()) {
                Logx.d(this.getClass(), "location mkdir " + basePath);
            }
        }
    }
}
