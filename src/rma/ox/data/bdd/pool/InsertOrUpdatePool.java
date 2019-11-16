package rma.ox.data.bdd.pool;

import com.badlogic.gdx.utils.Pool;

import rma.ox.data.bdd.query.InsertOrUpdateDBQuery;

public class InsertOrUpdatePool extends Pool<InsertOrUpdateDBQuery> {

    private static InsertOrUpdatePool instance;

    public static InsertOrUpdatePool get() {
        if (instance == null) instance = new InsertOrUpdatePool();
        return instance;
    }

    /*******************************/

    @Override
    protected InsertOrUpdateDBQuery newObject() {
        return new InsertOrUpdateDBQuery();
    }

    @Override
    public void free(InsertOrUpdateDBQuery object) {
        super.free(object);
    }
}
