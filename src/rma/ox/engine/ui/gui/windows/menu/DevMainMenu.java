package rma.ox.engine.ui.gui.windows.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import rma.ox.engine.ui.gui.utils.DrawableUtils;

public class DevMainMenu extends Dialog {

    private BitmapFont bitmapFont;
    private Button btnClose;
    private Stage stage;

    public DevMainMenu(Stage stage, String title) {
        super(title , new WindowStyle(new BitmapFont(), Color.WHITE, DrawableUtils.getColorDrawable( new Color(0.2f, 0.5f, 0.8f, 0.8f))));

        setMovable(true);
        setModal(false);
        setResizable(false);

        this.stage = stage;

        bitmapFont = new BitmapFont();
        Label.LabelStyle labelStyle = new Label.LabelStyle(bitmapFont, Color.WHITE);
        Button.ButtonStyle btnStyle = new Button.ButtonStyle();
        btnStyle.down = DrawableUtils.getColorDrawable(Color.LIGHT_GRAY);
        btnClose = new Button(btnStyle);
        btnClose.add(new Label("X", labelStyle));
        btnClose.pad(1, 8, 1, 4);
        getTitleTable().add(btnClose);
        btnClose.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide(Actions.sequence());
            }
        });

    }

    public void show(float x, float y) {
        setPosition(x-getPrefWidth(), y);//
        show(stage, Actions.sequence());
    }

    @Override
    public float getPrefWidth() {
        return 200;
    }
    @Override
    public float getPrefHeight() {
        return stage.getHeight() < 500 ? stage.getHeight() - 30 : 500;
    }
}
