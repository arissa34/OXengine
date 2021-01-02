package rma.ox.engine.camera.controler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import rma.ox.engine.camera.GhostCamera;

public class RTSController extends AbsController {

    public int screenX, screenY;
    private float degreesPerPixel = 0.4f;
    private int buttonTouchDown;
    public Vector3 front;

    public RTSController(GhostCamera camera) {
        super(camera);
        front = new Vector3();
    }

    @Override
    public void update(float delta) {
        front.set(tmp.set(Vector3.Y).crs(camera.right).nor());
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

    public void drag() {
        float deltaX = -Gdx.input.getDeltaX() * degreesPerPixel;
        float deltaY = -Gdx.input.getDeltaY() * degreesPerPixel;

        camera.update();
        dragX(deltaX);
        dragY(deltaY);
    }

    private float speed = 4;
    private float maxX = 2000;
    public void dragX(float deltaX){
        //if (getXpos() + deltaX > getMinX() && getXpos() + deltaX < getMaxX()) {
            //camera.targetDirection.mulAdd(directionToFollow, -(deltaX));
            camera.targetPosition.mulAdd(camera.right, -(deltaX * speed));

        if(camera.targetPosition.x > maxX){
            camera.targetPosition.x = maxX;
        }else if(camera.targetPosition.x < -maxX){
            camera.targetPosition.x = -maxX;
        }
        //}
    }

    private float maxZ = 2000;
    private float zOffset = 200;
    public void dragY(float deltaY){
        //if (getYpos() - deltaY > getMinY() && getYpos() - deltaY < getMaxY()) {
            //camera.targetDirection.mulAdd(camera.targetUp, (-deltaY));
            camera.targetPosition.mulAdd(front, (deltaY * speed));

        if(camera.targetPosition.z > maxZ+zOffset){
            camera.targetPosition.z = maxZ+zOffset;
        }else if(camera.targetPosition.z < -maxZ+zOffset){
            camera.targetPosition.z = -maxZ+zOffset;
        }
        //}
    }

    /**** GESTURE ****/

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        zoom(amountY * 3.5f);
        return false;
    }

    private float zoomMax = 900;
    private float zoomSpeed = 8;
    public synchronized void zoom(float zoomValue) {
        //Logx.d(this.getClass(), "followerAxis.getTranslation(tmp).z : "+followerAxis.getTranslation(tmp).z );
        //Logx.d(this.getClass(), "zoomValue : "+zoomValue);
        //Logx.d(this.getClass(), "------------------");
        camera.update();
        //if (getZpos() + zoomValue >= minZ && getZpos() + zoomValue <= maxZ) {
            tmp.set(camera.targetPosition).mulAdd(camera.direction, -zoomValue * zoomSpeed);
            camera.targetPosition.set(tmp);
        //}

        if(camera.targetPosition.y > zoomMax){
            camera.targetPosition.y = zoomMax;
        }
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

    float lastZoom;
    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        if(lastZoom == 0) lastZoom = pointer1.dst(pointer2);
        if(lastZoom > pointer1.dst(pointer2)) zoom(2);
        if(lastZoom < pointer1.dst(pointer2)) zoom(-2);
        lastZoom = pointer1.dst(pointer2);
        return false;
    }

    @Override
    public void pinchStop() {
        lastZoom = 0;
    }

}
