package rma.ox.data.bdd.base;

import com.badlogic.gdx.utils.Pool;
import rma.ox.data.bdd.db.NoSqlDB;
import rma.ox.logic.base.UseCase;

public abstract class DBQuery implements Runnable, Pool.Poolable{

    public interface Callback<T>{
        void succes(T response);
        void error(String msg);
    }

    protected UseCase.Callback callback;
    private NoSqlDB dbHelper;

    public void setDB(NoSqlDB dbHelper){
        this.dbHelper = dbHelper;
    }

    public NoSqlDB getDB(){
        return dbHelper;
    }

    @Override
    public void run() {
        executeQuery();
        realsePoolable();
    }

    protected abstract void executeQuery();

    protected abstract void realsePoolable();
}
