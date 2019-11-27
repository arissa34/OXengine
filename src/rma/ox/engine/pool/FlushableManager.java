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

    public FlushableManager(){
        listActionFlushable = new Array<>();
        listBDDFlushable = new Array<>();
        listUiFlushable = new Array<>();
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
}
