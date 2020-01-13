package rma.ox.engine.ui.gui.button;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

import rma.ox.engine.ressource.MyAssetManager;
import rma.ox.engine.ui.gui.utils.LabelUtils;
import rma.ox.engine.utils.Logx;


public class CustomCheckBox {

    private CheckBox checkBox;
    private MyAssetManager mAssets;
    private InputListener listener;

    public CustomCheckBox(String atlasName, String pathOn, String pathOff, String pathDisable) {
        mAssets = MyAssetManager.get();

        CheckBox.CheckBoxStyle style = new CheckBox.CheckBoxStyle();

        TextureAtlas atlas = mAssets.get(atlasName, TextureAtlas.class);

        if(atlas.findRegion(pathOn).splits != null) {
            NinePatch upNinePatch = atlas.createPatch(pathOn);
            NinePatchDrawable upPatchDrawable = new NinePatchDrawable(upNinePatch);
            style.checkboxOn  = upPatchDrawable;
        }else{
            style.checkboxOn  = new TextureRegionDrawable(new TextureRegion(atlas.createSprite(pathOn)));
        }

        if(atlas.findRegion(pathOff).splits != null) {
            NinePatch downNinePatch = atlas.createPatch(pathOff);
            NinePatchDrawable downPatchDrawable = new NinePatchDrawable(downNinePatch);
            style.checkboxOff = downPatchDrawable;
        }else {
            style.checkboxOff = new TextureRegionDrawable(new TextureRegion(atlas.createSprite(pathOff)));
        }

        if(atlas.findRegion(pathDisable).splits != null) {
            NinePatch disableNinePatch = atlas.createPatch(pathDisable);
            NinePatchDrawable disablePatchDrawable = new NinePatchDrawable(disableNinePatch);
            style.checkboxOffDisabled = disablePatchDrawable;
            style.checkboxOnDisabled = style.checkboxOffDisabled;
        }else {
            style.checkboxOffDisabled = new TextureRegionDrawable(new TextureRegion(atlas.createSprite(pathDisable)));
            style.checkboxOnDisabled = style.checkboxOffDisabled;
        }
        style.font = new BitmapFont();

        checkBox = new CheckBox("", style);
        checkBox.getImage().setScaling(Scaling.stretch);

    }
/*
    public CustomCheckBox(String pathOn, String pathOff, String pathOver, String pathDisable) {
        mAssets = MyAssetManager.get();
        Texture textureOn = mAssets.get(pathOn, Texture.class);
        Texture textureOff = mAssets.get(pathOff, Texture.class);
        Texture textureOver = mAssets.get(pathOver, Texture.class);
        Texture textureDisable = mAssets.get(pathDisable, Texture.class);
        CheckBox.CheckBoxStyle style = new CheckBox.CheckBoxStyle();
        style.font = new BitmapFont();
        style.checkboxOn = new TextureRegionDrawable(new TextureRegion(textureOn));
        style.checkboxOff = new TextureRegionDrawable(new TextureRegion(textureOff));
        style.checkboxOver = new TextureRegionDrawable(new TextureRegion(textureOver));
        style.checkboxOffDisabled = new TextureRegionDrawable(new TextureRegion(textureDisable));
        style.checkboxOnDisabled = new TextureRegionDrawable(new TextureRegion(textureDisable));
        checkBox = new CheckBox("", style);
        checkBox.getImage().setScaling(Scaling.fit);
    }
*/
    public CustomCheckBox(String pathOn, String pathOff, String pathDisable) {
        mAssets = MyAssetManager.get();
        Texture textureOn = mAssets.get(pathOn, Texture.class);
        Texture textureOff = mAssets.get(pathOff, Texture.class);
        Texture textureDisable = mAssets.get(pathDisable, Texture.class);
        CheckBox.CheckBoxStyle style = new CheckBox.CheckBoxStyle();
        style.font = new BitmapFont();
        style.checkboxOn = new TextureRegionDrawable(new TextureRegion(textureOn));
        style.checkboxOff = new TextureRegionDrawable(new TextureRegion(textureOff));
        style.checkboxOffDisabled = new TextureRegionDrawable(new TextureRegion(textureDisable));
        style.checkboxOnDisabled = new TextureRegionDrawable(new TextureRegion(textureDisable));
        checkBox = new CheckBox("", style);
        checkBox.getImage().setScaling(Scaling.fit);
    }

    public CustomCheckBox(TextureAtlas atlas, String textureUpString, String textureDownString, BitmapFont font, boolean isNinePatch){

        CheckBox.CheckBoxStyle style = new CheckBox.CheckBoxStyle();
        style.font = font;
        if(!isNinePatch) {
            style.checkboxOn = new TextureRegionDrawable(atlas.findRegion(textureUpString));
            style.checkboxOff = new TextureRegionDrawable(atlas.findRegion(textureDownString));
        }else{
            NinePatch onNinePatch = atlas.createPatch(textureUpString);
            NinePatchDrawable onPatchDrawable = new NinePatchDrawable(onNinePatch);
            style.checkboxOn = onPatchDrawable;
            NinePatch offNinePatch = atlas.createPatch(textureDownString);
            NinePatchDrawable offPatchDrawable = new NinePatchDrawable(offNinePatch);
            style.checkboxOff = offPatchDrawable;
        }
        checkBox = new CheckBox("", style);
        checkBox.getImage().setScaling(Scaling.fit);
        setChecked(false);
    }

    public CustomCheckBox(TextureAtlas atlas, String textureUpString, String textureDownString, String textureDisableString, BitmapFont font, boolean isNinePatch){

        CheckBox.CheckBoxStyle style = new CheckBox.CheckBoxStyle();
        style.font = font;
        if(!isNinePatch) {
            style.checkboxOn = new TextureRegionDrawable(atlas.findRegion(textureUpString));
            style.checkboxOff = new TextureRegionDrawable(atlas.findRegion(textureDownString));
            style.checkboxOffDisabled = new TextureRegionDrawable(atlas.findRegion(textureDisableString));
            style.checkboxOnDisabled = new TextureRegionDrawable(atlas.findRegion(textureDisableString));
        }else{
            NinePatch onNinePatch = atlas.createPatch(textureUpString);
            NinePatchDrawable onPatchDrawable = new NinePatchDrawable(onNinePatch);
            style.checkboxOn = onPatchDrawable;
            NinePatch offNinePatch = atlas.createPatch(textureDownString);
            NinePatchDrawable offPatchDrawable = new NinePatchDrawable(offNinePatch);
            style.checkboxOff = offPatchDrawable;
            NinePatch disableNinePatch = atlas.createPatch(textureDownString);
            NinePatchDrawable disablePatchDrawable = new NinePatchDrawable(disableNinePatch);
            style.checkboxOffDisabled = disablePatchDrawable;
            style.checkboxOnDisabled = style.checkboxOffDisabled;
        }
        checkBox = new CheckBox("", style);
        checkBox.getImage().setScaling(Scaling.stretch);
        setChecked(false);
    }

    public CustomCheckBox(String pathOn, String pathOff) {
        mAssets = MyAssetManager.get();
        Texture textureOn = mAssets.get(pathOn, Texture.class);
        Texture textureOff = mAssets.get(pathOff, Texture.class);
        CheckBox.CheckBoxStyle style = new CheckBox.CheckBoxStyle();
        style.font = new BitmapFont();
        style.checkboxOn = new TextureRegionDrawable(new TextureRegion(textureOn));
        style.checkboxOff = new TextureRegionDrawable(new TextureRegion(textureOff));
        checkBox = new CheckBox("", style);
        checkBox.getImage().setScaling(Scaling.fit);
    }

    public CustomCheckBox setChecked(boolean checked) {
        checkBox.setChecked(!checked);
        return this;
    }

    public boolean isChecked(){
        return !checkBox.isChecked(); // FIXME WTF ??!!!
    }

    public CustomCheckBox setCoord(float x, float y){
        checkBox.setX(x);
        checkBox.setY(y);
        return this;
    }

    public CustomCheckBox setSize(float width, float height) {
        checkBox.setWidth(width);
        checkBox.setHeight(height);
        return this;
    }

    public CustomCheckBox addListener(ChangeListener listener) {
        checkBox.addListener(listener);
        return this;
    }

    public CustomCheckBox addListener(InputListener listener){
        if(checkBox.isDisabled()) return this;
        checkBox.addListener(listener);
        this.listener = listener;
        return this;
    }

    public CustomCheckBox setDisable(boolean disable) {
        checkBox.setDisabled(disable);
        return this;
    }

    public CustomCheckBox putInStage(Stage stage) {
        stage.addActor(checkBox);
        return this;
    }

    public InputListener getListener() {
        return listener;
    }

    public CustomCheckBox setText(CharSequence txt){
        checkBox.getLabel().setText(txt);
        Stack stack = new Stack();
        stack.add(new Container(checkBox.getChildren().get(0)).fillX());
        stack.add(new Container(checkBox.getLabel()).padLeft(10).left());
        checkBox.clearChildren();
        checkBox.add(stack).width(checkBox.getImage().getWidth() + LabelUtils.getTexttWidth(checkBox.getLabel()));
        return this;
    }

    public CheckBox getActor() {
        return checkBox;
    }
}
