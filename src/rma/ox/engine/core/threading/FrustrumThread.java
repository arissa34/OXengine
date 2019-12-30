package rma.ox.engine.core.threading;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import rma.ox.engine.update.UpdatableThread;

public class FrustrumThread implements Runnable, Disposable {

    private static FrustrumThread instance;

    public static FrustrumThread get() {
        if (instance == null) instance = new FrustrumThread();
        return instance;
    }

    /*******************************/

    private Thread thread;
    private Array<UpdatableThread> listUpdatable;

    public FrustrumThread() {
        listUpdatable = new Array<>();
        thread = new Thread(this);
        thread.setPriority(1);
        thread.start();
    }

    public synchronized void register(UpdatableThread updatable) {
        if (!listUpdatable.contains(updatable, true))
            listUpdatable.add(updatable);
    }

    public synchronized void remove(UpdatableThread updatable) {
        listUpdatable.removeValue(updatable, true);
    }

    @Override
    public void run() {
        while (true){
            for (int i = 0; i < listUpdatable.size; i++) {
                if(listUpdatable.get(i) != null) listUpdatable.get(i).updateOnThread();
            }
        }
    }

    @Override
    public void dispose() {
        thread.interrupt();
        instance = null;
    }
}
