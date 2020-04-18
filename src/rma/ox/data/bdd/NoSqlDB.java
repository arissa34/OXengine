package rma.ox.data.bdd;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.NitriteBuilder;
import org.dizitart.no2.objects.ObjectFilter;
import org.dizitart.no2.objects.ObjectRepository;

import rma.ox.engine.utils.Logx;


public class NoSqlDB implements LifecycleListener {

    static NoSqlThread noSqlThread = new NoSqlThread();

    private final Array<Class<?>> repositoriesAllowed;

    private final Nitrite db;
    private final ArrayMap<Class<?>, ObjectRepository> repositories;

    public NoSqlDB(String dbPath, Array<Class<?>> repositoriesAllowed) {
        this(dbPath, true, true, repositoriesAllowed);
    }

    public NoSqlDB(String dbPath, boolean isWritable, boolean compressed, Array<Class<?>> repositoriesAllowed) {

        NitriteBuilder builder = Nitrite.builder();
        builder.filePath(dbPath);

        if(!isWritable) builder.readOnly();
        if(compressed) builder.disableAutoCompact();

        db = builder.openOrCreate();

        if(repositoriesAllowed != null) {
            this.repositoriesAllowed = repositoriesAllowed;
        }else{
            this.repositoriesAllowed = new Array<>();
        }

        repositories = new ArrayMap<>();
        initRepositories();

        Gdx.app.addLifecycleListener(this);
    }

    private void initRepositories() {
        for (int i = 0; i < repositoriesAllowed.size; i++) {
            Class type = repositoriesAllowed.get(i);
            repositories.put(type, db.getRepository(type));
            Logx.d(this.getClass(), "===> create repo : " + type);
        }
    }

    public  <T> ObjectRepository<T> getRepo(Class<T> type) {
        if (type == null) {
            Logx.e(this.getClass(), "getRepo type is null");
            return null;
        }
        return repositories.get(type);
    }

    public <T> void insert(Telegraph server, T object){
        noSqlThread.addRequest(
                NoSqlRequest.obtain(this, server)
                .insert(object)
        );
    }

    public <T> void insertOrUpdate(Telegraph server, T object){
        noSqlThread.addRequest(
                NoSqlRequest.obtain(this, server)
                .insertOrUpdate(object)
        );
    }

    public <T> void findAll(Telegraph server, Class<T> type){
        noSqlThread.addRequest(
                NoSqlRequest.obtain(this, server)
                .findAll(type)
        );
    }

    public <T> void findFirst(Telegraph server, ObjectFilter filter, Class<T> type){
        noSqlThread.addRequest(
                NoSqlRequest.obtain(this, server)
                .findFirst(filter, type)
        );
    }

    public <T> void findByFilter(Telegraph server, ObjectFilter filter, Class<T> type){
        noSqlThread.addRequest(
                NoSqlRequest.obtain(this, server)
                .findByFilter(filter, type)
        );
    }

    public <T> void removeById(Telegraph server, ObjectFilter filter, Class<T> type){
        noSqlThread.addRequest(
                NoSqlRequest.obtain(this, server)
                .removeById(filter, type)
        );
    }

    public <T> void remove(Telegraph server, T object){
        noSqlThread.addRequest(
                NoSqlRequest.obtain(this, server)
                .remove(object)
        );
    }

    public void commit(){
        db.commit();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        if(db != null && !db.isClosed()){
            db.close();
        }
    }
}
