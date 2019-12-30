package rma.ox.engine.ui.dev;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import rma.ox.engine.core.observer.DataNotify;
import rma.ox.engine.core.observer.Observable;
import rma.ox.engine.core.observer.ObservableState;
import rma.ox.engine.core.observer.Observer;
import rma.ox.engine.core.threading.RunnableThread;
import rma.ox.engine.renderable.manager.StageManager;
import rma.ox.engine.settings.Config;
import rma.ox.engine.ui.gui.IGui;
import rma.ox.engine.ui.gui.notification.InAppNotification;
import rma.ox.engine.ui.gui.toast.Toast;
import rma.ox.engine.ui.gui.utils.DrawableUtils;
import rma.ox.engine.ui.gui.utils.LabelUtils;
import rma.ox.engine.utils.Logx;

public class DevBar extends Table implements IGui, Observer {

    private Label.LabelStyle labelStyle;
    private Label fpsLabel;
    private StringBuffer fpsString;
    private Button versionLabel;
    private Button logBtn;
    private Button OxBtn;
    private BitmapFont bitmapFont;

    private DevMainMenu devMainMenu;
    private DevLogMenu devLogMenu;

    private GLProfiler glProfiler;
    private Stage stage;

    public DevBar(Stage stage){
        this.stage = stage;
        initializeUi();
        layoutUi();
        eventsUi();

        glProfiler = new GLProfiler(Gdx.graphics);
        glProfiler.enable();

        Logx.observable.subscribe(this);
    }

    @Override
    public void initializeUi() {

        left();
        setBackground(DrawableUtils.getColorDrawable( new Color(0.2f, 0.5f, 0.8f, 1.8f)));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/ui/skin_dev/Montserrat-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.genMipMaps = true;
        parameter.mono = false;
        parameter.borderStraight = false;
        parameter.kerning = true;
        parameter.borderWidth = 0.9f;
        parameter.shadowOffsetX = 1;
        parameter.shadowOffsetY = 1;
        bitmapFont = generator.generateFont(parameter);
        bitmapFont.getData().markupEnabled = true;

        labelStyle = new Label.LabelStyle(bitmapFont, Color.WHITE);

        fpsString = new StringBuffer();
        fpsLabel = newLabel("FPS : 00");

        devMainMenu = new DevMainMenu(stage, "OXEngine", bitmapFont);
        devLogMenu = new DevLogMenu(stage, "Log", bitmapFont);
    }

    protected Cell adhHorizontalSeparator(){
       return add(new Image(DrawableUtils.getColorDrawable( Color.GOLD))).height(.7f).fillX();
    }

    protected Cell addVerticalSeparator(){
       return add(new Image(DrawableUtils.getColorDrawable(Color.WHITE))).width(1).fillY().pad(4, 10, 4, 5);
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
        adhHorizontalSeparator().expandX().colspan(100).row();
        add(fpsLabel).pad(2, 5, 2, 0).width(LabelUtils.getTexttWidth(fpsLabel));
        addVerticalSeparator();
        add(versionLabel = newBtn(Config.VERSION));
        addVerticalSeparator();
        add(logBtn = newBtn("{ [RED]0[WHITE], [ORANGE]0[WHITE], 0 }"));
        addVerticalSeparator();
        addVerticalSeparator().expandX().right();
        add(OxBtn = newBtn("OX")).padRight(5);
    }


    private int count = 0;
    @Override
    public void eventsUi() {
        fpsLabel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                count++;

                InAppNotification.Builder.setListener(new InAppNotification.Listener() {
                    @Override
                    public void onClick() {
                        Logx.d("Notif onClick");
                    }

                    @Override
                    public void onCancel() {
                        Logx.d("Notif onCancel");
                    }
                }).setHeader("badlogic.jpg", "Click here to download the new version of Clash of Clan "+count, bitmapFont).build().show();
                //InAppNotification.show("badlogic.jpg", "title title title title title title title title title title : "+count, "subTitlesubTitlesubTitlesu bTitlesub Titl esubTit lesubTi tle ubTitle subTitlesubT itle : "+count);
                Toast.show(StageManager.get().getMainStage(), "TEST", Toast.Duration.SHORT);
            }
        });
        versionLabel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                count++;

                InAppNotification.Builder.setHeader("badlogic.jpg", "Header blabla "+count, bitmapFont).setIcon("badlogic.jpg").setTitle("New version !", bitmapFont).setDescription("Click here to download the new version of Clash of Clan", bitmapFont).build().show();
                RunnableThread.get().add(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                //InAppNotification.show("badlogic.jpg", "title : "+count, "subTitlesubTitlesubTitlesu : "+count).setFont(bitmapFont);
                //InAppNotification.Builder.setHeader("badlogic.jpg", "HEADER").setTitle("TITLE "+count, bitmapFont).build().show();

            }
        });
        logBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //count++;
                //InAppNotification.Builder.setIcon("badlogic.jpg").setTitle("TITLE "+count).setDescription("subTitlesubTitlesubTitlesu bTitlesub Titl esubTit lesubTi tle ubTitle subTitlesubT itle", bitmapFont).build().show();
                devLogMenu.show(getPosX(logBtn), getHeight());
            }
        });
        OxBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                devMainMenu.show(getPosX(OxBtn), getHeight());
            }
        });
    }

    private float getPosX(Table actor){
        return actor.getX()-actor.getPadLeft();
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

    @Override
    public void onSubscribe(Observable observable) {

    }

    @Override
    public void onNotify(ObservableState command, DataNotify dataNotify) {
        if(command instanceof Logx.LogState){
            ((Label)logBtn.getChild(0)).setText("{ [RED]"+Logx.errorCount+"[WHITE], [ORANGE]"+Logx.debugCount+"[WHITE], "+Logx.logCount+" }");
        }
    }

    @Override
    public void onUnsubscribe(Observable observable) {

    }
}
