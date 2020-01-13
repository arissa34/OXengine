package rma.ox.engine.renderable.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelCache;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.BaseLight;
import com.badlogic.gdx.graphics.g3d.shaders.MyDefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.SkeletonRenderer;

import java.util.Comparator;

import rma.ox.engine.camera.helper.CameraHelper;
import rma.ox.engine.renderable.skeleton.SkeletonRender;
import rma.ox.engine.shader.ShaderHelper;
import rma.ox.engine.shader.ShaderLoader;

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
    private Array<SkeletonRender> listSkeleton;
    private ModelInstance skybox;
    private ModelCache modelCache;
    private ModelCache modelCacheNoEnv;
    private ModelBatch modelBatch;
    private DefaultShaderProvider shaderProvider;

    private PolygonSpriteBatch polygonBatch;
    private SkeletonRenderer skeletonRenderer;

    private boolean modelCacheDirty = true;
    private boolean modelCacheNoEnvDirty = true;

    private Environment environment;
    public Array<BaseLight> lights;
    private ColorAttribute ambientColor;

    public G3DRenderManager() {
        //modelBatch = new ModelBatch();

        modelBatch = new ModelBatch(shaderProvider = new DefaultShaderProvider() {
            @Override
            protected Shader createShader(final Renderable renderable) {
                ShaderLoader loader = ShaderHelper.get().getShaderLoader();
                MyDefaultShader.Config config = new MyDefaultShader.Config();
                config.vertexShader = loader.load("day_night_color.glsl:VS");
                config.fragmentShader = loader.load("day_night_color.glsl:FS");
                config.numBones = 3;
                return new MyDefaultShader(renderable, config);
            }
        });
        modelCache = new ModelCache();
        modelCacheNoEnv = new ModelCache();
        polygonBatch = new PolygonSpriteBatch();
        polygonBatch.setBlendFunction(GL20.GL_SRC_COLOR, GL20.GL_ONE);
        skeletonRenderer = new SkeletonRenderer();
        skeletonRenderer.setPremultipliedAlpha(true);
        environment = new Environment();
        environment.set(ambientColor = new ColorAttribute(ColorAttribute.AmbientLight, 0.35f, 0.35f, 0.35f, 1f));
        lights = new Array<>();
        listModelsDynamic = new Array<>();
        listModelsDynamicNoEnv = new Array<>();
        listModelsCache = new Array<>();
        listModelsCacheNoEnv = new Array<>();
        listSkeleton = new Array<>();
    }

    public void render() {
        polygonBatch.setProjectionMatrix(CameraHelper.get().getCamera().combined);

        modelBatch.begin(CameraHelper.get().getCamera());
        {
            if(skybox != null) modelBatch.render(skybox, environment);
            modelBatch.render(getModelCacheNoEnv());
            modelBatch.render(getModelCache(), environment);
            modelBatch.render(listModelsDynamicNoEnv);
            modelBatch.render(listModelsDynamic, environment);
        }
        modelBatch.end();

        synchronized (this) {
            listSkeleton.sort(zComparator);
        }

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        polygonBatch.begin();
        for (int i = 0; i < listSkeleton.size; i++) {
            listSkeleton.get(i).draw(polygonBatch, skeletonRenderer);
        }
        polygonBatch.end();
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);

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

    public synchronized void addSkeleton(SkeletonRender skeleton) {
        if (!listSkeleton.contains(skeleton, true)) {
            listSkeleton.add(skeleton);
            listSkeleton.sort(zComparator);
        }
    }

    Comparator<SkeletonRender> zComparator = new Comparator<SkeletonRender>() {
        @Override
        public int compare(SkeletonRender e1, SkeletonRender e2) {
            if (e1 == null) return 0;
            if (e2 == null) return 0;
            return Float.compare(e2.getZIndex(), e1.getZIndex());
        }
    };

    public synchronized void removeSkeleton(SkeletonRender skeleton) {
        listSkeleton.removeValue(skeleton, true);
    }

    public void clearSkeletonList() {
        listSkeleton.clear();
    }

    public synchronized void addModelDynamic(ModelInstance instance) {
        listModelsDynamic.add(instance);
    }

    public synchronized void removeModelDynamic(ModelInstance instance) {
        listModelsDynamic.removeValue(instance, true);
    }

    public synchronized void clearModelDynamic() {
        listModelsDynamic.clear();
    }

    public synchronized void addModelNoEnvDynamic(ModelInstance instance) {
        listModelsDynamicNoEnv.add(instance);
    }

    public synchronized void removeModelNoEnvDynamic(ModelInstance instance) {
        listModelsDynamicNoEnv.add(instance);
    }

    public synchronized void clearModelNoenvDynamic() {
        listModelsDynamicNoEnv.clear();
    }

    public synchronized void addModelToCache(ModelInstance instance) {
        listModelsCache.add(instance);
        modelCacheDirty = true;
    }

    public synchronized void removeModelToCache(ModelInstance instance) {
        listModelsCache.removeValue(instance, true);
        modelCacheDirty = true;
    }

    public synchronized void clearModelToCache() {
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

    public void setSkybox(ModelInstance skybox) {
        this.skybox = skybox;
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
