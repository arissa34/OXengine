package rma.ox.data.bdd.query;

import com.badlogic.gdx.utils.Array;

import rma.ox.data.bdd.base.DBQuery;
import rma.ox.data.bdd.pool.RemovePool;
import rma.ox.engine.utils.Logx;

import org.dizitart.no2.objects.ObjectFilter;
import org.dizitart.no2.objects.filters.ObjectFilters;

public class RemoveDBQuery extends DBQuery {

    private Object[] obj;
    private Class type;
    private ObjectFilter filter;
    private DBQuery.Callback<Void> listener;
    private String pathDB;

    public RemoveDBQuery setListener(DBQuery.Callback<Void> listener) {
        this.listener = listener;
        return this;
    }

    public RemoveDBQuery setDB(String pathDB) {
        this.pathDB = pathDB;
        return this;
    }

    public RemoveDBQuery by(Object... obj) {
        this.obj = obj;
        this.type = obj.length > 0 ? obj[0].getClass() : null;
        this.filter = null;
        return this;
    }

    public RemoveDBQuery byId(String id, Object value, Class<?> type) {
        byFilter(ObjectFilters.eq(id, value), type);
        return this;
    }

    public RemoveDBQuery byIds(String id, Array<Object> values, Class<?> type) {
        byFilter(ObjectFilters.in(id, values.items), type);
        return this;
    }

    public RemoveDBQuery byFilter(ObjectFilter filter, Class<?> type) {
        this.filter = filter;
        this.type = type;
        return this;
    }

    @Override
    protected void executeQuery() {
        if (pathDB == null) {
            Logx.e(getClass(), "pathDB is null");
            return;
        }
        //Logx.d(this.getClass(), "==+> executeQuery"+Thread.currentThread().getName());
        try {
            if (obj != null && type != null) {
                for (int i = 0; i < obj.length; i++) {
                    getDB(pathDB).remove(obj[i], type);
                }
            } else if (filter != null && type != null) {
                getDB(pathDB).remove(filter, type);
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
        RemovePool.get().free(this);
    }

    @Override
    public void reset() {
        pathDB = null;
        obj = null;
        filter = null;
        this.type = null;
    }
}
