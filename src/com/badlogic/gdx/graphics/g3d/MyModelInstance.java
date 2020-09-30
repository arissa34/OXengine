package com.badlogic.gdx.graphics.g3d;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class MyModelInstance extends ModelInstance {

    protected Vector3 position, center, dimensions, halfExtents;
    protected Matrix4 localTransform;
    protected BoundingBox boundingBoxCache;
    protected BoundingBox boundingBox;
    protected float boundingBoxRadius;

    public MyModelInstance(Model model) {
        super(model);

        boundingBoxCache = new BoundingBox();
        boundingBox = new BoundingBox();
        position = new Vector3();
        center = new Vector3();
        dimensions = new Vector3();
        halfExtents = new Vector3();
        localTransform = new Matrix4();
    }

    public void initLocalTransform(){
        localTransform.set(transform);
    }

    public synchronized void createBoundingBox() {
        boundingBoxCache.set(calculateBoundingBox(boundingBox));
        updateBoundingBox();
    }

    public void updateBoundingBox() {
        boundingBox.set(boundingBoxCache).mul(transform);
        boundingBox.getCenter(center);
        boundingBox.getDimensions(dimensions);
        boundingBoxRadius = dimensions.len() / 2f;
        halfExtents.set(dimensions).scl(0.5f);
    }

    public void applyTransform(Vector3 location, Vector3 rotation, Vector3 scale) {
        for (Node node : nodes) {
            node.scale.set(Math.abs(scale.x), Math.abs(scale.z), Math.abs(scale.y));
        }

        transform.rotate(Vector3.X, rotation.x);
        transform.rotate(Vector3.Y, rotation.y);
        transform.rotate(Vector3.Z, rotation.z);
        transform.setTranslation(location);
        calculateTransforms();
    }

    public void setTransform(Matrix4 transform) {
        this.transform.set(transform);
        updateBoundingBox();
    }

    public void setPosition(Vector3 position) {
        this.transform.setTranslation(position);
        updateBoundingBox();
    }

    public void setPosition(float x, float y, float z) {
        this.transform.setTranslation(x, y, z);
        updateBoundingBox();
    }

    public void setWorldTransform(Matrix4 parentTransform) {
        transform.set(parentTransform).mul(localTransform);
        updateBoundingBox();
    }

    public Vector3 getPosition() {
        return transform.getTranslation(position);
    }

    public Matrix4 getLocalTransform() {
        return localTransform;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public boolean isVisible(Camera camera) {
        if(boundingBox == null) return true;
        return camera.frustum.sphereInFrustum(center, boundingBoxRadius);
    }


}
