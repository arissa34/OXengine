package rma.ox.engine.core.observer;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

public class ObservManager {

    private static ObservManager instance;

    public static ObservManager get() {
        if (instance == null) instance = new ObservManager();
        return instance;
    }

    /*******************************/

    private ArrayMap<Class<? extends Observable>, Array<Class<? extends Observer>>> contracts;
    private Array<Observable> listObservable;
    private Array<Observer> listObserver;

    public ObservManager() {
        contracts = new ArrayMap<>();
        listObservable = new Array<>();
        listObserver = new Array<>();
    }

    public void contract(Class<? extends Observable> observable, Class<? extends Observer>... observersAutorized){
        if(!contracts.containsKey(observable)){
            contracts.put(observable, Array.with(observersAutorized));
        }
    }

    public void registerObservable(Observable observable){
        listObservable.add(observable);
        if(contracts.containsKey(observable.getClass())){
            Array<Class<? extends Observer>> observerTypes = contracts.get(observable.getClass());
            for(int i =0; i < observerTypes.size; i++){
                for(int j =0; j < listObserver.size; j++){
                    if(listObserver.get(j).getClass() == (observerTypes.get(i))){
                        observable.subscribe(listObserver.get(j));
                    }
                }
            }
        }
    }

    public void registerObserver(Observer observer){
        listObserver.add(observer);
        for(int i =0; i < contracts.size; i++){
            for(int j =0; j < contracts.getValueAt(i).size; j++){
                if(observer.getClass() == (contracts.getValueAt(i).get(j))){
                    for(int k =0; k < listObservable.size; k++){
                        if(contracts.getKeyAt(i) == listObservable.get(k).getClass()){
                            listObservable.get(k).subscribe(observer);
                        }
                    }
                }
            }
        }
    }

}
