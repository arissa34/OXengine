package rma.ox.engine.shader.list;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.shaders.AbsBaseShader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;

/**
 * Created by Rami on 26/03/2018.
 */

public class Pixelation2dShader extends AbsBaseShader {

    protected final int u_texture = register(new Uniform("u_texture"));
    protected final int u_projTrans = register(new Uniform("u_projTrans"));
    protected final int u_pixelation_x = register(new Uniform("u_pixelation_x"));
    protected final int u_pixelation_y = register(new Uniform("u_pixelation_y"));

    private float pixelationX = 0.1f;
    private float pixelationY = 0.1f;

    public Pixelation2dShader(){
        super();
        loadShader();
    }

    @Override
    public String addPrefix() {
        return "";
    }

    @Override
    public String getVertexFileName() {
        return "pixelation.glsl:VS";
    }

    @Override
    public String getFragmentFileName() {
        return "pixelation.glsl:FS";
    }

    @Override
    public void init() {
        program.begin();
        program.setUniformf("u_pixelation_x", pixelationX);
        program.setUniformf("u_pixelation_y", pixelationY);
        program.end();
    }

    @Override
    public void begin(Camera camera, RenderContext context) {
        super.begin(camera, context);
        program.setUniformf("u_pixelation_x", pixelationX);
        program.setUniformf("u_pixelation_y", pixelationY);
    }

    @Override
    public void render(Renderable renderable) {
        renderable.meshPart.render(program);
    }

    public void setTexture(Texture texture){
        texture.bind(u_texture);
        set(u_pixelation_x, pixelationX);
        set(u_pixelation_y, pixelationY);
    }

    public void setPixelation(float pixelation){
        this.pixelationX = pixelation;
        this.pixelationY = pixelation;
        if(pixelationX <= 0) pixelationX=0.1f;
        if(pixelationY <= 0) pixelationY=0.1f;
        program.begin();
        program.setUniformf("u_pixelation_x", pixelationX);
        program.setUniformf("u_pixelation_y", pixelationY);
        program.end();
    }

    public void setPixelationX(float pixelationX) {
        this.pixelationX = pixelationX;
    }

    public void setPixelationY(float pixelationY) {
        this.pixelationY = pixelationY;
    }

}
