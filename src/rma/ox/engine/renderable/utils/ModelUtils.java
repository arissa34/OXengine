package rma.ox.engine.renderable.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

import rma.ox.engine.camera.helper.CameraHelper;
import rma.ox.engine.core.utils.MatrixUtils;
import rma.ox.engine.utils.Logx;

public class ModelUtils {

    private static Vector3 tmp = new Vector3();
    private static Vector3 tmp2 = new Vector3();
    private static Vector3 tmp3 = new Vector3();
    private static Quaternion q = new Quaternion();
    private static Matrix4 m = new Matrix4();

    public static void faceToCamera(Matrix4 modelTransform){
        Camera camera = CameraHelper.get().getCamera();

        q.idt();
        m.idt();
        tmp.setZero();
        tmp2.setZero();
        tmp3.setZero();

        tmp2.set(modelTransform.getTranslation(tmp2));
        tmp3.set(modelTransform.getScale(tmp3));


        q = camera.view.getRotation(q).conjugate();
        modelTransform.set(q);

        //modelTransform.val[Matrix4.M13] = 0;

        m.setToTranslationAndScaling(tmp2, tmp.set(tmp3.x, tmp3.x, tmp3.x));
        modelTransform.mulLeft(m);
    }
}
