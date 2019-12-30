package rma.ox.engine.core.threading;

import com.badlogic.gdx.ai.utils.CircularBuffer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import rma.ox.engine.update.UpdatableThread;
import rma.ox.engine.utils.Logx;

public class RunnableThread extends AbsPausableThread {

    private static RunnableThread instance;

    public static RunnableThread get() {
        if (instance == null) instance = new RunnableThread();
        return instance;
    }

    /*******************************/

    private CircularBuffer<Runnable> queue;
    private Runnable currentRunnable;

    public RunnableThread() {
        queue = new CircularBuffer<>(10);
        setPriority(1).launchThread();
    }

    public void add(Runnable request) {
        queue.store(request);
        resume();
    }

    @Override
    public void runThread() {
        if (currentRunnable == null) currentRunnable = queue.read();

        while (currentRunnable != null) {
            currentRunnable.run();
            currentRunnable = queue.read();
        }
    }
}
