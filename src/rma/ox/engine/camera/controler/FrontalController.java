package rma.ox.engine.camera.controler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import rma.ox.engine.camera.GhostCamera;

public class FrontalController extends AbsController {

    public int screenX, screenY;
    private float minX = -100f;
    private float maxX = 100f;
    private float minY = -5f;
    private float maxY = 50f;
    private float minZ = 10f;
    private float maxZ = 150f;
    private float degreesPerPixel = 0.4f;
    private Matrix4 transformToFollow = new Matrix4();
    public Matrix4 followerAxis = new Matrix4();
    public Vector3 directionToFollow = new Vector3();
    public Vector3 rightToFollow = new Vector3();
    public Vector3 leftToFollow = new Vector3();
    public Vector3 rightCam = new Vector3();
    private int buttonTouchDown;

    public FrontalController(GhostCamera camera) {
        super(camera);
    }

    @Override
    public void update(float delta) {

        updateTransformToFollow();
        //camera.update(delta);
    }

    public void setTransformToFollow(Matrix4 transformToFollow) {
        this.transformToFollow = transformToFollow;
    }

    public void updateTransformToFollow() {
        camera.targetUp.set(Vector3.Y).rot(transformToFollow).nor();
        directionToFollow.set(Vector3.X).rot(transformToFollow).nor();
        rightToFollow.set(camera.targetUp).crs(directionToFollow).nor();
        leftToFollow.set(directionToFollow).crs(camera.targetUp).nor();
        followerAxis.set(camera.view).mul(transformToFollow);
        rightCam.set(tmp.set(camera.targetUp).crs(camera.targetDirection).nor());
    }

    public void initPosition() {
        updateTransformToFollow();
        transformToFollow.getTranslation(camera.targetPosition);
        camera.targetPosition.mulAdd(rightToFollow, -60);
        camera.targetDirection.set(tmp.set(camera.targetUp).crs(directionToFollow).nor());
    }

    public void setBoundMovement(float minX, float maxX, float minY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
    }

    public void setBoundMovement(float minX, float maxX, float minY, float maxY) {
        this.setBoundMovement(minX, maxX, minY);
        this.maxY = maxY;
    }

    public void setBoundZ(float minZ, float maxZ) {
        this.minZ = minZ;
        this.maxZ = maxZ;
    }

    public float getMinX() {
        return minX;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMinY() {
        return minY;
    }

    public float getMaxY() {
        return maxY;
    }

    public float getMinZ() {
        return minZ;
    }

    public float getMaxZ() {
        return maxZ;
    }

    public float getXpos() {
        followerAxis.set(camera.targetView).mul(transformToFollow);
        return -followerAxis.getTranslation(tmp).x;
    }

    public float getYpos() {
        followerAxis.set(camera.targetView).mul(transformToFollow);
        return -followerAxis.getTranslation(tmp).y;
    }

    public float getZpos() {
        followerAxis.set(camera.targetView).mul(transformToFollow);
        return -followerAxis.getTranslation(tmp).z;
    }

    public float getZpos(Matrix4 mat) {
        followerAxis.set(mat).mul(transformToFollow);
        return followerAxis.getTranslation(tmp).z;
    }

    public void setZpos(float z) {
        float offs = z - getZpos();
        tmp.set(camera.targetPosition).mulAdd(leftToFollow, offs);
        camera.targetPosition.set(tmp);
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

    public void dragX(float deltaX){
        //if (getXpos() + deltaX > getMinX() && getXpos() + deltaX < getMaxX()) {
            //camera.targetDirection.mulAdd(directionToFollow, -(deltaX));
            camera.targetPosition.mulAdd(directionToFollow, (deltaX));
        //}
    }

    public void dragY(float deltaY){
        //if (getYpos() - deltaY > getMinY() && getYpos() - deltaY < getMaxY()) {
            //camera.targetDirection.mulAdd(camera.targetUp, (-deltaY));
            camera.targetPosition.mulAdd(camera.targetUp, (-deltaY));
        //}
    }

    /**** GESTURE ****/

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        zoom(amount * 3.5f);
        return false;
    }

    public synchronized void zoom(float zoomValue) {
        //Logx.d(this.getClass(), "followerAxis.getTranslation(tmp).z : "+followerAxis.getTranslation(tmp).z );
        //Logx.d(this.getClass(), "zoomValue : "+zoomValue);
        //Logx.d(this.getClass(), "------------------");
        camera.update();
        //if (getZpos() + zoomValue >= minZ && getZpos() + zoomValue <= maxZ) {
            tmp.set(camera.targetPosition).mulAdd(leftToFollow, zoomValue);
            camera.targetPosition.set(tmp);
        //}
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

    public Matrix4 getTransformToFollow() {
        return transformToFollow;
    }
}
