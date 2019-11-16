package rma.ox.data.network.http;

import com.badlogic.gdx.Net;

public class ResponseError extends Throwable {

    public final Net.HttpResponse networkResponse;

    public ResponseError() {
        this.networkResponse = null;
    }

    public ResponseError(Net.HttpResponse networkResponse) {
        this.networkResponse = networkResponse;
    }

    public ResponseError(String s) {
        super(s);
        this.networkResponse = null;
    }

    public ResponseError(String s, Throwable throwable) {
        super(s, throwable);
        this.networkResponse = null;
    }

    public ResponseError(Throwable throwable) {
        super(throwable);
        this.networkResponse = null;
    }

}
