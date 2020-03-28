package rma.ox.engine.ui.dev;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import rma.ox.engine.ui.gui.utils.DrawableUtils;
import rma.ox.engine.ui.gui.utils.LabelUtils;

public abstract class AbsDevMenu extends Dialog {

    protected BitmapFont bitmapFont;
    protected Button btnClose;
    protected Stage stage;
    protected boolean isShowing;

    public AbsDevMenu(Stage stage, String title, BitmapFont font) {
        super(title , new Window.WindowStyle(font, Color.WHITE, DrawableUtils.getColorDrawable( new Color(0.2f, 0.5f, 0.8f, 0.8f))));

        setMovable(true);
        setModal(false);
        setResizable(false);
        isShowing = false;

        this.stage = stage;

        bitmapFont = font;
        Label.LabelStyle labelStyle = new Label.LabelStyle(bitmapFont, Color.WHITE);
        Button.ButtonStyle btnStyle = new Button.ButtonStyle();
        btnStyle.down = DrawableUtils.getColorDrawable(Color.LIGHT_GRAY);
        btnClose = new Button(btnStyle);
        btnClose.add(new Label("X", labelStyle));
        btnClose.pad(1, 8, 1, 7);

        getTitleTable().add(btnClose);

        getTitleTable().padTop(LabelUtils.getTexttHeight(getTitleLabel())*2).row();
        padLeft(10);
        btnClose.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide(Actions.sequence());
            }
        });
        setTouchable(Touchable.enabled);
    }

    public void show(float x, float y) {
        setPosition(x, y);//
        show(stage, Actions.sequence());
        isShowing = true;
    }

    @Override
    public void hide(Action action) {
        super.hide(action);
        isShowing = false;
    }

    @Override
    public float getPrefWidth() {
        return 300;
    }

    @Override
    public float getPrefHeight() {
        return stage.getHeight() < 500 ? stage.getHeight() - 30 : 500;
    }
}
