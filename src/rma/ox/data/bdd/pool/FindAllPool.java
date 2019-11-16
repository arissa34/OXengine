package rma.ox.data.bdd.pool;

import com.badlogic.gdx.utils.Pool;

import rma.ox.data.bdd.query.FindListDBQuery;

public class FindAllPool extends Pool<FindListDBQuery> {

    private static FindAllPool instance;

    public static FindAllPool get() {
        if (instance == null) instance = new FindAllPool();
        return instance;
    }

    /*******************************/

    @Override
    protected FindListDBQuery newObject() {
        return new FindListDBQuery();
    }

    @Override
    public void free(FindListDBQuery object) {
        super.free(object);
    }
}
