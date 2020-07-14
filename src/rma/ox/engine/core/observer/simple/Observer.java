package rma.ox.engine.core.observer.simple;


public interface Observer{
    void onSubscribe(Observable observable);
    void onNotification(String event, Object... infos);
    void onUnsubscribe(Observable observable);
}