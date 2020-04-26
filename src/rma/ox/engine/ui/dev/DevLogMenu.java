package rma.ox.engine.ui.dev;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import rma.ox.engine.core.observer.DataNotify;
import rma.ox.engine.core.observer.Observable;
import rma.ox.engine.core.observer.ObservableState;
import rma.ox.engine.core.observer.Observer;
import rma.ox.engine.ui.gui.utils.DrawableUtils;
import rma.ox.engine.ui.gui.utils.LabelUtils;
import rma.ox.engine.utils.Logx;

public class DevLogMenu extends AbsDevMenu implements Observer {

    private ScrollPane scrollPane;
    private Label logLabel;
    private String logs;

    public DevLogMenu(Stage stage, String title, BitmapFont font) {
        super(stage , title, font);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        logLabel = new Label("" ,labelStyle);
        scrollPane = new ScrollPane(logLabel);
        getContentTable().add(scrollPane).padTop(30).expand().left().bottom();

       // Logx.observable.subscribe(this);
    }

    @Override
    public float getPrefWidth() {
        return stage.getWidth() < 500 ? stage.getWidth() - 30 : 500;
    }

    @Override
    public void show(float x, float y) {
        //logLabel.setText(logs);
        //scrollPane.scrollTo(0, 0, 0, 0);
        super.show(x, y);
    }

    @Override
    public void onSubscribe(Observable observable) {

    }

    @Override
    public void onNotify(ObservableState command, DataNotify dataNotify) {
        //logs = dataNotify.data.toString();
        //if(isShowing){
        //    logLabel.setText(logs);
        //    scrollPane.scrollTo(0, 0, 0, 0);
        //}
    }

    @Override
    public void onUnsubscribe(Observable observable) {

    }
}
