package com.esotericsoftware.spine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.NumberUtils;
import com.badlogic.gdx.utils.ShortArray;
import com.esotericsoftware.spine.attachments.Attachment;
import com.esotericsoftware.spine.attachments.ClippingAttachment;
import com.esotericsoftware.spine.attachments.MeshAttachment;
import com.esotericsoftware.spine.attachments.RegionAttachment;
import com.esotericsoftware.spine.attachments.SkeletonAttachment;
import com.esotericsoftware.spine.utils.SkeletonClipping;
import com.esotericsoftware.spine.utils.TwoColorPolygonBatch;

public class CustomSkeletonRenderer {

    static private final String TAG = CustomSkeletonRenderer.class.getSimpleName();
    static private final short[] quadTriangles = {0, 1, 2, 2, 3, 0};

    private boolean premultipliedAlpha;
    private final FloatArray vertices = new FloatArray(32);
    private final SkeletonClipping clipper = new SkeletonClipping();
    private SkeletonRenderer.VertexEffect vertexEffect;
    private final Vector2 temp = new Vector2();
    private final Vector2 temp2 = new Vector2();
    private final Color temp3 = new Color();
    private final Color temp4 = new Color();
    private final Color temp5 = new Color();
    private final Color temp6 = new Color();

    Array<ModelInstance> listInstance;

    public void init(Skeleton skeleton, Camera camera) throws Exception{
        listInstance = new Array<ModelInstance>();

        Vector2 tempPos = this.temp;
        Vector2 tempUv = this.temp2;
        Color tempLight = this.temp3;
        Color tempDark = this.temp4;
        Color temp5 = this.temp5;
        Color temp6 = this.temp6;
        int verticesLength = 0;
        float[] vertices = null, uvs = null;
        short[] triangles = null;

        Array<Slot> drawOrder = skeleton.drawOrder;
        Color color = null, skeletonColor = skeleton.color;
        float r = skeletonColor.r, g = skeletonColor.g, b = skeletonColor.b, a = skeletonColor.a;

        for (int i = 0, n = drawOrder.size; i < n; i++) {

            Slot slot = drawOrder.get(i);
            Texture texture = null;
            Attachment attachment = slot.attachment;
            int vertexSize = clipper.isClipping() ? 2 : 5;

            if (attachment instanceof MeshAttachment) {
                MeshAttachment mesh = (MeshAttachment)attachment;
                int count = mesh.getWorldVerticesLength();
                verticesLength = (count >> 1) * vertexSize;
                vertices = this.vertices.setSize(verticesLength);
                mesh.computeWorldVertices(slot, 0, count, vertices, 0, vertexSize);
                //vertices = mesh.getVertices();
                triangles = mesh.getTriangles();
                texture = mesh.getRegion().getTexture();
                uvs = mesh.getUVs();
                color = mesh.getColor();
                Gdx.app.log("", "attachment : " +attachment.getName());
                Gdx.app.log("", "uvs length : " +uvs.length);
                Gdx.app.log("", "vertices length : " +vertices.length);
                Gdx.app.log("", "verticesLength : " +verticesLength);
                Gdx.app.log("", "triangles length : " +triangles.length);
                Gdx.app.log("", "color  : " +color);
                Gdx.app.log("", "-----------------------");


                Color slotColor = slot.getColor();
                float alpha = a * slotColor.a * color.a * 255;
                float c = NumberUtils.intToFloatColor(((int)alpha << 24) //
                        | ((int)(b * slotColor.b * color.b * alpha) << 16) //
                        | ((int)(g * slotColor.g * color.g * alpha) << 8) //
                        | (int)(r * slotColor.r * color.r * alpha));

                ModelBuilder modelBuilder = new ModelBuilder();
                modelBuilder.begin();
                MeshPartBuilder meshBuilder;
                Material material = new Material(TAG);//, new DepthTestAttribute(false)

                float[] vertices2 = new float[vertices.length + ((vertices.length/vertexSize)*3)];
                    if (vertexEffect != null) {
                    temp5.set(NumberUtils.floatToIntColor(c));
                    temp6.set(0);
                    for (int v = 0, u = 0, t = 0; v < vertices.length; v += 5, u += 2, t += 8) {
                        tempPos.x = vertices[v];
                        tempPos.y = vertices[v + 1];
                        tempLight.set(temp5);
                        tempDark.set(temp6);
                        tempUv.x = uvs[u];
                        tempUv.y = uvs[u + 1];
                        vertexEffect.transform(tempPos, tempUv, tempLight, tempDark);
                        vertices[v] = tempPos.x;
                        vertices[v + 1] = tempPos.y;
                        vertices[v + 2] = 0;
                        vertices[v + 3] = tempUv.x;
                        vertices[v + 4] = tempUv.y;
                        Gdx.app.log("", "x : " +vertices[v]);
                        Gdx.app.log("", "y : " +vertices[v+1]);
                        Gdx.app.log("", "y : " +vertices[v+2]);
                        //vertices2[t] = vertices[v];
                        //vertices2[t + 1] = vertices[v + 1];
                        //vertices2[t + 2] = vertices[v + 2];
                        //vertices2[t + 3] = slotColor.r;
                        //vertices2[t + 4] = slotColor.g;
                        //vertices2[t + 5] = slotColor.b;
                        //vertices2[t + 6] = tempUv.x;
                        //vertices2[t + 7] = tempUv.y;

                    }
                } else {
                    for (int v = 2, u = 0, t = 0; v < vertices.length; v += 5, u += 2, t += 8) {
                        vertices[v] = 0;
                        vertices[v + 1] = uvs[u];
                        vertices[v + 2] = uvs[u + 1];
                        Gdx.app.log("", "x : " +vertices[v-2]);
                        Gdx.app.log("", "y : " +vertices[v-1]);
                        Gdx.app.log("", "y : " +vertices[v]);
                        //vertices2[t] = vertices[v-2];
                        //vertices2[t + 1] = vertices[v-1];
                        //vertices2[t + 2] = vertices[v];
                        //vertices2[t + 3] = slotColor.r;
                        //vertices2[t + 4] = slotColor.g;
                        //vertices2[t + 5] = slotColor.b;
                        //vertices2[t + 6] = uvs[u];
                        //vertices2[t + 7] = uvs[u + 1];
                    }
                }

                meshBuilder = modelBuilder.part("rect", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position  | VertexAttributes.Usage.TextureCoordinates , material);
                //meshBuilder.setUVRange(1, 0f, 0f, 1);
                meshBuilder.addMesh(vertices, triangles);
                meshBuilder.setVertexTransformationEnabled(true);
                //meshBuilder.setVertexTransform(camera.combined);
                Model model = modelBuilder.end();
                ModelInstance modelInstance = new ModelInstance(model);
                material.set(TextureAttribute.createDiffuse(texture));
                //material.set(new ColorAttribute(ColorAttribute.Diffuse, color));
                material.set(new ColorAttribute(ColorAttribute.Diffuse, color));
                material.set(new BlendingAttribute(true, alpha));
                listInstance.add(modelInstance);

                modelInstance.transform.scl(6f);
                //gameModel.worldTransform.setToLookAt(camera.position, camera.up);
            }

        }
    }

    public void render(ModelBatch batch){
        for(ModelInstance modelInstance : listInstance){
            batch.render(modelInstance);
        }
    }

    @SuppressWarnings("null")
    public void draw (PolygonSpriteBatch batch, Skeleton skeleton) {
        Vector2 tempPos = this.temp;
        Vector2 tempUv = this.temp2;
        Color tempLight = this.temp3;
        Color tempDark = this.temp4;
        Color temp5 = this.temp5;
        Color temp6 = this.temp6;
        SkeletonRenderer.VertexEffect vertexEffect = this.vertexEffect;
        if (vertexEffect != null) vertexEffect.begin(skeleton);

        boolean premultipliedAlpha = this.premultipliedAlpha;
        BlendMode blendMode = null;
        int verticesLength = 0;
        float[] vertices = null, uvs = null;
        short[] triangles = null;
        Color color = null, skeletonColor = skeleton.color;
        float r = skeletonColor.r, g = skeletonColor.g, b = skeletonColor.b, a = skeletonColor.a;
        Array<Slot> drawOrder = skeleton.drawOrder;
        for (int i = 0, n = drawOrder.size; i < n; i++) {
            Slot slot = drawOrder.get(i);
            Texture texture = null;
            int vertexSize = clipper.isClipping() ? 2 : 5;
            Attachment attachment = slot.attachment;
            if (attachment instanceof MeshAttachment) {
                MeshAttachment mesh = (MeshAttachment)attachment;
                int count = mesh.getWorldVerticesLength();
                verticesLength = (count >> 1) * vertexSize;
                vertices = this.vertices.setSize(verticesLength);
                mesh.computeWorldVertices(slot, 0, count, vertices, 0, vertexSize);
                triangles = mesh.getTriangles();
                texture = mesh.getRegion().getTexture();
                uvs = mesh.getUVs();
                color = mesh.getColor();
            }

            if (texture != null) {
                Color slotColor = slot.getColor();
                float alpha = a * slotColor.a * color.a * 255;
                float c = NumberUtils.intToFloatColor(((int)alpha << 24) //
                        | ((int)(b * slotColor.b * color.b * alpha) << 16) //
                        | ((int)(g * slotColor.g * color.g * alpha) << 8) //
                        | (int)(r * slotColor.r * color.r * alpha));

                BlendMode slotBlendMode = slot.data.getBlendMode();
                if (slotBlendMode != blendMode) {
                    blendMode = slotBlendMode;
                    batch.setBlendFunction(blendMode.getSource(premultipliedAlpha), blendMode.getDest());
                }

                if (clipper.isClipping()) {
                    clipper.clipTriangles(vertices, verticesLength, triangles, triangles.length, uvs, c, 0, false);
                    FloatArray clippedVertices = clipper.getClippedVertices();
                    ShortArray clippedTriangles = clipper.getClippedTriangles();
                    if (vertexEffect != null) applyVertexEffect(clippedVertices.items, clippedVertices.size, 5, c, 0);
                    batch.draw(texture, clippedVertices.items, 0, clippedVertices.size, clippedTriangles.items, 0,
                            clippedTriangles.size);
                } else {
                    if (vertexEffect != null) {
                        temp5.set(NumberUtils.floatToIntColor(c));
                        temp6.set(0);
                        for (int v = 0, u = 0; v < verticesLength; v += 5, u += 2) {
                            tempPos.x = vertices[v];
                            tempPos.y = vertices[v + 1];
                            tempLight.set(temp5);
                            tempDark.set(temp6);
                            tempUv.x = uvs[u];
                            tempUv.y = uvs[u + 1];
                            vertexEffect.transform(tempPos, tempUv, tempLight, tempDark);
                            vertices[v] = tempPos.x;
                            vertices[v + 1] = tempPos.y;
                            vertices[v + 2] = tempLight.toFloatBits();
                            vertices[v + 3] = tempUv.x;
                            vertices[v + 4] = tempUv.y;
                        }
                    } else {
                        for (int v = 2, u = 0; v < verticesLength; v += 5, u += 2) {
                            vertices[v] = c;
                            vertices[v + 1] = uvs[u];
                            vertices[v + 2] = uvs[u + 1];
                        }
                    }


                    Gdx.app.log("", "=== DRAWW  2");
                    batch.draw(texture, vertices, 0, verticesLength, triangles, 0, triangles.length);
                }
            }

            clipper.clipEnd(slot);
        }
        clipper.clipEnd();
        if (vertexEffect != null) vertexEffect.end();
    }

    private void applyVertexEffect (float[] vertices, int verticesLength, int stride, float light, float dark) {
        Vector2 tempPos = this.temp;
        Vector2 tempUv = this.temp2;
        Color tempLight = this.temp3;
        Color tempDark = this.temp4;
        Color temp5 = this.temp5;
        Color temp6 = this.temp6;
        SkeletonRenderer.VertexEffect vertexEffect = this.vertexEffect;
        temp5.set(NumberUtils.floatToIntColor(light));
        temp6.set(NumberUtils.floatToIntColor(dark));
        if (stride == 5) {
            for (int v = 0; v < verticesLength; v += stride) {
                tempPos.x = vertices[v];
                tempPos.y = vertices[v + 1];
                tempUv.x = vertices[v + 3];
                tempUv.y = vertices[v + 4];
                tempLight.set(temp5);
                tempDark.set(temp6);
                vertexEffect.transform(tempPos, tempUv, tempLight, tempDark);
                vertices[v] = tempPos.x;
                vertices[v + 1] = tempPos.y;
                vertices[v + 2] = tempLight.toFloatBits();
                vertices[v + 3] = tempUv.x;
                vertices[v + 4] = tempUv.y;
            }
        } else {
            for (int v = 0; v < verticesLength; v += stride) {
                tempPos.x = vertices[v];
                tempPos.y = vertices[v + 1];
                tempUv.x = vertices[v + 4];
                tempUv.y = vertices[v + 5];
                tempLight.set(temp5);
                tempDark.set(temp6);
                vertexEffect.transform(tempPos, tempUv, tempLight, tempDark);
                vertices[v] = tempPos.x;
                vertices[v + 1] = tempPos.y;
                vertices[v + 2] = tempLight.toFloatBits();
                vertices[v + 3] = tempDark.toFloatBits();
                vertices[v + 4] = tempUv.x;
                vertices[v + 5] = tempUv.y;
            }
        }
    }

    public boolean getPremultipliedAlpha () {
        return premultipliedAlpha;
    }

    public void setPremultipliedAlpha (boolean premultipliedAlpha) {
        this.premultipliedAlpha = premultipliedAlpha;
    }

    public SkeletonRenderer.VertexEffect getVertexEffect () {
        return vertexEffect;
    }

    public void setVertexEffect (SkeletonRenderer.VertexEffect vertexEffect) {
        this.vertexEffect = vertexEffect;
    }

    static public interface VertexEffect {
        public void begin (Skeleton skeleton);

        public void transform (Vector2 position, Vector2 uv, Color color, Color darkColor);

        public void end ();
    }
}
