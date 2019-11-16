package rma.ox.data.bdd.pool;

import com.badlogic.gdx.utils.Pool;

import rma.ox.data.bdd.query.RemoveDBQuery;

public class RemovePool extends Pool<RemoveDBQuery> {

    private static RemovePool instance;

    public static RemovePool get() {
        if (instance == null) instance = new RemovePool();
        return instance;
    }

    /*******************************/

    @Override
    protected RemoveDBQuery newObject() {
        return new RemoveDBQuery();
    }

    @Override
    public void free(RemoveDBQuery object) {
        super.free(object);
    }
}
