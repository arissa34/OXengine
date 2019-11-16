package rma.ox.data.bdd.base;

public class DBQueryHandler {

    private static DBQueryHandler INSTANCE;
    private final DBQueryScheduler dbQueryScheduler;

    public DBQueryHandler(DBQueryScheduler dbQueryScheduler) {
        this.dbQueryScheduler = dbQueryScheduler;
    }

    public static DBQueryHandler get() {
        if (INSTANCE == null) {
            INSTANCE = new DBQueryHandler(new DBThreadPoolScheduler());
        }
        return INSTANCE;
    }

    /*******************************/

    public void execute( final DBQuery query) {
        dbQueryScheduler.execute(query);
    }
}
