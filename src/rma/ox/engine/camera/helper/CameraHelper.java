package rma.ox.engine.camera.helper;

import com.badlogic.gdx.math.Matrix4;

import rma.ox.engine.camera.FrontalCamera;
import rma.ox.engine.camera.GhostCamera;
import rma.ox.engine.settings.SettingsHelper;

public class CameraHelper {

    private static CameraHelper instance;

    public static CameraHelper get() {
        if (instance == null) instance = new CameraHelper();
        return instance;
    }

    /*******************************/

    private FrontalCamera defaultCamera;

    public CameraHelper(){
        defaultCamera = new FrontalCamera(SettingsHelper.get().getFOV(), SettingsHelper.get().getWidth(), SettingsHelper.get().getHeight());
        defaultCamera.targetPosition.set(0, 50, 200);
        defaultCamera.targetDirection.set(0, 0, 0);
        defaultCamera.targetUp.set(0, 1, 0);
        defaultCamera.snapToTarget();
        defaultCamera.setTransformToFollow(new Matrix4().setTranslation(0, 10, 0));
        defaultCamera.initPosition();
    }

    public void update(float delta){
        getCamera().update(delta);
    }

    public GhostCamera getCamera(){
        return defaultCamera;
    }

}
