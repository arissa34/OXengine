package rma.ox.engine.camera;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class FrontalCamera extends GhostCamera implements InputProcessor {

    public int screenX, screenY;
    private float minX = -100f;
    private float maxX = 100f;
    private float minY = -20f;
    private float maxY = 50f;
    private float minZ = 30f;
    private float maxZ = 150f;
    private float degreesPerPixel = 0.4f;
    public Matrix4 transformToFollow = new Matrix4();
    public Matrix4 followerAxis = new Matrix4();
    public Vector3 directionToFollow = new Vector3();
    public Vector3 rightToFollow = new Vector3();
    public Vector3 leftToFollow = new Vector3();
    public Vector3 rightCam = new Vector3();
    private int buttonTouchDown;

    public FrontalCamera(float fov, float width, float height) {
        super(fov, width, height);
        near = 10f;
        far = 1000f;
        InputMultiplexer inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
        inputMultiplexer.addProcessor(this);
    }

    public void update(float delta) {
        //Gdx.app.log("","update FrontalCamera position "+position);
        updateTransformToFollow();
        super.update(delta);
    }

    public void setTransformToFollow(Matrix4 transformToFollow) {
        this.transformToFollow = transformToFollow;
    }

    public void updateTransformToFollow() {
        targetUp.set(Vector3.Y).rot(transformToFollow).nor();
        directionToFollow.set(Vector3.X).rot(transformToFollow).nor();
        rightToFollow.set(targetUp).crs(directionToFollow).nor();
        leftToFollow.set(directionToFollow).crs(targetUp).nor();
        followerAxis.set(view).mul(transformToFollow);
        rightCam.set(tmp.set(targetUp).crs(targetDirection).nor());
    }

    public void initPosition() {
        updateTransformToFollow();
        transformToFollow.getTranslation(targetPosition).mulAdd(rightToFollow, -60);
        transformToFollow.getTranslation(targetDirection);
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
        followerAxis.set(targetView).mul(transformToFollow);
        return -followerAxis.getTranslation(tmp).x;
    }

    public float getYpos() {
        followerAxis.set(targetView).mul(transformToFollow);
        return -followerAxis.getTranslation(tmp).y;
    }

    public float getZpos() {
        followerAxis.set(targetView).mul(transformToFollow);
        return -followerAxis.getTranslation(tmp).z;
    }

    public float getZpos(Matrix4 mat) {
        followerAxis.set(mat).mul(transformToFollow);
        return followerAxis.getTranslation(tmp).z;
    }

    public void setZpos(float z) {
        float offs = z - getZpos();
        tmp.set(targetPosition).mulAdd(leftToFollow, offs);
        targetPosition.set(tmp);
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

        update();
        dragX(deltaX);
        dragY(deltaY);
    }

    public void dragX(float deltaX){
        if (getXpos() + deltaX > getMinX() && getXpos() + deltaX < getMaxX()) {
            targetDirection.mulAdd(directionToFollow, (deltaX));
            targetPosition.mulAdd(directionToFollow, (deltaX));
        }
    }

    public void dragY(float deltaY){
        if (getYpos() - deltaY > getMinY() && getYpos() - deltaY < getMaxY()) {
            targetDirection.mulAdd(targetUp, (-deltaY));
            targetPosition.mulAdd(targetUp, (-deltaY));
        }
    }

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
        update();
        if (getZpos() + zoomValue >= minZ && getZpos() + zoomValue <= maxZ) {
            tmp.set(targetPosition).mulAdd(leftToFollow, zoomValue);
            targetPosition.set(tmp);
        }
    }
}
