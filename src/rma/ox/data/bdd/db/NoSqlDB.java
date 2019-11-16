package rma.ox.data.bdd.db;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.NitriteBuilder;
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


public class NoSqlDB {

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
    }

    private void initRepositories() {
        for (int i = 0; i < repositoriesAllowed.size; i++) {
            Class type = repositoriesAllowed.get(i);
            repositories.put(type, db.getRepository(type));
            Logx.d(this.getClass(), "===> create repo : " + type);
        }
    }

    private <T> ObjectRepository<T> getRepo(Class<T> type) {
        if (type == null) {
            Logx.e(this.getClass(), "getRepo type is null");
            return null;
        }
        return repositories.get(type);
    }

    public <T> boolean insert(T object) /*throws UniqueConstraintException, InvalidIdException*/ {
        ObjectRepository<T> repo = (ObjectRepository<T>) getRepo(object.getClass());
        if (repo == null) {
            Logx.e(this.getClass(), "insert ObjectRepository is null");
            return false;
        }
        try {
            repo.insert(object);
            Logx.d(this.getClass(), "===> insert: " + object.getClass().getSimpleName());
            return true;
        } catch (UniqueConstraintException uce) {
            Logx.e(this.getClass(), "insert UniqueConstraintException " + uce.getMessage());
            return false;
        } catch (InvalidIdException ie) {
            Logx.e(this.getClass(), "insert InvalidIdException " + ie.getMessage());
            return false;
        }
    }

    public <T> boolean insertOrUpdate(T object) /*throws UniqueConstraintException, InvalidIdException*/ {
        ObjectRepository<T> repo = (ObjectRepository<T>) getRepo(object.getClass());
        if (repo == null) {
            Logx.e(this.getClass(), "insertOrUpdate ObjectRepository is null");
            return false;
        }
        try {
            repo.insert(object);
            //Logx.d(this.getClass(), "===> insertOrUpdate: "+ object.getClass().getSimpleName());
            return true;
        } catch (UniqueConstraintException uce) {
            //Logx.e(this.getClass(), "insertOrUpdate UniqueConstraintException " + uce.getMessage());
            return update(object);
        } catch (InvalidIdException ie) {
            Logx.e(this.getClass(), "insertOrUpdate InvalidIdException " + ie.getMessage());
            return false;
        }
    }

    public <T> boolean update(T object) {
        ObjectRepository<T> repo = (ObjectRepository<T>) getRepo(object.getClass());
        if (repo == null) {
            Logx.e(this.getClass(), "update ObjectRepository is null");
            return false;
        }

        try {
            repo.update(object);
            //Logx.d(this.getClass(), "===> update: "+ object.getClass().getSimpleName());
            return true;
        } catch (ValidationException v) {
            Logx.e(this.getClass(), "update ValidationException " + v.getMessage());
            return false;
        } catch (NotIdentifiableException nie) {
            Logx.e(this.getClass(), "update NotIdentifiableException " + nie.getMessage());
            return false;
        }
    }

    public <T> T findById(String id, Object value, Class<T> type) {
        return findFirst(ObjectFilters.eq(id, value), type);
    }

    public <T> T findFirst(ObjectFilter filter, Class<T> type) {
        return getRepo(type).find(filter).firstOrDefault();
    }

    public <T> Array<T> findByFilter(ObjectFilter filter, Class<T> type) {
        Cursor<T> c = getRepo(type).find(filter);
        Array<T> list = new Array<>();
        Iterator<T> iterator = c.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    public <T> Array<T> findAll(Class<T> type) {
        Cursor<T> c = getRepo(type).find();
        Array<T> list = new Array<>();
        Iterator<T> iterator = c.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    public <T> boolean removeById(ObjectFilter filter, Class<T> type) {
        ObjectRepository<T> repo = getRepo(type);
        if (repo == null) {
            Logx.e(this.getClass(), "removeById ObjectRepository is null");
            return false;
        }
        repo.remove(filter);
        return true;
    }

    public <T> boolean remove(T value, Class<T> type) {
        ObjectRepository<T> repo = getRepo(type);
        if (repo == null) {
            Logx.e(this.getClass(), "remove ObjectRepository is null");
            return false;
        }
        try {
            repo.remove(value);
            return true;
        } catch (NotIdentifiableException nie) {
            Logx.e(this.getClass(), "remove NotIdentifiableException " + nie.getMessage());
            return false;
        }
    }

}
