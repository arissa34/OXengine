package rma.ox.engine.pool;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FlushablePool;

public class FlushableManager {

    private static FlushableManager instance;

    public static FlushableManager get() {
        if (instance == null) instance = new FlushableManager();
        return instance;
    }

    /*******************************/

    public Array<FlushablePool> listActionFlushable;
    public Array<FlushablePool> listBDDFlushable;
    public Array<FlushablePool> listUiFlushable;
    public Array<FlushablePool> listRequestFlushable;
    public Array<FlushablePool> listEntityFlushable;

    public FlushableManager(){
        listActionFlushable = new Array<>();
        listBDDFlushable = new Array<>();
        listUiFlushable = new Array<>();
        listRequestFlushable = new Array<>();
        listEntityFlushable = new Array<>();
    }

    public void registerEntity(FlushablePool pool){
        if(!listEntityFlushable.contains(pool, true)) {
            listEntityFlushable.add(pool);
        }
    }

    public void registerUi(FlushablePool pool){
        if(!listUiFlushable.contains(pool, true)) {
            listUiFlushable.add(pool);
        }
    }

    public void flushUi(){
        for(int i =0; i < listUiFlushable.size; i++){
            listUiFlushable.get(i).flush();
        }
    }

    public void flushEntities(){
        for(int i =0; i < listEntityFlushable.size; i++){
            listEntityFlushable.get(i).flush();
        }
    }
}
