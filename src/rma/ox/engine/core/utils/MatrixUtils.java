package rma.ox.engine.core.utils;

import com.badlogic.gdx.math.Matrix4;

public class MatrixUtils {

    public static float getX(Matrix4 matrix4){
        return matrix4.val[Matrix4.M03];
    }

    public static float getY(Matrix4 matrix4){
        return matrix4.val[Matrix4.M13];
    }

    public static float getZ(Matrix4 matrix4){
        return matrix4.val[Matrix4.M23];
    }

}
