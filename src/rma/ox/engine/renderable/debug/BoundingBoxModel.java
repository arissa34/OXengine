package rma.ox.engine.renderable.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.IndexBufferObject;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.VertexBufferObject;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ShortArray;

public class BoundingBoxModel {

    public BoundingBox boundingBox;
    public VertexBufferObject vbo;
    public IndexBufferObject ibo;

    public FloatArray verts;
    public ShortArray indices;

    public static VertexAttribute vertexAttribute = VertexAttribute.Position();

    public BoundingBoxModel(BoundingBox boundingBox){
        this.boundingBox = boundingBox;
        verts = new FloatArray();
        indices = new ShortArray();
        initShader();
    }

    private void updateBox(){
        verts.clear(); indices.clear();
        final float x0 = boundingBox.min.x, y0 = boundingBox.min.y, z0 = boundingBox.min.z, x1 = boundingBox.max.x, y1 = boundingBox.max.y, z1 = boundingBox.max.z;
        verts.add(x0);verts.add(y0);verts.add(z0);
        verts.add(x0);verts.add(y0);verts.add(z1);
        verts.add(x0);verts.add(y1);verts.add(z0);
        verts.add(x0);verts.add(y1);verts.add(z1);
        verts.add(x1);verts.add(y0);verts.add(z0);
        verts.add(x1);verts.add(y0);verts.add(z1);
        verts.add(x1);verts.add(y1);verts.add(z0);
        verts.add(x1);verts.add(y1);verts.add(z1);

        indices.add(0);indices.add(1);indices.add(2);indices.add(0);
        indices.add(2);indices.add(3);indices.add(1);indices.add(2);

        indices.add(6);indices.add(0);indices.add(4);indices.add(6);
        indices.add(7);indices.add(5);indices.add(6);
        indices.add(4);indices.add(5);indices.add(7);

        indices.add(3);indices.add(1);indices.add(7);
        indices.add(5);indices.add(1);indices.add(3);

    }

    private void buildVBO(){
        if(vbo == null){
            vbo = new VertexBufferObject(false, verts.size, vertexAttribute);
            vbo.setVertices(verts.items, 0, verts.size);
        }else{
            vbo.updateVertices(0, verts.items, 0, verts.size);
        }
        if(ibo == null) {
            ibo = new IndexBufferObject(indices.size);
            ibo.setIndices(indices.items, 0, indices.size);
        }else{
            ibo.updateIndices(0, indices.items, 0, 0 );
        }
    }

    public void update(){
        updateBox();
        buildVBO();
    }

    public void render(Camera camera){
        shader.begin();
        shader.setUniformMatrix("u_projTrans", camera.combined);

        ibo.bind();
        vbo.bind(shader);
        Gdx.gl20.glDrawElements(GL20.GL_LINE_STRIP, ibo.getNumIndices(), GL20.GL_UNSIGNED_SHORT, 0);
        vbo.unbind(shader);
        ibo.unbind();

        shader.end();
    }

    ShaderProgram shader;

    public void initShader() {
        String vertexShader = "attribute vec4 a_position;                                                   \n" +
                "attribute vec4 a_color;                                                                    \n" +
                "attribute vec2 a_texCoord0;                                                                \n" +
                "uniform mat4 u_projTrans;                                                                  \n" +
                "varying vec4 v_color;                                                                      \n" +
                "varying vec2 v_texCoords;                                                                  \n" +
                "                                                                                           \n" +
                "void main()                                                                                \n" +
                "{                                                                                          \n" +
                "   vec3 pos = vec3(a_position.x , a_position.y, a_position.z );                            \n" +
                "   v_color = vec4(1, 1, 1, 1);                                                             \n" +
                "   v_texCoords = a_texCoord0;                                                              \n" +
                "   gl_Position =  u_projTrans  * vec4(pos, 1.0);                                           \n" +
                "}                                                                                          \n";

        String fragmentShader = "#ifdef GL_ES                                                               \n" +
                "precision mediump float;                                                                   \n" +
                "#endif                                                                                     \n" +
                "varying vec4 v_color;                                                                      \n" +
                "varying vec2 v_texCoords;                                                                  \n" +
                "uniform sampler2D u_texture;                                                               \n" +
                "void main()                                                                                \n" +
                "{                                                                                          \n" +
                "  gl_FragColor = v_color;//* texture2D(u_texture, v_texCoords);                           \n" +
                "}                                                                                          \n";

        shader = new ShaderProgram(vertexShader, fragmentShader);
        if (!shader.isCompiled()) {
            Gdx.app.log("ShaderTest", shader.getLog());
            Gdx.app.exit();
        }
    }

}