package rma.ox.data.bdd.base;

import com.badlogic.gdx.utils.Pool;
import rma.ox.data.bdd.db.NoSqlDB;
import rma.ox.engine.ressource.MyAssetManager;
import rma.ox.logic.base.UseCase;

public abstract class DBQuery implements Runnable, Pool.Poolable{

    public interface Callback<T>{
        void succes(T response);
        void error(String msg);
    }

    protected UseCase.Callback callback;

    public NoSqlDB getDB(String path){
        return MyAssetManager.get().get(path, NoSqlDB.class);
    }

    @Override
    public void run() {
        executeQuery();
        realsePoolable();
    }

    protected abstract void executeQuery();

    protected abstract void realsePoolable();
}
