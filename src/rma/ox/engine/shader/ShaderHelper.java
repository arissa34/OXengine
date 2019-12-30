package rma.ox.engine.shader;

import com.badlogic.gdx.Gdx;

public class ShaderHelper {

    private static ShaderHelper instance;

    public static ShaderHelper get() {
        if (instance == null) instance = new ShaderHelper();
        return instance;
    }

    /************************/

    public static final String pathBaseShader = "data/shader";

    private ShaderLoader loader;

    public ShaderHelper(){
    }

    public ShaderLoader getShaderLoader(){
        if(loader == null){
            loader = new ShaderLoader(Gdx.files.internal(pathBaseShader));
        }
        return loader;
    }
}
