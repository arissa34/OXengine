package rma.ox.engine.core.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.BufferUtils;

import java.nio.FloatBuffer;

import rma.ox.engine.utils.Logx;

/**
 * Created by Rami on 17/02/2018.
 */
public class TextureUtils {

    public static Texture noFilter(Texture texture) {
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        return texture;
    }

    public static Texture bilinearFilter(Texture texture) {
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        return texture;
    }

    public static TextureLoader.TextureParameter bilinearFilter(TextureLoader.TextureParameter textureParameter) {
        textureParameter.minFilter = Texture.TextureFilter.Linear;
        textureParameter.magFilter = Texture.TextureFilter.Linear;
        return textureParameter;
    }

    public static Texture trilinearFilter(Texture texture) {
        texture.setFilter(Texture.TextureFilter.MipMap, Texture.TextureFilter.Nearest);
        return texture;
    }

    public static ModelLoader.ModelParameters trilinearFilter(ModelLoader.ModelParameters modelParameters){
        modelParameters.textureParameter.genMipMaps = true;
        modelParameters.textureParameter.minFilter = Texture.TextureFilter.MipMap;
        modelParameters.textureParameter.magFilter = Texture.TextureFilter.Linear;
        return modelParameters;
    }

    public static TextureLoader.TextureParameter trilinearFilter(TextureLoader.TextureParameter textureParameter){
        textureParameter.genMipMaps = true;
        textureParameter.minFilter = Texture.TextureFilter.MipMap;
        textureParameter.magFilter = Texture.TextureFilter.Linear;
        return textureParameter;
    }

    public static Texture anisotropicFilter(Texture texture, float anisotropy) {
        if (Gdx.graphics.supportsExtension("GL_EXT_texture_filter_anisotropic")) {
            texture.bind();
            FloatBuffer buffer = BufferUtils.newFloatBuffer(16);
            Gdx.gl20.glGetFloatv(GL20.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, buffer);
            float maxAnisotropySupported = buffer.get(0);
            float valueApplied = Math.min(maxAnisotropySupported, anisotropy);
            Gdx.gl20.glTexParameterf(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MAX_ANISOTROPY_EXT, valueApplied);
        } else {
            Logx.l(TextureUtils.class, "No Anisotropic !");
        }
        return texture;
    }
}
