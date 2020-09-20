package rma.ox.engine.core.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class ScissorUtils {


    public static void displayRight(FrameBuffer frameBuffer, SpriteBatch spriteBatch){
        spriteBatch.flush();
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        int x = (int)(w - w*0.3f);
        int y = (int)(h - h*0.3f);
        w *= 0.3f;
        h *= 0.3f;

        Gdx.gl.glViewport(x, y, w, h);
        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
        Gdx.gl.glScissor(x, y, w, h);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        frameBuffer.getColorBufferTexture().bind(0);
        spriteBatch.disableBlending();
        spriteBatch.begin();
        spriteBatch.draw(frameBuffer.getColorBufferTexture(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, 0, 1, 1);
        //spriteBatch.draw(frameBuffer.getColorBufferTexture(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, 0, frameBuffer.getColorBufferTexture().getWidth(), frameBuffer.getColorBufferTexture().getHeight(),true, true);
        spriteBatch.end();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
    }

    public static void displayRightPixMap(Pixmap pixmap, SpriteBatch spriteBatch){
        spriteBatch.flush();
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        int x = (int)(w - w*0.3f);
        int y = (int)(h - h*0.3f);
        w *= 0.3f;
        h *= 0.3f;

        Gdx.gl.glViewport(x, y, w, h);
        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
        Gdx.gl.glScissor(x, y, w, h);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        spriteBatch.disableBlending();
        spriteBatch.begin();
        spriteBatch.draw(new Texture(pixmap), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, 0, 1, 1);
        spriteBatch.end();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
    }

    public static void displayLeft(FrameBuffer frameBuffer, SpriteBatch spriteBatch){
        spriteBatch.flush();
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        int x = (int)(0);
        int y = (int)(h - h*0.3f);
        w *= 0.3f;
        h *= 0.3f;

        Gdx.gl.glViewport(x, y, w, h);
        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
        Gdx.gl.glScissor(x, y, w, h);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        frameBuffer.getColorBufferTexture().bind(0);
        spriteBatch.disableBlending();
        spriteBatch.begin();
        spriteBatch.draw(frameBuffer.getColorBufferTexture(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, 0, 1, 1);
        spriteBatch.end();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
    }

    public static void displayLeft(FrameBuffer frameBuffer, SpriteBatch spriteBatch, float ratio){
        spriteBatch.flush();
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        int x = (int)(0);
        int y = (int)(h - h*ratio);
        w *= ratio;
        h *= ratio;

        Gdx.gl.glViewport(x, y, w, h);
        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
        Gdx.gl.glScissor(x, y, w, h);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        spriteBatch.disableBlending();
        spriteBatch.begin();
        spriteBatch.draw(frameBuffer.getColorBufferTexture(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, 0, 1, 1);
        spriteBatch.end();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
    }


    public static void displayRightBottom(FrameBuffer frameBuffer, SpriteBatch spriteBatch){
        spriteBatch.flush();
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        int x = (int)(w - w*0.3f) ;
        int y = (int)(0);
        w *= 0.3f;
        h *= 0.3f;

        Gdx.gl.glViewport(x, y, w, h);
        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
        Gdx.gl.glScissor(x, y, w, h);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        spriteBatch.disableBlending();
        spriteBatch.begin();
        spriteBatch.draw(frameBuffer.getColorBufferTexture(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, 0, 1, 1);
        spriteBatch.end();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
    }


    public static void displaLeftBottom(FrameBuffer frameBuffer, SpriteBatch spriteBatch){
        spriteBatch.flush();
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        int x = (int)(0) ;
        int y = (int)(0);
        w *= 0.3f;
        h *= 0.3f;

        Gdx.gl.glViewport(x, y, w, h);
        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
        Gdx.gl.glScissor(x, y, w, h);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        spriteBatch.disableBlending();
        spriteBatch.begin();
        spriteBatch.draw(frameBuffer.getColorBufferTexture(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, 0, 1, 1);
        spriteBatch.end();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
    }


    public static void displayFullScreen(FrameBuffer frameBuffer, SpriteBatch spriteBatch){
        spriteBatch.flush();
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        int x = (int)(0);
        int y = (int)(0);

        Gdx.gl.glViewport(x, y, w, h);
        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
        Gdx.gl.glScissor(x, y, w, h);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        spriteBatch.disableBlending();
        spriteBatch.begin();
        spriteBatch.draw(frameBuffer.getColorBufferTexture(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, 0, 1, 1);
        spriteBatch.end();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
    }
}
