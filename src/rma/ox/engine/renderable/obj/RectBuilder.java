package rma.ox.engine.renderable.obj;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ShortArray;

public class RectBuilder {

    public static final String TAG = RectBuilder.class.getSimpleName();

    private float width, height;
    private FloatArray verts;
    private ShortArray indices;
    private Material material;
    private Mesh mesh;
    private MeshPart meshPart;
    private Node node;
    private Model model;
    private VertexAttributes vertexAttributes;

    public RectBuilder(float width, float height){
        this.width = width;
        this.height = height;

        verts = new FloatArray();
        indices = new ShortArray();

        buildVertices();
        buildIndices();
        buildMesh();

        material = new Material(TAG);
        model = new Model();
        model.materials.add(material);
        model.meshes.add(mesh);
        model.meshParts.add(meshPart);
        node = new Node();
        node.parts.add(new NodePart(meshPart, material));
        model.nodes.add(node);

        verts.clear();
        indices.clear();
    }

    private void buildVertices(){
        verts.add(-width);verts.add(-height);verts.add(0);      // position
        //verts.add(0);verts.add(0);verts.add(1);                 // normal
        verts.add(1);verts.add(1);verts.add(1);verts.add(1);    // color
        verts.add(1);verts.add(1);                              // texture

        verts.add(width);verts.add(-height);verts.add(0);      // position
        //verts.add(0);verts.add(0);verts.add(1);
        verts.add(1);verts.add(1);verts.add(1);verts.add(1);
        verts.add(0);verts.add(1);

        verts.add(width);verts.add(height);verts.add(0);      // position
        //verts.add(0);verts.add(0);verts.add(1);
        verts.add(1);verts.add(1);verts.add(1);verts.add(1);
        verts.add(0);verts.add(0);

        verts.add(-width);verts.add(height);verts.add(0);      // position
        //verts.add(0);verts.add(0);verts.add(1);
        verts.add(1);verts.add(1);verts.add(1);verts.add(1);
        verts.add(1);verts.add(0);
    }

    private void buildIndices(){
        indices.add(0); indices.add(1); indices.add(3);
        indices.add(3); indices.add(1); indices.add(2);
    }

    private void buildMesh(){
        vertexAttributes = new VertexAttributes(VertexAttribute.Position()/*, VertexAttribute.Normal()*/, VertexAttribute.ColorUnpacked(), VertexAttribute.TexCoords(0));

        mesh = new Mesh(true, verts.items.length, indices.items.length, vertexAttributes);
        mesh.setVertices(verts.items);
        mesh.setIndices(indices.items);

        meshPart = new MeshPart();
        meshPart.primitiveType = GL20.GL_TRIANGLES;
        meshPart.offset = 0;
        meshPart.size= mesh.getNumIndices();
        meshPart.mesh = mesh;
        meshPart.update();
    }

    public Model getModel(){
        return model;
    }

}
