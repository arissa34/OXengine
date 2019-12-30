package rma.ox.engine.update;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.utils.Array;

public class UpdateManager {

    private static UpdateManager instance;

    public static UpdateManager get() {
        if (instance == null) instance = new UpdateManager();
        return instance;
    }

    /*******************************/


    private Array<UpdatableMainThread> listUpdatable;

    public UpdateManager() {
        listUpdatable = new Array<>();
    }

    public void register(UpdatableMainThread updatable) {
        if (!listUpdatable.contains(updatable, true))
            listUpdatable.add(updatable);
    }

    public void remove(UpdatableMainThread updatable) {
        listUpdatable.removeValue(updatable, true);
    }

    public void update(float delta) {
        for (int i = 0; i < listUpdatable.size; i++) {
            listUpdatable.get(i).updateOnMainThread(delta);
        }
    }
}
