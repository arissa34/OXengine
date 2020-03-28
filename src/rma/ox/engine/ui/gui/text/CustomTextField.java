package rma.ox.engine.ui.gui.text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public class CustomTextField {

    private final TextField textField;

    public CustomTextField(Drawable background, Drawable focusedBackground, BitmapFont font){
        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.font = font;
        style.background = background;
        style.focusedBackground = focusedBackground;
        textField = new TextField("", style);
    }

    public CustomTextField(TextureAtlas atlas, String pathNotSelected, String pathSelected, String cursor, BitmapFont font, boolean isNinePatch){
        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        if(!isNinePatch){
            style.background = new TextureRegionDrawable(atlas.findRegion(pathNotSelected));;
            style.focusedBackground = new TextureRegionDrawable(atlas.findRegion(pathSelected));;
        }else{
            NinePatch noSelectNinePatch = atlas.createPatch(pathNotSelected);
            NinePatchDrawable noSelectPatchDrawable = new NinePatchDrawable(noSelectNinePatch);
            style.background = noSelectPatchDrawable;

            NinePatch selectNinePatch = atlas.createPatch(pathSelected);
            NinePatchDrawable selectPatchDrawable = new NinePatchDrawable(selectNinePatch);
            style.focusedBackground = selectPatchDrawable;

        }
        style.cursor = new TextureRegionDrawable(atlas.findRegion(cursor));
        style.font = font;
        style.fontColor = Color.WHITE;
        style.focusedFontColor = Color.WHITE;
        textField = new TextField("", style);
    }

    public String getText(){
        return textField.getText();
    }

    public TextField getActor(){
        return textField;
    }

    public void setHint(String hint){
        textField.setMessageText(hint);
    }

    public void setPasswordMode (boolean passwordMode) {
        textField.setPasswordMode(passwordMode);
        textField.setPasswordCharacter('*');
    }

    public void addListener(TextField.TextFieldListener listener){
        textField.setTextFieldListener(listener);
    }
}
