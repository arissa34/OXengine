package rma.ox.engine.ui.gui.bar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import rma.ox.engine.settings.Config;
import rma.ox.engine.ui.gui.IGui;
import rma.ox.engine.ui.gui.utils.DrawableUtils;
import rma.ox.engine.ui.gui.windows.menu.DevMainMenu;
import rma.ox.engine.utils.Logx;

public class DevBar extends Table implements IGui {

    private Label.LabelStyle labelStyle;
    private Label fpsLabel;
    private Label versionLabel;
    private StringBuffer fpsString;
    private Button OxBtn;
    private BitmapFont bitmapFont;

    private DevMainMenu devMainMenu;

    private GLProfiler glProfiler;
    private Stage stage;

    public DevBar(Stage stage){
        this.stage = stage;
        initializeUi();
        layoutUi();
        eventsUi();

        glProfiler = new GLProfiler(Gdx.graphics);
        glProfiler.enable();
    }

    @Override
    public void initializeUi() {

        left(); pad(2, 5, 2, 5);
        setBackground(DrawableUtils.getColorDrawable( new Color(0.2f, 0.5f, 0.8f, 0.8f)));

        bitmapFont = new BitmapFont();
        labelStyle = new Label.LabelStyle(bitmapFont, Color.WHITE);

        fpsString = new StringBuffer();
        fpsLabel = newLabel("");
        versionLabel = newLabel(Config.VERSION);

        devMainMenu = new DevMainMenu(stage, "OXEngine");
    }

    protected Cell addVerticalSeparator(){
       return add(new Image(DrawableUtils.getColorDrawable(Color.WHITE))).width(1).fillY().pad(0, 10, 0, 5);
    }

    private Label newLabel(String txt){
        return new Label(txt, labelStyle);
    }

    protected Button newBtn(String txt){
        Button.ButtonStyle btnStyle = new Button.ButtonStyle();
        btnStyle.down = DrawableUtils.getColorDrawable(Color.LIGHT_GRAY);
        Button btn = new Button(btnStyle);
        btn.add(new Label(txt, labelStyle));
        btn.pad(0, 5, 0, 5);
        return btn;
    }

    @Override
    public void layoutUi() {
        add(fpsLabel);
        addVerticalSeparator();
        add(versionLabel);
        addVerticalSeparator();
        addVerticalSeparator().expandX().right();
        add(OxBtn = newBtn("OX"));
    }

    @Override
    public void eventsUi() {
        OxBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                devMainMenu.show(getPosX(OxBtn), getHeight());
            }
        });
    }

    private float getPosX(Table actor){
        return actor.getX()+actor.getWidth()+actor.getPadLeft()+actor.getPadRight();
    }

    @Override
    public void update(float delta) {
        int fps = Gdx.graphics.getFramesPerSecond();
        if (fps > 30) {
            fpsLabel.setColor(Color.GREEN);
        } else {
            fpsLabel.setColor(Color.RED);
        }
        fpsString.setLength(0);
        fpsString.append("FPS : ");
        fpsString.append(fps);
        fpsLabel.setText(fpsString);

        glProfiler.reset();
    }
}
