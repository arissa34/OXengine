package rma.ox.engine.core.observer;

public class DataNotify {
    public Observable sender;
    public Object data;

    public DataNotify(Observable sender) {
        this.sender = sender;
    }
}
