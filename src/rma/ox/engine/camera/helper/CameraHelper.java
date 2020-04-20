package rma.ox.engine.camera.helper;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import rma.ox.engine.camera.FrontalCamera;
import rma.ox.engine.camera.GhostCamera;
import rma.ox.engine.camera.GodCamera;
import rma.ox.engine.camera.PivotCamera;
import rma.ox.engine.camera.controler.AbsController;
import rma.ox.engine.camera.controler.FrontalController;
import rma.ox.engine.camera.controler.GodController;
import rma.ox.engine.camera.controler.PivotController;
import rma.ox.engine.settings.SettingsHelper;

public class CameraHelper {

    private static CameraHelper instance;

    public static CameraHelper get() {
        if (instance == null) instance = new CameraHelper();
        return instance;
    }

    /*******************************/

    private GhostCamera camera;
    private AbsController defaultController;

    private PivotController pivotController;
    private GodController godController;
    private FrontalController frontalController;

    public CameraHelper(){
        camera = new GhostCamera(SettingsHelper.get().getFOV(), SettingsHelper.get().getWidth(), SettingsHelper.get().getHeight());
        FillViewport extendViewport = new FillViewport(SettingsHelper.get().getWidth(), SettingsHelper.get().getHeight());
        extendViewport.apply(true);
        camera.targetPosition.set(0, 50, 600);
        camera.snapToTarget();
        setGodCamera();
    }

    public GhostCamera getCamera(){
        return camera;
    }

    public void update(float delta){
        if(defaultController != null) {
            defaultController.update(delta);
        }
        getCamera().update(delta);
    }

    public GhostCamera setGodCamera(){
        if(godController == null){
            godController = new GodController(camera);
        }
        camera.far = 1000f;
        activateController(godController);
        return camera;
    }

    public GhostCamera setFrontalCamera(Matrix4 targetToFollow){
        if(frontalController == null){
            frontalController = new FrontalController(camera);
        }
        camera.far = 1000f;
        frontalController.setTransformToFollow(targetToFollow);
        frontalController.initPosition();
        activateController(frontalController);
        return camera;
    }

    public GhostCamera setPivotCamera(Vector3 pivotPos){
        if(pivotController == null){
            pivotController = new PivotController(camera);
        }
        camera.far = 10000f;
        pivotController.setTargetRotation(pivotPos);
        activateController(pivotController);
        return camera;
    }

    private void activateController(AbsController newController){
        if(defaultController != null) {
            defaultController.disable();
        }
        defaultController = newController;
        defaultController.enable();
    }

    public void disableTouch(){
        if(defaultController != null) {
            defaultController.disable();
        }
    }

    public void enableTouch(){
        if(defaultController != null) {
            defaultController.enable();
        }
    }

    public AbsController getDefaultController(){
        return defaultController;
    }

}
