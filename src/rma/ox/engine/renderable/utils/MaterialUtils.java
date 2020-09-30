package rma.ox.engine.renderable.utils;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.DepthTestAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.NoLightAttribute;

public class MaterialUtils {

    public static void addMaterialBlending(ModelInstance modelInstance){
        if(modelInstance == null) return;
        for(int i = 0; i < modelInstance.materials.size; i++){
            if(!modelInstance.materials.get(i).has(BlendingAttribute.Type)) {
                BlendingAttribute attribute = new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, 0.98f);
                modelInstance.materials.get(i).set(attribute);
            }
        }
    }

    public static void removeMaterialBlending(ModelInstance modelInstance){
        if(modelInstance == null) return;
        for(int i = 0; i < modelInstance.materials.size; i++){
            modelInstance.materials.get(i).remove(BlendingAttribute.Type);
        }
    }

    public static void updateBlendingValue(ModelInstance modelInstance, float opacity){
        if(modelInstance == null) return;
        for(int i = 0; i < modelInstance.materials.size; i++){
            if(modelInstance.materials.get(i).has(BlendingAttribute.Type)){
                BlendingAttribute attribute = (BlendingAttribute) modelInstance.materials.get(i).get(BlendingAttribute.Type);
                if(attribute != null){
                    attribute.opacity = opacity;
                    modelInstance.materials.get(i).set(attribute);
                }
            }
        }
    }

    public static void addMaterialNoLight(ModelInstance modelInstance){
       if(modelInstance == null) return;
       for(int i = 0; i < modelInstance.materials.size; i++){
           NoLightAttribute attribute = new NoLightAttribute();
           modelInstance.materials.get(i).set(attribute);
       }
    }

    public static void removeMaterialNoLight(ModelInstance modelInstance){
        if(modelInstance == null) return;
        for(int i = 0; i < modelInstance.materials.size; i++){
            modelInstance.materials.get(i).remove(NoLightAttribute.NoLight);
        }
    }

    public static void addMaterialCulling(ModelInstance modelInstance){
        if(modelInstance == null) return;
        for(int i = 0; i < modelInstance.materials.size; i++){
            modelInstance.materials.get(i).set(IntAttribute.createCullFace(0));
        }
    }

    public static void addMaterialAlphaTest(ModelInstance modelInstance){
        addMaterialAlphaTest(modelInstance, 0.5f);
    }

    public static void addMaterialAlphaTest(ModelInstance modelInstance, float value){
        if(modelInstance == null) return;
        for(int i = 0; i < modelInstance.materials.size; i++){
            modelInstance.materials.get(i).set(new FloatAttribute(FloatAttribute.AlphaTest, value));
        }
    }

    public static void addDepthTest(ModelInstance modelInstance){
        if(modelInstance == null) return;
        for(int i = 0; i < modelInstance.materials.size; i++){
            modelInstance.materials.get(i).set(new DepthTestAttribute(0));
        }
    }

    public static void enableDepthTest(ModelInstance modelInstance){
         setDepthTest(modelInstance, true);
    }

    public static void disableDepthTest(ModelInstance modelInstance){
         setDepthTest(modelInstance, false);
    }

    public static void setDepthTest(ModelInstance modelInstance, boolean activated){
        for(int i = 0; i < modelInstance.materials.size; i++){
            DepthTestAttribute attribute = (DepthTestAttribute) modelInstance.materials.get(i).get(DepthTestAttribute.Type);
            if(attribute != null){
                attribute.depthMask = activated;
            }else{
                modelInstance.materials.get(i).set(new DepthTestAttribute(activated));
            }
        }
    }
}
