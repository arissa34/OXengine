package com.badlogic.gdx.graphics.g3d.shaders;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;

import rma.ox.engine.shader.ShaderHelper;
import rma.ox.engine.shader.ShaderLoader;
import rma.ox.engine.update.UpdatableMainThread;
import rma.ox.engine.update.UpdateManager;

public abstract class AbsBaseShader extends MyBaseShader implements UpdatableMainThread {


    protected static float delta = 0f;

    private ShaderLoader loader;

    public AbsBaseShader() {
        super();
        UpdateManager.get().register(this);
    }

    public void loadShader(){
        loader = ShaderHelper.get().getShaderLoader();
        try{
            program = new ShaderProgram(loader.load(getVertexFileName()), addPrefix() + loader.load(getFragmentFileName()));
            if (!program.isCompiled()) {
                throw new RuntimeException(program.getLog());
            }
            init(program, null);
            init();
        }catch (Exception e){

        }
    }

    public abstract String addPrefix();

    public abstract String getVertexFileName();

    public abstract String getFragmentFileName();

    @Override
    public void init() {
    }

    @Override
    public void begin(Camera camera, RenderContext context) {
        program.begin();
    }

    @Override
    public void end () {
        program.end();
    }

    @Override
    public int compareTo(Shader other) {
        return 0;
    }

    @Override
    public boolean canRender(Renderable instance) {
        return true;
    }

    @Override
    public void updateOnMainThread(float delta) {
        this.delta = delta;
    }

    @Override
    public void dispose () {
        super.dispose();
        if(program != null) {
            program.dispose();
        }
    }

}
