package rma.ox.engine.ui.gui.button;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import rma.ox.engine.ressource.MyAssetManager;
import rma.ox.engine.utils.Logx;

public class CustomButton {

    public Button button;
    public Label label;
    public MyAssetManager mAssets;

    public CustomButton(String atlasName, String pathUp, String pathDown, String pathDisable){
        mAssets = MyAssetManager.get();

        Button.ButtonStyle btnStyle = new Button.ButtonStyle();

        TextureAtlas atlas = mAssets.get(atlasName, TextureAtlas.class);

        if(atlas.findRegion(pathUp).splits != null) {
            NinePatch upNinePatch = atlas.createPatch(pathUp);
            NinePatchDrawable upPatchDrawable = new NinePatchDrawable(upNinePatch);
            btnStyle.up = upPatchDrawable;
        }else{
            btnStyle.up = new TextureRegionDrawable(new TextureRegion(atlas.createSprite(pathUp)));
        }

        if(atlas.findRegion(pathDown).splits != null) {
            NinePatch downNinePatch = atlas.createPatch(pathDown);
            NinePatchDrawable downPatchDrawable = new NinePatchDrawable(downNinePatch);
            btnStyle.down = downPatchDrawable;
        }else {
            btnStyle.down = new TextureRegionDrawable(new TextureRegion(atlas.createSprite(pathDown)));
        }

        if(atlas.findRegion(pathDisable).splits != null) {
            NinePatch disableNinePatch = atlas.createPatch(pathDisable);
            NinePatchDrawable disablePatchDrawable = new NinePatchDrawable(disableNinePatch);
            btnStyle.disabled = disablePatchDrawable;
        }else {
            btnStyle.disabled = new TextureRegionDrawable(new TextureRegion(atlas.createSprite(pathDisable)));
        }

        button = new Button(btnStyle);
    }

    public CustomButton(String pathUp, String pathDown, String pathDisable){
        mAssets = MyAssetManager.get();
        Texture textureUp = mAssets.get(pathUp, Texture.class);
        Texture textureDown = mAssets.get(pathDown, Texture.class);
        Texture textureDisable = mAssets.get(pathDisable, Texture.class);
        Button.ButtonStyle btnStyle = new Button.ButtonStyle();
        btnStyle.down = new TextureRegionDrawable(new TextureRegion(textureDown));
        btnStyle.up = new TextureRegionDrawable(new TextureRegion(textureUp));
        btnStyle.disabled = new TextureRegionDrawable(new TextureRegion(textureDisable));
        button = new Button(btnStyle);
    }

    public CustomButton(String pathUp, String pathDown){
        mAssets = MyAssetManager.get();
        Texture textureUp = mAssets.get(pathUp, Texture.class);
        Texture textureDown = mAssets.get(pathDown, Texture.class);
        Button.ButtonStyle btnStyle = new Button.ButtonStyle();
        btnStyle.down = new TextureRegionDrawable(new TextureRegion(textureDown));
        btnStyle.up = new TextureRegionDrawable(new TextureRegion(textureUp));
        button = new Button(btnStyle);
    }

    public CustomButton(TextureRegionDrawable textureUp, TextureRegionDrawable textureDown){
        mAssets = MyAssetManager.get();
        Button.ButtonStyle btnStyle = new Button.ButtonStyle();
        btnStyle.down = textureDown;
        btnStyle.up = textureUp;
        button = new Button(btnStyle);
    }


    public CustomButton(TextureAtlas atlas, String textureUpString, String textureDownString, boolean isNinePatch){
        Button.ButtonStyle btnStyle = new Button.ButtonStyle();
        if(!isNinePatch){
            btnStyle.down = new TextureRegionDrawable(atlas.findRegion(textureDownString));
            btnStyle.up = new TextureRegionDrawable(atlas.findRegion(textureUpString));
        }else{
            NinePatch upNinePatch = atlas.createPatch(textureUpString);
            NinePatchDrawable upPatchDrawable = new NinePatchDrawable(upNinePatch);
            btnStyle.up = upPatchDrawable;

            NinePatch downNinePatch = atlas.createPatch(textureDownString);
            NinePatchDrawable downPatchDrawable = new NinePatchDrawable(downNinePatch);
            btnStyle.down = downPatchDrawable;
        }
        button = new Button(btnStyle);
    }

    public CustomButton(String pathUp){
        mAssets = MyAssetManager.get();
        Texture textureUp = mAssets.get(pathUp, Texture.class);
        Button.ButtonStyle btnStyle = new Button.ButtonStyle();
        btnStyle.up = new TextureRegionDrawable(new TextureRegion(textureUp));
        button = new Button(btnStyle);
    }

    public CustomButton addImageBehind(String pathTexture){
        button.add(new Image( mAssets.get(pathTexture, Texture.class)));
        return this;
    }

    public CustomButton addTextBehind(CharSequence text){
        button.add(text);
        return this;
    }

    public CustomButton addListener(InputListener listener){
        if(button.isDisabled()) return this;
        button.addListener(listener);
        return this;
    }

    public CustomButton setSize(float width, float height){
        button.setWidth(width);
        button.setHeight(height);
        button.getStyle().up.setMinWidth(width);
        button.getStyle().up.setMinHeight(height);
        if(button.getStyle().down != null){
            button.getStyle().down.setMinWidth(width);
            button.getStyle().down.setMinHeight(height);
        }
        if(button.getStyle().disabled != null){
            button.getStyle().disabled.setMinWidth(width);
            button.getStyle().disabled.setMinHeight(height);
        }
        return this;
    }

    public CustomButton setCoord(float x, float y){
        button.setX(x);
        button.setY(y);
        return this;
    }

    public CustomButton setScale(float scaleX, float scaleY) {
        button.setScale(scaleX, scaleY);
        return this;
    }

    public CustomButton setDisable(boolean disable){
        button.setDisabled(disable);
        return this;
    }

    public CustomButton putInStage(Stage stage){
        stage.addActor(button);
        return this;
    }

    public CustomButton setText(CharSequence txt, BitmapFont font){
        if(label == null) {
            Label.LabelStyle labelStyle = new Label.LabelStyle();
            labelStyle.font = font;
            button.add(label = new Label(txt, labelStyle));
        }else {
            label.setText(txt);
        }
        return this;
    }
    public CustomButton addLabels(int padB, Actor...labels){
        button.add(labels).padBottom(padB);
        return this;
    }

    public CustomButton setText(CharSequence txt, BitmapFont font, int padB){
        if(label == null) {
            Label.LabelStyle labelStyle = new Label.LabelStyle();
            labelStyle.font = font;
            button.add(label = new Label(txt, labelStyle)).padBottom(padB);
        }else {
            label.setText(txt);
        }
        return this;
    }

    public CustomButton setText(CharSequence txt, float scale, BitmapFont font){
        if(label == null) {
            Label.LabelStyle labelStyle = new Label.LabelStyle();
            labelStyle.font = font;
            Label label = new Label(txt, labelStyle);
            label.setFontScale(scale);
            button.add(label);
        }else {
            label.setFontScale(scale);
            label.setText(txt);
        }
        return this;
    }

    public float getHeight(){
        return getActor().getHeight();
    }

    public float getWidth(){
        return getActor().getWidth();
    }

    public Button getActor(){
        return button;
    }
}
