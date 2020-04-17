package rma.ox.engine.camera.controler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntIntMap;

import rma.ox.engine.camera.GhostCamera;
import rma.ox.engine.utils.Logx;

public class GodController extends AbsController {

    private float velocity = 50;
    private float factor = 1;
    private int buttonTouchDown;

    private final IntIntMap keys = new IntIntMap();
    private int STRAFE_LEFT = Input.Keys.A;
    private int STRAFE_RIGHT = Input.Keys.D;
    private int FORWARD = Input.Keys.W;
    private int BACKWARD = Input.Keys.S;
    private int UP = Input.Keys.E;
    private int DOWN = Input.Keys.Q;
    private int SHIFT = Input.Keys.SHIFT_LEFT;

    public GodController(GhostCamera camera) {
        super(camera);
    }

    @Override
    public void update(float delta) {
        updateKey(delta);
    }

    /**** InputProcessor ****/

    public void updateKey (float deltaTime) {
        if (keys.containsKey(SHIFT)) {
            factor = 4;
        }else{
            factor = 1;
        }
        if (keys.containsKey(FORWARD)) {
            tmp.set(camera.targetDirection).nor().scl(deltaTime * velocity * factor);
            camera.targetPosition.add(tmp);
        }
        if (keys.containsKey(BACKWARD)) {
            tmp.set(camera.targetDirection).nor().scl(-deltaTime * velocity * factor);
            camera.targetPosition.add(tmp);
        }
        if (keys.containsKey(STRAFE_LEFT)) {
            tmp.set(camera.targetDirection).crs(camera.targetUp).nor().scl(-deltaTime * velocity * factor);
            camera.targetPosition.add(tmp);
        }
        if (keys.containsKey(STRAFE_RIGHT)) {
            tmp.set(camera.targetDirection).crs(camera.targetUp).nor().scl(deltaTime * velocity * factor);
            camera.targetPosition.add(tmp);
        }
        if (keys.containsKey(UP)) {
            tmp.set(camera.targetUp).nor().scl(deltaTime * velocity * factor);
            camera.targetPosition.add(tmp);
        }
        if (keys.containsKey(DOWN)) {
            tmp.set(camera.targetUp).nor().scl(-deltaTime * velocity * factor);
            camera.targetPosition.add(tmp);
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

    private float startX, startY;
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        buttonTouchDown = button;
        startX = screenX;
        startY = screenY;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (buttonTouchDown == Input.Buttons.LEFT) {
            drag(screenX, screenY);
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (buttonTouchDown == Input.Buttons.LEFT) {
            drag(screenX, screenY);
        }
        return false;
    }

    public void drag(float screenX, float screenY) {
        float deltaX = (screenX - startX) / Gdx.graphics.getWidth();
        float deltaY = (startY - screenY) / Gdx.graphics.getHeight();
        startX = screenX;
        startY = screenY;
        if(camera.targetUp.y < 0){
            deltaX = -deltaX;
            //deltaY = -deltaY;
        }
        tmp.set(camera.targetDirection).crs(camera.targetUp).nor();
        rotateAround(camera.targetPosition, tmp.nor(), deltaY * 360);
        rotateAround(camera.targetPosition, Vector3.Y, deltaX * -360);
    }

    public void rotateAround (Vector3 point, Vector3 axis, float angle) {
        tmp2.set(point);
        tmp2.sub(camera.targetPosition);
        translateTarget(tmp2);
        rotateTarget(axis, angle);
        tmp2.rotate(axis, angle);
        camera.translate(-tmp2.x, -tmp2.y, -tmp2.z);
    }
    public void translateTarget (Vector3 vec) {
        camera.targetPosition.add(vec);
    }
    public void rotateTarget (Vector3 axis, float angle) {
        camera.targetDirection.rotate(axis, angle);
        camera.targetUp.rotate(axis, angle);
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
        startX = x;
        startY = y;
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
        drag(x, y);
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
