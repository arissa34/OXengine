package rma.ox.engine.ui.dev;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import rma.ox.engine.update.UpdatableMainThread;
import rma.ox.engine.update.UpdateManager;
import rma.ox.engine.utils.Logx;

public class DevGLProfile extends AbsDevMenu implements UpdatableMainThread {

    private  GLProfiler glProfiler;
    private int calls, drawCalls, shaders, texture, javaHeap, nativeHeap;
    private int mb = 1024 * 1024;
    Label callsLbl    ;
    Label drawCallsLbl;
    Label shadersLbl  ;
    Label textureLbl  ;
    Label javaHeapLbl ;
    Label nativeHeapLbl ;


    private StringBuilder callsSB;
    private StringBuilder drawCallsSB;
    private StringBuilder shadersSB;
    private StringBuilder textureSB;
    private StringBuilder javaHeapSB;
    private StringBuilder nativeHeapSB;


    public DevGLProfile(Stage stage, String title, BitmapFont font, GLProfiler glProfiler) {
        super(stage, title, font);
        this.glProfiler = glProfiler;
        drawCalls = glProfiler.getDrawCalls();
        shaders = glProfiler.getShaderSwitches();
        texture = glProfiler.getTextureBindings();
        javaHeap = (int) (Gdx.app.getJavaHeap() / mb);
        nativeHeap = (int) (Gdx.app.getNativeHeap() / mb);


        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        callsLbl        =    new Label("", labelStyle);
        drawCallsLbl    =    new Label("", labelStyle);
        shadersLbl      =    new Label("", labelStyle);
        textureLbl      =    new Label("", labelStyle);
        javaHeapLbl     =    new Label("", labelStyle);
        nativeHeapLbl     =    new Label("", labelStyle);

        callsSB     = new StringBuilder();
        drawCallsSB = new StringBuilder();
        shadersSB   = new StringBuilder();
        textureSB   = new StringBuilder();
        javaHeapSB   = new StringBuilder();
        nativeHeapSB   = new StringBuilder();

        getContentTable().bottom().left().pad(20);
       getContentTable().add(callsLbl).left().row();
       getContentTable().add(drawCallsLbl).left().row();
       getContentTable().add(shadersLbl).left().row();
       getContentTable().add(textureLbl).left().row();
       getContentTable().add(javaHeapLbl).left().row();
       getContentTable().add(nativeHeapLbl).left().row();
    }


    @Override
    public float getPrefWidth() {
        return 250;
    }

    @Override
    public float getPrefHeight() {
        return stage.getHeight() < 250 ? stage.getHeight() - 30 : 250;
    }

    @Override
    public void show(float x, float y) {
        super.show(x, y);
        UpdateManager.get().register(this);
    }

    @Override
    public void hide() {
        super.hide();
        UpdateManager.get().remove(this);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        getProfiles();
    }

    @Override
    public void updateOnMainThread(float delta) {


    }

    private synchronized void getProfiles(){
        calls = glProfiler.getCalls();
        drawCalls = glProfiler.getDrawCalls();
        shaders = glProfiler.getShaderSwitches();
        texture = glProfiler.getTextureBindings();
        javaHeap = (int) (Gdx.app.getJavaHeap() / mb);
        nativeHeap = (int) (Gdx.app.getNativeHeap() / mb);

        callsSB.setLength(0);
        drawCallsSB.setLength(0);
        shadersSB.setLength(0);
        textureSB.setLength(0);
        javaHeapSB.setLength(0);
        nativeHeapSB.setLength(0);

        callsLbl.setText(callsSB.append("Calls : ").append(calls));
        drawCallsLbl.setText(drawCallsSB.append("Draw calls : ").append(drawCalls));
        shadersLbl.setText(shadersSB.append("Shader switches : ").append(shaders));
        textureLbl.setText(textureSB.append("Texture binding : ").append(texture));
        javaHeapLbl.setText(javaHeapSB.append("Java Heap : ").append(javaHeap).append("MB"));
        nativeHeapLbl.setText(nativeHeapSB.append("Native Heap : ").append(nativeHeap).append("MB"));

        glProfiler.reset();
    }
}
