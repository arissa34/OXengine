package rma.ox.engine.ui.gui.dialog;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;

import rma.ox.engine.ressource.MyAssetManager;
import rma.ox.engine.ui.gui.button.CustomButton;
import rma.ox.engine.ui.gui.utils.LabelUtils;
import rma.ox.engine.utils.Logx;

public class AbsDialog<D extends AbsDialog> extends Dialog {

    private static final String TAG = AbsDialog.class.getSimpleName();

    private final MyAssetManager assets = MyAssetManager.get();
    protected final Stage stage;
    private CustomButton validBtn;
    protected CustomButton cancelBtn;
    protected Container titleContenair;
    private Label labelTitle;
    private Label labelText;
    protected Cell<Container> cellTitle;
    protected Cell<ScrollPane> cellScroll;
    protected Cell<Button> cellClose;
    protected Cell<Button> cellCancel;
    protected Cell<Button> cellValid;
    protected ScrollPane scroll;
    protected boolean isShowing;

    public AbsDialog(Stage stage, BitmapFont font) {
        super("", new WindowStyle(font, Color.WHITE, null));
        this.stage = stage;
        getTitleTable().clearChildren();
        getContentTable().clearChildren();
        getButtonTable().clearChildren();
    }

    public AbsDialog(Stage stage, WindowStyle windowStyle) {
        super("", windowStyle);
        this.stage = stage;
        getTitleTable().clearChildren();
        getContentTable().clearChildren();
        getButtonTable().clearChildren();
    }

    public D addTitle(String title, BitmapFont font) {
        return addTitle(title, font, Align.center);
    }

    public D addTitle(String title, BitmapFont font, int align) {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelTitle = new Label(title, labelStyle);
        labelTitle.setAlignment(align);

        titleContenair = new Container(labelTitle);
        titleContenair.fill(false);

        cellTitle = getTitleTable().add(titleContenair);

        return (D) this;
    }

    public D setTitle(String title) {
        if (labelTitle != null) {
            labelTitle.setText(title);
            pack();
        }
        return (D) this;
    }

    public D addCloseBtn(CustomButton closeBtn){
        //CustomButton closeBtn = new CustomButton(LevelAssets.popup_btn_close_n, LevelAssets.popup_btn_close_f);
        cellClose = getTitleTable().add(closeBtn.getActor()).right();
        closeBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                hide();
            }
        });
        return (D) this;
    }

    public D setNineDrawableBckgTitle(String atlasName, String bckgName) {
        if(titleContenair == null){
            Logx.d(this.getClass(),"titleContenair is null");
            return (D) this;
        }
        TextureAtlas atlas = assets.get(atlasName, TextureAtlas.class);
        NinePatch onNinePatch = atlas.createPatch(bckgName);
        NinePatchDrawable ninePatchDrawable = new NinePatchDrawable(onNinePatch);
        titleContenair.setBackground(ninePatchDrawable);
        return (D) this;
    }

    public D addText(String text, BitmapFont font) {
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        labelText = new Label(text, labelStyle);
        labelText.setWrap(true);
        scroll = new ScrollPane(labelText);
        scroll.setForceScroll(false, false);
        cellScroll = getContentTable().add(scroll).prefWidth(LabelUtils.getTexttWidth(labelText)).growX();
        return (D) this;
    }

    public D setText(String text) {
        if (labelText != null) {
            labelText.setText(text);
            float rigth = cellScroll.getPadRight();
            float left = cellScroll.getPadLeft();
            float top = cellScroll.getPadTop();
            float bottom = cellScroll.getPadBottom();
            getContentTable().clearChildren();
            cellScroll = getContentTable().add(scroll).prefWidth(LabelUtils.getTexttWidth(labelText))
                     .pad(top, left, bottom, rigth)
                    .growX();
            pack();
        }
        return (D) this;
    }

    public D setNineDrawableBckg(String atlasName, String bckgName) {

        TextureAtlas atlas = assets.get(atlasName, TextureAtlas.class);
        NinePatch onNinePatch = atlas.createPatch(bckgName);
        NinePatchDrawable ninePatchDrawable = new NinePatchDrawable(onNinePatch);
        setBackground(ninePatchDrawable);

        return (D) this;
    }

    public D setNineDrawableBckg(TextureAtlas atlas, String bckgName) {
        NinePatch onNinePatch = atlas.createPatch(bckgName);
        NinePatchDrawable ninePatchDrawable = new NinePatchDrawable(onNinePatch);
        setBackground(ninePatchDrawable);

        return (D) this;
    }

    public D addCancelBtn(CustomButton cancelBtn) {
        this.cancelBtn = cancelBtn;
        cellCancel = getButtonTable().add(this.cancelBtn.button);
        return (D) this;
    }

    public D addCancelBtn(String textBtn, String atlasName, String btnNopress, String btnPress, String btnDisable, BitmapFont font, ClickListener listener) {
        cancelBtn = new CustomButton(
                atlasName,
                btnNopress,
                btnPress,
                btnDisable
        )
                .setText(textBtn, font)
                .addListener(listener);
        cellCancel = getButtonTable().add(cancelBtn.button);
        return (D) this;
    }

    public D addValidateBtn(CustomButton validBtn) {
        this.validBtn = validBtn;
        cellValid = getButtonTable().add(this.validBtn.button);
        return (D) this;
    }

    public D addValidateBtn(String textBtn, String atlasName, String btnNopress, String btnPress, String btnDisable, BitmapFont font, ClickListener listener) {
        validBtn = new CustomButton(
                atlasName,
                btnNopress,
                btnPress,
                btnDisable
        )
                .setText(textBtn, font)
                .addListener(listener);
        cellValid = getButtonTable().add(validBtn.button);
        return (D) this;
    }

    public D addDebug() {
        setDebug(true);
        return (D) this;
    }

    public float getCenterX() {
        return Math.round((stage.getWidth() - getWidth()) / 2);
    }

    public float getCenterY() {
        return Math.round((stage.getHeight() - getHeight()) / 2);
    }

    public D center() {
        setPosition(Math.round((stage.getWidth() - getWidth()) / 2), Math.round((stage.getHeight() - getHeight()) / 2));
        return (D) this;
    }

    public D left() {
        setX(0);
        return (D) this;
    }

    public D right() {
        setX(Math.round((stage.getWidth() - getWidth())));
        return (D) this;
    }

    public D top() {
        setY(Math.round((stage.getHeight() - getHeight())));
        return (D) this;
    }

    public D down() {
        setY(0);
        return (D) this;
    }
/*
    public D show() {
        setPosition(getCenterX(), getCenterY());
        show(stage, Actions.sequence());
        isShowing = true;
        return (D) this;
    }
*/
    public void hide() {
        super.hide(Actions.sequence());
        isShowing = false;
    }

    public D setDialogModal(boolean modal) {
        setModal(modal);
        return (D) this;
    }

    public D setDialogMovable(boolean movable) {
        setMovable(movable);
        return (D) this;
    }

    public D setDialogResizable(boolean resizable) {
        setResizable(resizable);
        return (D) this;
    }

    public D setMinSize(float width, float height) {
        getBackground().setMinWidth(width);
        getBackground().setMinHeight(height);
        return (D) this;
    }

    public D setMinWidth(float width) {
        getBackground().setMinWidth(width);
        return (D) this;
    }

    public D setMinHeight(float height) {
        getBackground().setMinHeight(height);
        return (D) this;
    }

    @Override
    public float getPrefWidth() {
        if (getWidth() > stage.getWidth()) {
            return stage.getWidth() - 20;
        } else {
            return super.getPrefWidth();
        }
    }

    @Override
    public float getPrefHeight() {
        if (getHeight() > stage.getHeight()) {
            return stage.getHeight() - 10;
        } else {
            return super.getPrefHeight();
        }
    }

}
