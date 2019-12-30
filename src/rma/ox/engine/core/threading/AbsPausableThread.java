package rma.ox.engine.core.threading;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.LifecycleListener;

public abstract class AbsPausableThread implements Runnable, LifecycleListener {

    protected final Object threadLock;
    protected Thread thread;
    protected boolean suspended;
    protected boolean stopped;

    public AbsPausableThread() {
        threadLock = new Object();
        thread = new Thread(this);
        suspended = true;
        stopped = false;
        Gdx.app.addLifecycleListener(this);
    }

    public AbsPausableThread setDeamon(boolean isDeamon) {
        thread.setDaemon(isDeamon);
        return this;
    }

    public AbsPausableThread setPriority(int priority) {
        thread.setPriority(priority);
        return this;
    }

    public AbsPausableThread launchThread() {
        thread.start();
        return this;
    }

    public abstract void runThread();

    @Override
    public void run() {

        while (true) {

            synchronized (threadLock) {

                try {
                    while (suspended) {
                        threadLock.wait();
                    }
                    if (stopped)
                        return;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            runThread();
            pause();
        }
    }

    public void stop() {
        synchronized (threadLock) {
            stopped = true;
            suspended = false;
            threadLock.notifyAll();
        }
    }

    public void pause() {
        synchronized (threadLock) {
            suspended = true;
            threadLock.notifyAll();
        }
    }

    public void resume() {
        synchronized (threadLock) {
            if(!suspended) return;
            suspended = false;
            threadLock.notifyAll();
        }
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void dispose () {
        synchronized (threadLock) {
            thread = null;
            threadLock.notifyAll();
        }
        Gdx.app.removeLifecycleListener(this);
    }
}
