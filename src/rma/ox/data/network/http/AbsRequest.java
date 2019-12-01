package rma.ox.data.network.http;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pools;

import rma.ox.engine.core.threading.runnable.RunnablePool;
import rma.ox.engine.utils.Logx;

import java.util.Iterator;
import java.util.Map;

public abstract class AbsRequest<T> implements Net.HttpResponseListener {

    private Net.HttpRequest currentHttpRequest;
    private static HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
    private boolean isCancelled = false;

    protected abstract String getUrl();

    protected abstract String getMethod();

    protected abstract ArrayMap<String, String> getHeaders();

    protected abstract Map<String, String> getPostParams();

    protected abstract Class<T> getTypeToken();

    protected abstract boolean mustSave();

    protected abstract boolean isFileToDownload();

    protected abstract void saveResult(T result) throws Exception;

    protected abstract void postSuccesEvent(T result);

    protected abstract void postFailEvent(ResponseError error);

    @Override
    public void handleHttpResponse(Net.HttpResponse httpResponse) {
        if (isCancelled) return;

        int statusCode = httpResponse.getStatus().getStatusCode();

        Logx.d(this.getClass(), "httpResponse statusCode : " + statusCode);

        if (statusCode == 200 || statusCode == 201) {
            if (isFileToDownload()) {
                onSuccessRequestTexture(httpResponse.getResult());
            } else {
                String content = httpResponse.getResultAsString();
                onSuccessRequest(content);
            }
        } else if (statusCode == 204) {
            onSuccessRequest(null);
        } else {
            postFailEvent(new ResponseError(httpResponse));
        }

        Pools.free(currentHttpRequest);
        currentHttpRequest = null;
    }

    private void onSuccessRequestTexture(byte[] rawImageBytes) {
        if (mustSave()) {
        } else {
            Gdx.app.postRunnable(
                    RunnablePool.get(new RunnablePool.Executor() {
                        @Override
                        public void execute() {
                            Pixmap pixmap = new Pixmap(rawImageBytes, 0, rawImageBytes.length);
                            Texture texture = new Texture(pixmap);
                            postSuccesEvent((T) texture);
                        }
                    })
            );
        }
    }

    private void onSuccessRequest(String content) {
        if (mustSave()) {
            launchThreadSave(content);
        } else {
            postSuccesEvent(extractJsonResponse(content));
        }
    }

    private T extractJsonResponse(String content) {
        Json json = new Json();
        T result = json.fromJson(getTypeToken(), content);
        return result;
    }

    private void launchThreadSave(final String content) {
        //TODO REMANAGE REMOVE THE NEW TO AVOID GCC
        new Thread(new Runnable() {
            @Override
            public void run() {
                final T result = extractJsonResponse(content);
                try {
                    saveResult(result);
                } catch (Exception e) {
                    Logx.d(this.getClass(), "==> save http response error !!");
                    e.printStackTrace();
                }
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        postSuccesEvent(result); // NEED TO BE IN THE MAIN THREAD
                    }
                });
            }
        }).start();
    }

    @Override
    public void failed(Throwable t) {
        postFailEvent(new ResponseError(t));
        Pools.free(currentHttpRequest);
        currentHttpRequest = null;
    }

    @Override
    public void cancelled() {
        isCancelled = true;
        Pools.free(currentHttpRequest);
        currentHttpRequest = null;
    }

    public void launch() {
        requestBuilder.newRequest()
                .method(getMethod())
                .url(getUrl());

        if (getHeaders() != null) {
            Iterator<ObjectMap.Entry<String, String>> iterable = getHeaders().iterator();
            while (iterable.hasNext()) {
                ObjectMap.Entry<String, String> entry = iterable.next();
                requestBuilder.header(entry.key, entry.value);
            }
        }

        if (getPostParams() != null) {
            requestBuilder.formEncodedContent(getPostParams());
        }

        Net.HttpRequest httpRequest = requestBuilder.build();

        Logx.d(this.getClass(), "+++++++++ httpRequest +++++++++");
        Logx.d(this.getClass(), "httpRequest getUrl : " + httpRequest.getUrl());
        Logx.d(this.getClass(), "httpRequest getMethod : " + httpRequest.getMethod());
        Logx.d(this.getClass(), "httpRequest getHeaders : " + httpRequest.getHeaders());
        Logx.d(this.getClass(), "httpRequest getContent : " + httpRequest.getContent());
        Logx.d(this.getClass(), "+++++++++++++++++++++++++++++++");

        Gdx.net.sendHttpRequest(httpRequest, this);
        currentHttpRequest = httpRequest;
        isCancelled = false;
    }

    public void cancel() {
        if (currentHttpRequest != null) {
            Gdx.net.cancelHttpRequest(currentHttpRequest);
        }
    }
}
