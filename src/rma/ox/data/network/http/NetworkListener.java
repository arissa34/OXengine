package rma.ox.data.network.http;

public interface NetworkListener<T> {
    void onSucced(T result);
    void onFail(ResponseError error);
}
