package rma.ox.engine.camera.helper;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.ArrayMap;

import rma.ox.engine.camera.FrontalCamera;
import rma.ox.engine.camera.GhostCamera;
import rma.ox.engine.camera.GodCamera;
import rma.ox.engine.settings.SettingsHelper;

public class CameraHelper {

    private static CameraHelper instance;

    public static CameraHelper get() {
        if (instance == null) instance = new CameraHelper();
        return instance;
    }

    /*******************************/

    private GhostCamera defaultCamera;
    private FrontalCamera frontalCamera;
    private GodCamera godCamera;

    public CameraHelper(){
        setGodCamera();
        defaultCamera.targetPosition.set(0, 50, 200);
    }

    public void update(float delta){
        getCamera().update(delta);
    }

    public GhostCamera getCamera(){
        return defaultCamera;
    }

    public void setFrontalCamera(Matrix4 targetToFollow){
        if(frontalCamera == null){
            frontalCamera  = new FrontalCamera(SettingsHelper.get().getFOV(), SettingsHelper.get().getWidth(), SettingsHelper.get().getHeight());
        }
        if(defaultCamera != null){
            passInfo(godCamera, defaultCamera);
        }
        defaultCamera = frontalCamera;
        frontalCamera.setTransformToFollow(targetToFollow);
        frontalCamera.initPosition();
    }

    public void setGodCamera(){
        if(godCamera == null){
            godCamera = new GodCamera(SettingsHelper.get().getFOV(), SettingsHelper.get().getWidth(), SettingsHelper.get().getHeight());
        }
        if(defaultCamera != null){
            passInfo(godCamera, defaultCamera);
        }
        defaultCamera = godCamera;
    }

    private void passInfo(GhostCamera newCam, GhostCamera oldCam){
        newCam.targetPosition.set(oldCam.targetPosition);
        newCam.targetDirection.set(oldCam.targetDirection);
        newCam.targetUp.set(oldCam.targetUp);
        newCam.snapToTarget();
    }

}
