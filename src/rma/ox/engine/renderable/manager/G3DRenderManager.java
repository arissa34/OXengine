package rma.ox.engine.renderable.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelCache;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.MyModelInstance;
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
import rma.ox.engine.utils.Logx;

public class G3DRenderManager {

    private static G3DRenderManager instance;

    public static G3DRenderManager get() {
        if (instance == null) instance = new G3DRenderManager();
        return instance;
    }

    /*******************************/

    private Array<MyModelInstance> listModelsDynamic;
    private Array<MyModelInstance> listModelsDynamicNoEnv;
    private Array<MyModelInstance> listModelsCache;
    private Array<MyModelInstance> listModelsCacheNoEnv;
    private Array<SkeletonRender> listSkeleton;
    private ModelInstance skybox;
    private ModelCache modelCache;
    private ModelCache modelCacheNoEnv;
    private ModelBatch modelBatch;
    private SpriteBatch spriteBatch;
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
                config.vertexShader = loader.load("main_shader.glsl:VS");
                config.fragmentShader = loader.load("main_shader.glsl:FS");
                config.numBones = 3;
                config.defaultCullFace = 0;
                return new MyDefaultShader(renderable, config);
            }
        });
        modelCache = new ModelCache();
        modelCacheNoEnv = new ModelCache();
        spriteBatch = new SpriteBatch();
        polygonBatch = new PolygonSpriteBatch();
        polygonBatch.setBlendFunction(GL20.GL_SRC_COLOR, GL20.GL_ONE);
        skeletonRenderer = new SkeletonRenderer();
        skeletonRenderer.setPremultipliedAlpha(true);
        environment = new Environment();
        environment.set(ambientColor = new ColorAttribute(ColorAttribute.AmbientLight, .35f, .35f, .35f, 1f));
        lights = new Array<>();
        listModelsDynamic = new Array<>();
        listModelsDynamicNoEnv = new Array<>();
        listModelsCache = new Array<>();
        listModelsCacheNoEnv = new Array<>();
        listSkeleton = new Array<>();
    }

    private int i;
    public void render() {
        render(true);
    }
    public void render(boolean renderSkyBox) {

        if (skybox != null && renderSkyBox) {
            modelBatch.begin(CameraHelper.getCamera());
                modelBatch.render(skybox);
            modelBatch.end();
        }

        modelBatch.begin(CameraHelper.getCamera());
        {
            modelBatch.render(getModelCacheNoEnv());
            modelBatch.render(getModelCache(), environment);

            for (i = 0; i < listModelsDynamicNoEnv.size; i++) {
                if (listModelsDynamicNoEnv.get(i).isVisible(CameraHelper.getCamera()))
                    modelBatch.render(listModelsDynamicNoEnv.get(i));
            }
            for (i = 0; i < listModelsDynamic.size; i++) {
                if (listModelsDynamic.get(i).isVisible(CameraHelper.getCamera()))
                    modelBatch.render(listModelsDynamic.get(i), environment);
            }
        }
        modelBatch.end();

        //Logx.e("--- nbr render listModelsDynamicNoEnv : "+listModelsDynamicNoEnv.size);

        renderSkeleton();
    }

    private void renderSkeleton() {

        if (listSkeleton.size == 0) return;

        synchronized (this) {
            listSkeleton.sort(zComparator);
        }

        polygonBatch.setProjectionMatrix(CameraHelper.get().getCamera().combined);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        polygonBatch.begin();
        for (int i = 0; i < listSkeleton.size; i++) {
            listSkeleton.get(i).draw(polygonBatch, skeletonRenderer);
        }
        polygonBatch.end();
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        //Gdx.gl.glDisable(GL20.GL_CULL_FACE);
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
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

    public synchronized void addModelDynamic(MyModelInstance instance) {
        if (!listModelsDynamic.contains(instance, true))
            listModelsDynamic.add(instance);
    }

    public synchronized void removeModelDynamic(MyModelInstance instance) {
        listModelsDynamic.removeValue(instance, true);
    }

    public synchronized void clearModelDynamic() {
        listModelsDynamic.clear();
    }

    public synchronized void addModelNoEnvDynamic(MyModelInstance instance) {
        if (!listModelsDynamicNoEnv.contains(instance, true))
            listModelsDynamicNoEnv.add(instance);
    }

    public synchronized void removeModelNoEnvDynamic(MyModelInstance instance) {
        listModelsDynamicNoEnv.removeValue(instance, true);
    }

    public synchronized void clearModelNoenvDynamic() {
        listModelsDynamicNoEnv.clear();
    }

    public synchronized void addModelToCache(MyModelInstance instance) {
        if (!listModelsCache.contains(instance, true)) {
            listModelsCache.add(instance);
            modelCacheDirty = true;
        }
    }

    public synchronized void removeModelToCache(MyModelInstance instance) {
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

    public void addModelToCacheNoEnv(MyModelInstance instance) {
        if (!listModelsCacheNoEnv.contains(instance, true)) {
            listModelsCacheNoEnv.add(instance);
            modelCacheNoEnvDirty = true;
        }
    }

    public void removeModelToCacheNoEnv(MyModelInstance instance) {
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

    public ModelInstance getSkybox() {
        return skybox;
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
        if (!lights.contains(light, true)) {
            lights.add(light);
            environment.add(light);
        }
    }

    public void removeLight(BaseLight light) {
        lights.removeValue(light, true);
        environment.remove(light);
    }

    public void clearAllLights() {
        environment.remove(lights);
        lights.clear();
    }

    public void clearAll() {
        clearAllLights();
        clearModelToCacheNoEnv();
        clearModelToCache();
        clearModelNoenvDynamic();
        clearModelDynamic();
        clearSkeletonList();
    }
}
