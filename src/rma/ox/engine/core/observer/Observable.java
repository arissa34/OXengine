package rma.ox.engine.core.observer;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;

public class Observable<K> implements Component {

    private Array<Observer> observers;
    protected K model;
    protected DataNotify dataNotify;

    public Observable(K model){
        this.model = model;
        observers = new Array<>();
        dataNotify = new DataNotify(this);
    }

    public Observable(){
        this(null);
    }

    public Observable<K> subscribe(Observer<K> observer){
        if(!observers.contains(observer, true)){
            observers.add(observer);
            observer.onSubscribe(this);
        }
        return this;
    }

    public void unsubscribe(Observer<K> observer){
        if(observers.removeValue(observer, true)){
            observer.onUnsubscribe(this);
        }
    }

    public void notifyObsv(ObservableState command){
        dataNotify.data = null;
        for(int i = 0; i < observers.size; i++){
            observers.get(i).onNotify(command, dataNotify);
        }
    }

    public void  notifyObsv(ObservableState command, Object obj){
        dataNotify.data = obj;
        //for(int i = 0; i < observers.size; i++){
        //    observers.get(i).onNotify(command, dataNotify);
        //}
    }

    public K getModel(){
        return model;
    }

    public void dispose(){
        //ObservManager.get().removeObservable(type, this);
        for(int i = 0; i < observers.size; i++){
            observers.get(i).onUnsubscribe(this);
        }
        observers.clear();
    }
}
