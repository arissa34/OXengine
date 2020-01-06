package rma.ox.engine.renderable.obj;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

import rma.ox.engine.camera.helper.CameraHelper;
import rma.ox.engine.renderable.manager.G3DRenderManager;
import rma.ox.engine.renderable.utils.MaterialUtils;
import rma.ox.engine.update.UpdatableMainThread;
import rma.ox.engine.update.UpdateManager;

public class SkyboxModel implements UpdatableMainThread {

    private ModelInstance modelInstance;
    private Vector3 tmp;
    private Matrix4 transform;
    private Quaternion q;

    public SkyboxModel(Model model) {
        modelInstance = new ModelInstance(model);

        tmp = new Vector3();
        q = new Quaternion();
        transform = modelInstance.transform;

        MaterialUtils.addDepthTest(modelInstance);
        MaterialUtils.addMaterialNoLight(modelInstance);
        MaterialUtils.addMaterialCulling(modelInstance);
    }

    public SkyboxModel createSkyBox(Texture xpos, Texture xneg, Texture ypos, Texture yneg, Texture zpos, Texture zneg) {
        modelInstance.materials.get(0).set(TextureAttribute.createDiffuse(xpos));
        modelInstance.materials.get(1).set(TextureAttribute.createDiffuse(xneg));
        modelInstance.materials.get(2).set(TextureAttribute.createDiffuse(ypos));
        modelInstance.materials.get(3).set(TextureAttribute.createDiffuse(yneg));
        modelInstance.materials.get(5).set(TextureAttribute.createDiffuse(zpos));
        modelInstance.materials.get(4).set(TextureAttribute.createDiffuse(zneg));
        wrapTexture();
        return this;
    }

    public SkyboxModel attachToWorld() {
        if (modelInstance != null) {
            G3DRenderManager.get().setSkybox(modelInstance);
            UpdateManager.get().register(this);
        }
        return this;
    }

    public SkyboxModel dettachToWorld() {
        G3DRenderManager.get().setSkybox(null);
        UpdateManager.get().remove(this);
        return this;
    }

    private void wrapTexture() {
        for (int i = 0; i < modelInstance.materials.size; i++) {
            if (modelInstance.materials.get(i).has(TextureAttribute.Diffuse)) {
                modelInstance.materials.get(i).set(TextureAttribute.createDiffuse(wrap(modelInstance.materials.get(i))));
            }
        }
    }

    private Texture wrap(Material material) {
        Texture texture = ((TextureAttribute) material.get(TextureAttribute.Diffuse)).textureDescription.texture;
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        return texture;
    }

    public void update(Vector3 position) {
        tmp.set(position.x, position.y, position.z);
        transform.getRotation(q);
        modelInstance.transform.set(q);
        modelInstance.transform.setTranslation(tmp);
        modelInstance.transform.scl(20f);
    }

    @Override
    public void updateOnMainThread(float delta) {
        update(CameraHelper.get().getCamera().position);
    }
}
