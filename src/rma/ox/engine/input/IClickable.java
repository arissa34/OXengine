package rma.ox.engine.input;

import com.badlogic.gdx.graphics.Camera;

public interface IClickable<T> {

    boolean isClicked(Camera camera, float screenX, float screenY);
    T getObject();
}
