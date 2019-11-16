package rma.ox.data.bdd.query;

import com.badlogic.gdx.utils.Array;
import rma.ox.data.bdd.base.DBQuery;
import rma.ox.data.bdd.pool.FindAllPool;
import rma.ox.engine.utils.Logx;

import org.dizitart.no2.objects.ObjectFilter;
import org.dizitart.no2.objects.filters.ObjectFilters;

public class FindListDBQuery<T> extends DBQuery {

    private Class<?> type;
    private ObjectFilter filter;
    private DBQuery.Callback<T> listener;

    public FindListDBQuery setListener(DBQuery.Callback<T> listener) {
        this.listener = listener;
        return this;
    }

    public FindListDBQuery byIds(String id, Array<Object> values, Class<?> type) {
        byFilter(ObjectFilters.in(id, values.items), type);
        return this;
    }

    public FindListDBQuery byFilter(ObjectFilter filter, Class<?> type) {
        this.filter = filter;
        this.type = type;
        return this;
    }

    public FindListDBQuery all(Class<?> type) {
        this.type = type;
        this.filter = ObjectFilters.ALL;
        return this;
    }

    @Override
    protected void executeQuery() {
        if (type == null) {
            Logx.e(this.getClass(), "type is null");
            return;
        }
        //Logx.d(this.getClass(), "==+> executeQuery : "+Thread.currentThread().getName());
        try {
            T response;
            if (filter == ObjectFilters.ALL) {
                response = (T) getDB().findAll(type);

            } else {
                response = (T) getDB().findByFilter(filter, type);
            }
            if (listener != null) {
                listener.succes(response);
            }
        } catch (Exception e) {
            if (listener != null) {
                listener.error(e.getMessage());
            }
        }
    }

    @Override
    protected void realsePoolable() {
        FindAllPool.get().free(this);
    }

    @Override
    public void reset() {
        type = null;
        listener = null;
        filter = null;
    }
}
