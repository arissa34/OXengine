package rma.ox.engine.camera.controler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import rma.ox.engine.camera.GhostCamera;

public class PivotController extends AbsController {

    private float degreesPerPixel = 0.2f;
    private int buttonTouchDown;

    private Vector3 targetRotation;

    public PivotController(GhostCamera camera) {
        super(camera);
    }

    public void setTargetRotation(Vector3 targetRotation){
        this.targetRotation = targetRotation;
        camera.moveToLooktAt(tmp.set(targetRotation).sub(camera.targetPosition).nor());
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
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

    private final Quaternion deltaRotation = new Quaternion();

    public Vector3 tmp2 = new Vector3();
    public void drag() {
        float deltaX = -Gdx.input.getDeltaX() * degreesPerPixel;
        float deltaY = -Gdx.input.getDeltaY() * degreesPerPixel;
        if(camera.targetUp.y < 0){
            deltaX = -deltaX;
        }
        tmp.set(camera.targetDirection).crs(camera.targetUp).nor();
        deltaRotation.setEulerAngles(deltaX, deltaY * tmp.x, deltaY * tmp.z);
        rotateAround(targetRotation, deltaRotation);
    }

    public void rotateAround(Vector3 point, Quaternion quat) {
        tmp.set(point).sub(camera.targetPosition);
        camera.targetPosition.add(tmp);
        quat.transform(camera.targetDirection);
        quat.transform(camera.targetUp);
        quat.transform(tmp);
        camera.targetPosition.add(-tmp.x, -tmp.y, -tmp.z);
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
        zoom(amount * 8f);
        return false;
    }

    private void zoom(float value){
        tmp.set(camera.targetDirection).nor().scl(-value);
        camera.targetPosition.add(tmp);
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
