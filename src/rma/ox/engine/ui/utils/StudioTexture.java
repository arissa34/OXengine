package rma.ox.engine.ui.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import rma.ox.engine.camera.helper.CameraHelper;

public class StudioTexture {

    public static Texture faddingTexture(int width, int height, Color colorBottom, Color colorTop){
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        FrameBuffer frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
        frameBuffer.begin();
        {
            Gdx.gl.glClearColor(0, 0, 0, 0);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_ONE_MINUS_DST_ALPHA, GL20.GL_SRC_COLOR);
            shapeRenderer.setProjectionMatrix(CameraHelper.get().getCamera().combined);
            shapeRenderer.begin();
            {
                shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.rect(-(width/2), -(height/2), width, height, colorTop, colorTop, colorBottom, colorBottom);
            }
            Gdx.gl.glDisable(GL20.GL_BLEND);
            shapeRenderer.end();
        }
        frameBuffer.end();
        return frameBuffer.getColorBufferTexture();
    }

}
