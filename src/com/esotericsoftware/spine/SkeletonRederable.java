package com.esotericsoftware.spine;

import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import rma.ox.engine.core.threading.FrustrumThread;

import rma.ox.engine.camera.helper.CameraHelper;
import rma.ox.engine.core.utils.MatrixUtils;
import rma.ox.engine.renderable.manager.G3DRenderManager;
import rma.ox.engine.renderable.utils.ModelUtils;
import rma.ox.engine.renderable.skeleton.SkeletonRender;
import rma.ox.engine.skeleton.Direction;
import rma.ox.engine.update.UpdatableMainThread;
import rma.ox.engine.update.UpdatableThread;
import rma.ox.engine.update.UpdateManager;
import rma.ox.engine.utils.RandomUtils;

public class SkeletonRederable implements UpdatableMainThread, UpdatableThread, SkeletonRender {

    private boolean isInit;
    protected Skeleton skeleton;
    protected Matrix4 transform;
    protected Vector3 position;
    protected SkeletonAnimation animation;
    private boolean isVisibleInFrustrum = false;
    private Direction direction;

    public SkeletonRederable() {
        isInit = false;
    }

    public SkeletonRederable init(Skeleton skeleton){
        this.skeleton = skeleton;
        transform = new Matrix4();
        position = new Vector3();
        direction = Direction.RIGHT;
        animation = new SkeletonAnimation(skeleton);
        //animation.playLoopAnimation(10, "blink");
        //animation.addLoopAnimation(11, "idle", true);
        //animation.addLoopAnimation(12, "hold_riffle", true);
        isInit = true;
        return this;
    }

    public Skeleton getSkeleton() {
        return skeleton;
    }

    public Matrix4 getTransform() {
        return transform;
    }

    public void setPosition(float x, float y, float z){
        if(!isInit){ return;}
        transform.setTranslation(x, y, z);
    }

    public void setScale(float x, float y, float z){
        if(!isInit){ return;}
        if(direction == Direction.LEFT)
            transform.scale(-x, y, z);
        else
            transform.scale(x, y, z);
    }

    @Override
    public void updateOnMainThread(float delta) {
        if(!isInit){ return;}
        //ModelUtils.faceToCamera(transform);
        updateAnimation(delta);
    }

    private void updateAnimation(float delta){
        if(!isInit){ return;}
        animation.update(delta);
        animation.apply(isVisibleInFrustrum);
    }

    @Override
    public void updateOnThread() {
        if(!isInit){ return;}
        checkIfIsInFrustrum();
    }

    @Override
    public void draw(PolygonSpriteBatch polygonBatch, SkeletonRenderer skeletonRenderer) {
        if(!isInit){ return;}
        if(!isVisibleInFrustrum){ return;}
        polygonBatch.setTransformMatrix(transform);
        skeletonRenderer.draw(polygonBatch, skeleton);
    }

    public SkeletonAnimation getSkeletonAnimation() {
        return animation;
    }

    public Vector3 getPosition(){
        return transform.getTranslation(position);
    }

    public float getX(){
        return MatrixUtils.getX(transform);
    }

    public float getY(){
        return MatrixUtils.getY(transform);
    }

    public float getZ(){
        return MatrixUtils.getZ(transform);
    }

    @Override
    public float getZIndex(){
        if(!isInit){ return 0;}
        return getPosition().dst(CameraHelper.get().getCamera().position);
    }

    public void checkIfIsInFrustrum() {
        isVisibleInFrustrum = CameraHelper.get().getCamera().frustum.boundsInFrustum(getX(), getY(), getZ(), 10 / 2, 20 / 2, 0);
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
        if (direction == Direction.LEFT) {
            transform.scale(-1, 1, 1);
        } else {
            transform.scale(1, 1, 1);
        }
    }

    public void randomPos(){
        setPosition(RandomUtils.getRandomFloat(-150, 150),
                0,
                RandomUtils.getRandomFloat(-150, 10));
    }

    public SkeletonRederable attachToWorld() {
        if(!isInit){ return this;}
        FrustrumThread.get().register(this);
        UpdateManager.get().register(this);
        G3DRenderManager.get().addSkeleton(this);
        return this;
    }

    public SkeletonRederable removeToWorld() {
        if(!isInit){ return this;}
        FrustrumThread.get().remove(this);
        UpdateManager.get().remove(this);
        G3DRenderManager.get().removeSkeleton(this);
        return this;
    }
}
