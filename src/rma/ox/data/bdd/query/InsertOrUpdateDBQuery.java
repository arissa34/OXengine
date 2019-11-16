package rma.ox.data.bdd.query;

import rma.ox.data.bdd.base.DBQuery;
import rma.ox.data.bdd.pool.InsertOrUpdatePool;
import rma.ox.engine.utils.Logx;

public class InsertOrUpdateDBQuery extends DBQuery {

    private Object[] obj;
    private DBQuery.Callback<Void> listener;
    private String pathDB;

    public InsertOrUpdateDBQuery setDB(String pathDB) {
        this.pathDB = pathDB;
        return this;
    }

    public InsertOrUpdateDBQuery setListener(DBQuery.Callback<Void> listener) {
        this.listener = listener;
        return this;
    }

    public InsertOrUpdateDBQuery init(Object... obj) {
        this.obj = obj;
        return this;
    }

    @Override
    protected void executeQuery() {
        if (pathDB == null) {
            Logx.e(getClass(), "pathDB is null");
            return;
        }
        if (obj == null) {
            Logx.e(this.getClass(), "obj is null");
            return;
        }
        try {
            //Logx.d(this.getClass(), "==+> executeQuery "+Thread.currentThread().getName());
            for (int i = 0; i < obj.length; i++) {
                getDB(pathDB).insertOrUpdate(obj[i]);
            }
            if (listener != null) {
                listener.succes(null);
            }
        } catch (Exception e) {
            if (listener != null) {
                listener.error(e.getMessage());
            }
        }
    }

    @Override
    protected void realsePoolable() {
        InsertOrUpdatePool.get().free(this);
    }

    @Override
    public void reset() {
        pathDB = null;
        obj = null;
        listener = null;
    }
}
