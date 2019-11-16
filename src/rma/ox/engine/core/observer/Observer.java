package rma.ox.engine.core.observer;


public interface Observer<K> {

    void onSubscribe(Observable<K> observable);
    void onNotify(ObservableState command, DataNotify dataNotify);
    void onUnsubscribe(Observable<K> observable);

}
