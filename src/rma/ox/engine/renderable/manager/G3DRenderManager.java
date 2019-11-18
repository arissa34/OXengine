package rma.ox.engine.renderable.manager;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelCache;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.BaseLight;
import com.badlogic.gdx.utils.Array;

import rma.ox.engine.camera.helper.CameraHelper;

public class G3DRenderManager {

    private static G3DRenderManager instance;

    public static G3DRenderManager get() {
        if (instance == null) instance = new G3DRenderManager();
        return instance;
    }

    /*******************************/

    private Array<ModelInstance> listModelsDynamic;
    private Array<ModelInstance> listModelsDynamicNoEnv;
    private Array<ModelInstance> listModelsCache;
    private Array<ModelInstance> listModelsCacheNoEnv;
    private ModelCache modelCache;
    private ModelCache modelCacheNoEnv;
    private ModelBatch modelBatch;

    private boolean modelCacheDirty = true;
    private boolean modelCacheNoEnvDirty = true;

    private Environment environment;
    public Array<BaseLight> lights;
    private ColorAttribute ambientColor;

    public G3DRenderManager() {
        modelBatch = new ModelBatch();
        modelCache = new ModelCache();
        modelCacheNoEnv = new ModelCache();
        environment = new Environment();
        environment.set(ambientColor = new ColorAttribute(ColorAttribute.AmbientLight, 0.35f, 0.35f, 0.35f, 1f));
        lights = new Array<>();
        listModelsDynamic = new Array<>();
        listModelsDynamicNoEnv = new Array<>();
        listModelsCache = new Array<>();
        listModelsCacheNoEnv = new Array<>();
    }

    public void render() {
        modelBatch.begin(CameraHelper.get().getCamera());
        {
            modelBatch.render(getModelCacheNoEnv());
            modelBatch.render(getModelCache(), environment);
            modelBatch.render(listModelsDynamicNoEnv);
            modelBatch.render(listModelsDynamic, environment);
        }
        modelBatch.end();
    }

    public ModelCache getModelCache() {
        if (modelCacheDirty) {
            updateModelCache();
        }
        return modelCache;
    }

    public ModelCache getModelCacheNoEnv() {
        if (modelCacheNoEnvDirty) {
            updateModelCacheNoEnv();
        }
        return modelCacheNoEnv;
    }

    public void addModelDynamic(ModelInstance instance) {
        listModelsDynamic.add(instance);
    }

    public void removeModelDynamic(ModelInstance instance) {
        listModelsDynamic.add(instance);
    }

    public void clearModelDynamic() {
        listModelsDynamic.clear();
    }

    public void addModelNoEnvDynamic(ModelInstance instance) {
        listModelsDynamicNoEnv.add(instance);
    }

    public void removeModelNoEnvDynamic(ModelInstance instance) {
        listModelsDynamicNoEnv.add(instance);
    }

    public void clearModelNoenvDynamic() {
        listModelsDynamicNoEnv.clear();
    }

    public void addModelToCache(ModelInstance instance) {
        listModelsCache.add(instance);
        modelCacheDirty = true;
    }

    public void removeModelToCache(ModelInstance instance) {
        listModelsCache.removeValue(instance, true);
        modelCacheDirty = true;
    }

    public void clearModelToCache() {
        listModelsCache.clear();
        modelCacheDirty = true;
    }

    private void updateModelCache() {
        modelCache.begin();
        {
            modelCache.add(listModelsCache);
        }
        modelCache.end();
        modelCacheDirty = false;
    }

    public void addModelToCacheNoEnv(ModelInstance instance) {
        listModelsCacheNoEnv.add(instance);
        modelCacheNoEnvDirty = true;
    }

    public void removeModelToCacheNoEnv(ModelInstance instance) {
        listModelsCacheNoEnv.removeValue(instance, true);
        modelCacheNoEnvDirty = true;
    }

    public void clearModelToCacheNoEnv() {
        listModelsCacheNoEnv.clear();
        modelCacheNoEnvDirty = true;
    }

    private void updateModelCacheNoEnv() {
        modelCacheNoEnv.begin();
        {
            modelCacheNoEnv.add(listModelsCacheNoEnv);
        }
        modelCacheNoEnv.end();
        modelCacheNoEnvDirty = false;
    }

    public ModelBatch getModelBatch() {
        return modelBatch;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public ColorAttribute getAmbientColor() {
        return ambientColor;
    }

    public void addLight(BaseLight light) {
        lights.add(light);
        environment.add(light);
    }

    public void clearAllLights() {
        environment.remove(lights);
        lights.clear();
    }
}
