package rma.ox.engine.core.threading;

public abstract class AbsPausableThread implements Runnable {

    protected Thread thread;
    protected boolean suspended;
    protected boolean stopped;

    public AbsPausableThread() {
        thread = new Thread(this);
        suspended = true;
        stopped = false;
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

            synchronized (this) {

                try {
                    while (suspended) {
                        wait();
                    }
                    if (stopped)
                        return;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            runThread();
        }
    }

    public synchronized void stop() {
        stopped = true;
        suspended = false;
        notifyAll();
    }

    public synchronized void suspend() {
        suspended = true;
    }

    public synchronized void resume() {
        suspended = false;
        notifyAll();
    }

    public boolean isSuspended() {
        return suspended;
    }
}
