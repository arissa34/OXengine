package rma.ox.engine.core.threading.runnable;

import com.badlogic.gdx.utils.FlushablePool;
import com.badlogic.gdx.utils.Pool;

public class RunnablePool extends FlushablePool<RunnablePool.PoolableRunnable> {

    private static RunnablePool instance;

    public static PoolableRunnable get(Executor executor) {
        if (instance == null) instance = new RunnablePool();
        return instance.obtain().setExecutor(executor);
    }

    /*******************************/

    @Override
    protected PoolableRunnable newObject() {
        return new PoolableRunnable(this);
    }

    @Override
    public PoolableRunnable obtain() {
        return super.obtain();
    }

    public interface Executor{
        void execute();
    }

    protected class PoolableRunnable implements Pool.Poolable, Runnable {

        private Executor executor;
        private RunnablePool pool;

        public PoolableRunnable(RunnablePool pool){
            this.pool = pool;
        }

        @Override
        public void run() {
            if(executor != null) {
                executor.execute();
            }
            pool.free(this);
        }

        public PoolableRunnable setExecutor(Executor executor) {
            this.executor = executor;
            return this;
        }

        @Override
        public void reset() {
            executor = null;
        }
    }

}
