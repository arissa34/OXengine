package rma.ox.data.bdd;

import com.badlogic.gdx.utils.Array;

import org.dizitart.no2.exceptions.InvalidIdException;
import org.dizitart.no2.exceptions.NotIdentifiableException;
import org.dizitart.no2.exceptions.UniqueConstraintException;
import org.dizitart.no2.exceptions.ValidationException;
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.ObjectFilter;
import org.dizitart.no2.objects.ObjectRepository;
import org.dizitart.no2.objects.filters.ObjectFilters;

import java.util.Iterator;

import rma.ox.engine.utils.Logx;

public class NoSqlUtils {

    private static String error;

    public static String getError(){
        return error;
    }

    public static <T> boolean insert(NoSqlDB db, T object) /*throws UniqueConstraintException, InvalidIdException*/ {
        error = null;
        if(db == null){
            Logx.e(NoSqlRequest.class, error = "BDD is null");
            return false;
        }
        ObjectRepository<T> repo = (ObjectRepository<T>) db.getRepo(object.getClass());
        if (repo == null) {
            Logx.e(NoSqlRequest.class, error = "insert ObjectRepository is null");
            return false;
        }
        try {
            repo.insert(object);
            Logx.d(NoSqlRequest.class, "===> insert: " + object.getClass().getSimpleName());
            return true;
        } catch (UniqueConstraintException uce) {
            Logx.e(NoSqlRequest.class, error = "insert UniqueConstraintException " + uce.getMessage());
            return false;
        } catch (InvalidIdException ie) {
            Logx.e(NoSqlRequest.class, error = "insert InvalidIdException " + ie.getMessage());
            return false;
        }
    }

    public static <T> boolean insertOrUpdate(NoSqlDB db, T object) /*throws UniqueConstraintException, InvalidIdException*/ {
        error = null;
        if(db == null){
            Logx.e(NoSqlRequest.class, error = "BDD is null");
            return false;
        }
        ObjectRepository<T> repo = (ObjectRepository<T>) db.getRepo(object.getClass());
        if (repo == null) {
            Logx.e(NoSqlRequest.class, error = "insertOrUpdate ObjectRepository is null");
            return false;
        }
        try {
            repo.insert(object);
            return true;
        } catch (UniqueConstraintException uce) {
            return update(db, object);
        } catch (InvalidIdException ie) {
            Logx.e(NoSqlRequest.class, error = "insertOrUpdate InvalidIdException " + ie.getMessage());
            return false;
        }
    }

    public static <T> boolean update(NoSqlDB db, T object) {
        error = null;
        if(db == null){
            Logx.e(NoSqlRequest.class, error = "BDD is null");
            return false;
        }
        ObjectRepository<T> repo = (ObjectRepository<T>) db.getRepo(object.getClass());
        if (repo == null) {
            Logx.e(NoSqlRequest.class, error = "updateOnMainThread ObjectRepository is null");
            return false;
        }

        try {
            repo.update(object);
            return true;
        } catch (ValidationException v) {
            Logx.e(NoSqlRequest.class, error = "updateOnMainThread ValidationException " + v.getMessage());
            return false;
        } catch (NotIdentifiableException nie) {
            Logx.e(NoSqlRequest.class, error = "updateOnMainThread NotIdentifiableException " + nie.getMessage());
            return false;
        }
    }

    public static <T> T findById(NoSqlDB db, String id, Object value, Class<T> type) {
        return findFirst(db, ObjectFilters.eq(id, value), type);
    }

    public static <T> T findFirst(NoSqlDB db, ObjectFilter filter, Class<T> type) {
        error = null;
        if(db == null){
            Logx.e(NoSqlRequest.class, error = "BDD is null");
            return null;
        }
        return db.getRepo(type).find(filter).firstOrDefault();
    }

    public static <T> Array<T> findByFilter(NoSqlDB db, ObjectFilter filter, Class<T> type) {
        error = null;
        if(db == null){
            Logx.e(NoSqlRequest.class, error = "BDD is null");
            return null;
        }
        Cursor<T> c = db.getRepo(type).find(filter);
        Array<T> list = new Array<>();
        Iterator<T> iterator = c.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    public static <T> Array<T> findAll(NoSqlDB db, Class<T> type) {
        error = null;
        if(db == null){
            Logx.e(NoSqlRequest.class, error = "BDD is null");
            return null;
        }
        Cursor<T> c = db.getRepo(type).find();
        Array<T> list = new Array<>();
        Iterator<T> iterator = c.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    public static <T> boolean removeById(NoSqlDB db, ObjectFilter filter, Class<T> type) {
        error = null;
        if(db == null){
            Logx.e(NoSqlRequest.class, error = "BDD is null");
            return false;
        }
        ObjectRepository<T> repo = db.getRepo(type);
        if (repo == null) {
            Logx.e(NoSqlRequest.class, "removeById ObjectRepository is null");
            return false;
        }
        repo.remove(filter);
        return true;
    }

    public static <T> boolean remove(NoSqlDB db, T object) {
        error = null;
        if(db == null){
            Logx.e(NoSqlRequest.class, error = "BDD is null");
            return false;
        }
        ObjectRepository<T> repo = (ObjectRepository<T>) db.getRepo(object.getClass());
        if (repo == null) {
            Logx.e(NoSqlRequest.class, error = "remove ObjectRepository is null");
            return false;
        }
        try {
            repo.remove(object);
            return true;
        } catch (NotIdentifiableException nie) {
            Logx.e(NoSqlRequest.class, error = "remove NotIdentifiableException " + nie.getMessage());
            return false;
        }
    }
}
