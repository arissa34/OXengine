package rma.ox.data.bdd;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.utils.FlushablePool;
import com.badlogic.gdx.utils.Pool;

import org.dizitart.no2.objects.ObjectFilter;

public class NoSqlRequest<T> implements Pool.Poolable {

    private static final FlushablePool<NoSqlRequest> noSqlPool = new FlushablePool<NoSqlRequest>() {
        @Override
        protected NoSqlRequest newObject() {
            return new NoSqlRequest();
        }
    };

    public static NoSqlRequest obtain(NoSqlDB db, Telegraph server){
        return noSqlPool.obtain().initRepo(db, server);
    }

    /******************************/

    private NoSqlDB db;
    private NoSqlCommand command;
    private T object;
    private Class<T> type;
    private ObjectFilter filter;
    private Telegraph server;

    private NoSqlRequest initRepo(NoSqlDB db, Telegraph server){
        this.db = db;
        this.server = server;
        return this;
    }

    public NoSqlRequest insert(T object){
        this.command = NoSqlCommand.INSERT;
        this.object = object;
        return this;
    }

    public NoSqlRequest insertOrUpdate(T object){
        this.command = NoSqlCommand.INSERT_OR_UPDATE;
        this.object = object;
        return this;
    }

    public NoSqlRequest update(T object){
        this.command = NoSqlCommand.UPDATE;
        this.object = object;
        return this;
    }

    public NoSqlRequest findAll(Class<T> type){
        this.command = NoSqlCommand.FIND_ALL;
        this.type = type;
        return this;
    }

    public NoSqlRequest findFirst(ObjectFilter filter, Class<T> type){
        this.command = NoSqlCommand.FIND_FIRST;
        this.type = type;
        this.filter = filter;
        return this;
    }

    public NoSqlRequest findByFilter(ObjectFilter filter, Class<T> type){
        this.command = NoSqlCommand.FIND_BY_FILTER;
        this.type = type;
        this.filter = filter;
        return this;
    }

    public NoSqlRequest removeById(ObjectFilter filter, Class<T> type){
        this.command = NoSqlCommand.REMOVE_BY_ID;
        this.type = type;
        this.filter = filter;
        return this;
    }

    public NoSqlRequest remove(T object){
        this.command = NoSqlCommand.REMOVE;
        this.object = object;
        return this;
    }

    protected boolean request(){
        if(command == null){
            MessageManager.getInstance().dispatchMessage(server, NoSqlCommand.ERROR.id, "NoSqlCommand request null");
        }else{
            T result = null;
            boolean isRequestSuccess = false;
            switch (command){
                case INSERT:
                    isRequestSuccess = NoSqlUtils.insert(db, object);
                    break;
                case INSERT_OR_UPDATE:
                    isRequestSuccess = NoSqlUtils.insertOrUpdate(db, object);
                    break;
                case UPDATE:
                    isRequestSuccess = NoSqlUtils.update(db, object);
                    break;
                case FIND_BY_ID:
                    //result = (T) NoSqlUtils.findById(db, id, filter, type);
                    break;
                case FIND_FIRST:
                    result = NoSqlUtils.findFirst(db, filter, type);
                    isRequestSuccess = (result != null && NoSqlUtils.getError() == null);
                    break;
                case FIND_ALL:
                    result = (T) NoSqlUtils.findAll(db, type);
                    isRequestSuccess = (result != null && NoSqlUtils.getError() == null);
                    break;
                case FIND_BY_FILTER:
                    result = (T) NoSqlUtils.findByFilter(db, filter, type);
                    isRequestSuccess = (result != null && NoSqlUtils.getError() == null);
                    break;
                case REMOVE_BY_ID:
                    isRequestSuccess = NoSqlUtils.removeById(db, filter, type);
                    break;
                case REMOVE:
                    isRequestSuccess = NoSqlUtils.remove(db, object);
                    break;
            }
            if(isRequestSuccess && server != null) {
                MessageManager.getInstance().dispatchMessage(server, command.id, result);
            }else if(server != null) {
                MessageManager.getInstance().dispatchMessage(server, NoSqlCommand.ERROR.id, NoSqlUtils.getError());
            }
        }
        db.commit();
        noSqlPool.free(this);
        return true;
    }

    @Override
    public void reset() {
        db = null;
        server = null;
        command = null;
        object = null;
        filter = null;
        type = null;
    }
}
