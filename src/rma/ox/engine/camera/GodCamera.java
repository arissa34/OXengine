package rma.ox.engine.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntIntMap;

public class GodCamera extends GhostCamera implements GestureDetector.GestureListener, InputProcessor {

    private float degreesPerPixel = 0.2f;
    private float velocity = 50;
    private float factor = 1;
    private int buttonTouchDown;

    private final IntIntMap keys = new IntIntMap();
    private int STRAFE_LEFT = Input.Keys.A;
    private int STRAFE_RIGHT = Input.Keys.D;
    private int FORWARD = Input.Keys.W;
    private int BACKWARD = Input.Keys.S;
    private int UP = Input.Keys.Q;
    private int DOWN = Input.Keys.E;
    private int SHIFT = Input.Keys.SHIFT_LEFT;

    public GodCamera(float fieldOfView, float viewportWidth, float viewportHeight) {
        super(fieldOfView, viewportWidth, viewportHeight);

        targetDirection.set(0, 0, -1);
        targetUp.set(0, 1, 0);
        targetPosition.set(0, 0, 0);

        addInputAndGestureListener(this, this);
    }

    @Override
    public void update(float delta) {
        updateKey(delta);
        super.update(delta);
    }

    /**** InputProcessor ****/

    public void updateKey (float deltaTime) {
        if (keys.containsKey(SHIFT)) {
            factor = 4;
        }else{
            factor = 1;
        }
        if (keys.containsKey(FORWARD)) {
            tmp.set(targetDirection).nor().scl(deltaTime * velocity * factor);
            targetPosition.add(tmp);
        }
        if (keys.containsKey(BACKWARD)) {
            tmp.set(targetDirection).nor().scl(-deltaTime * velocity * factor);
            targetPosition.add(tmp);
        }
        if (keys.containsKey(STRAFE_LEFT)) {
            tmp.set(targetDirection).crs(targetUp).nor().scl(-deltaTime * velocity * factor);
            targetPosition.add(tmp);
        }
        if (keys.containsKey(STRAFE_RIGHT)) {
            tmp.set(targetDirection).crs(targetUp).nor().scl(deltaTime * velocity * factor);
            targetPosition.add(tmp);
        }
        if (keys.containsKey(UP)) {
            tmp.set(targetUp).nor().scl(deltaTime * velocity * factor);
            targetPosition.add(tmp);
        }
        if (keys.containsKey(DOWN)) {
            tmp.set(targetUp).nor().scl(-deltaTime * velocity * factor);
            targetPosition.add(tmp);
        }
    }


    @Override
    public boolean keyDown(int keycode) {
        keys.put(keycode, keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        keys.remove(keycode, 0);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        buttonTouchDown = button;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (buttonTouchDown == Input.Buttons.LEFT) {
            drag();
        }
        return false;
    }

    public Vector3 tmp2 = new Vector3();
    public Vector3 tmp3 = new Vector3();
    public void drag() {
        float deltaX = -Gdx.input.getDeltaX() * degreesPerPixel;
        float deltaY = -Gdx.input.getDeltaY() * degreesPerPixel;

        targetDirection.rotate(up, deltaX);

        Vector3 oldPitchAxis = tmp.set(targetDirection).crs(targetUp).nor();
        Vector3 newDirection = tmp2.set(targetDirection).rotate(tmp, deltaY);
        Vector3 newPitchAxis = tmp3.set(tmp2).crs(targetUp);
        if (!newPitchAxis.hasOppositeDirection(oldPitchAxis))
            targetDirection.set(newDirection);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /**** GESTURE ****/

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        drag();
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
