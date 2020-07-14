package rma.ox.engine.core.observer.simple;

import com.badlogic.gdx.utils.Array;

public class Observable{

    public Array<Observer> observers = new Array<>();

    public Observable subscribe(Observer observer){
        if(!observers.contains(observer, true)){
            observers.add(observer);
            observer.onSubscribe(this);
        }
        return this;
    }

    public void notifyObservers(String event, Object... infos){
        for(int i = 0; i < observers.size; i++){
            observers.get(i).onNotification(event, infos);
        }
    }

    public void unsubscribe(Observer observer){
        if(observers.removeValue(observer, true)){
            observer.onUnsubscribe(this);
        }
    }
}