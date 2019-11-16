package rma.ox.logic.base;

import com.badlogic.gdx.utils.Pool;

/**
 * Use cases are the entry points to the domain layer.
 */
public abstract class UseCase implements Runnable, Pool.Poolable{

    public interface Callback{
        void succes(UseCase useCase);
        void error(String msg);
    }

    protected Callback callback;

    @Override
    public void run() {
       executeUseCase();
    }

    protected abstract void executeUseCase();

}
