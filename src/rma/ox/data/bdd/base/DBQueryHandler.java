package rma.ox.data.bdd.base;

import rma.ox.data.bdd.db.NoSqlDB;
import rma.ox.engine.ressource.MyAssetManager;

public class DBQueryHandler {

    private static DBQueryHandler INSTANCE;
    private final DBQueryScheduler dbQueryScheduler;
    public final NoSqlDB noSqlDB;

    public DBQueryHandler(DBQueryScheduler dbQueryScheduler) {
        this.dbQueryScheduler = dbQueryScheduler;
        noSqlDB = MyAssetManager.get().get("");
    }

    public static DBQueryHandler get() {
        if (INSTANCE == null) {
            INSTANCE = new DBQueryHandler(new DBThreadPoolScheduler());
        }
        return INSTANCE;
    }

    /*******************************/

    public void execute( final DBQuery query) {
        query.setDB(noSqlDB);
        dbQueryScheduler.execute(query);
    }
}
