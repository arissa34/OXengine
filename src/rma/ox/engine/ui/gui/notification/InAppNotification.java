package rma.ox.engine.ui.gui.notification;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.FlushablePool;
import com.badlogic.gdx.utils.Pool;

import rma.ox.engine.pool.FlushableManager;
import rma.ox.engine.renderable.manager.StageManager;
import rma.ox.engine.ressource.MyAssetManager;
import rma.ox.engine.ui.gui.utils.DrawableUtils;
import rma.ox.engine.utils.Logx;
import rma.ox.engine.utils.StringUtils;

public class InAppNotification extends Container implements Pool.Poolable {

    public interface Listener{
        void onClick();
        void onCancel();
    }

    static final FlushablePool<InAppNotification> notifPool = new FlushablePool<InAppNotification>() {
        @Override
        protected InAppNotification newObject() {
            FlushableManager.get().registerUi(this);
            return new InAppNotification();
        }
    };

    private static BitmapFont sfont = new BitmapFont();

    private BitmapFont font;
    private Label.LabelStyle labelStyle;

    private Stack header;
    private Table mainTable;
    private Image topImage;
    private Image icImage;
    private Table vertical;
    private Label headerTitleLabel;
    private Label titleLabel;
    private Label subTitleLabel;

    private TextureRegionDrawable topTxtDrawable;
    private TextureRegion topTxtRegion;
    private TextureRegionDrawable icTxtDrawable;
    private TextureRegion icTxtRegion;

    private Listener listener;

    public InAppNotification() {
        this(sfont);
    }

    public InAppNotification(BitmapFont font) {
        this.font = font;

        pad(InAppNotificationTable.PAD);
        setTouchable(Touchable.enabled);
        setWidth(300);
        left();

        labelStyle = new Label.LabelStyle(this.font, Color.WHITE);
        headerTitleLabel = new Label("header", labelStyle);
        titleLabel = new Label("title", labelStyle);
        subTitleLabel = new Label("subTitle", labelStyle);
        titleLabel.setWrap(true);
        subTitleLabel.setWrap(true);

        topTxtRegion = new TextureRegion();
        icTxtRegion = new TextureRegion();
        topTxtDrawable = new TextureRegionDrawable(topTxtRegion);
        icTxtDrawable = new TextureRegionDrawable(icTxtRegion);
        topImage = new Image(DrawableUtils.getColorDrawable(Color.RED));
        icImage = new Image(DrawableUtils.getColorDrawable(Color.BLUE));

        mainTable = new Table();
        vertical = new Table();
        header = new Stack();
        header.add(new Container<>(topImage).height(80).width(getWidth()));
        header.add(new Container<>(headerTitleLabel).bottom().padBottom(10));
        mainTable.add(header).colspan(2).pad(-5, -10, 10, -10).row();
        mainTable.setBackground(DrawableUtils.getColorDrawable(Color.FOREST));
        mainTable.pad(5, 10, 5, 10);
        setActor(mainTable);

        addListener(new InputListener(){
            float xInit;
            boolean remove = false;

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                xInit = x;
                remove = false;
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                invalidate();
                setX(getX() - (xInit-x));
                float v = Math.abs(getX() - (xInit-x));
                getColor().a = 1 - (v/150);
                if(getColor().a <= 0.15f){
                    remove = true;
                }else {
                    remove = false;
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(remove){
                    removeAndNotify();
                    remove=false;
                }else{
                    setX(0);
                    getColor().a = 1;
                }
            }
        });

        addListener(new ActorGestureListener(){
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                click();
            }
        });
    }

    public InAppNotification setFont(BitmapFont font){
        this.font = font;
        labelStyle.font = font;
        titleLabel.setStyle(labelStyle);
        subTitleLabel.setStyle(labelStyle);
        headerTitleLabel.setStyle(labelStyle);
        return this;
    }

    public InAppNotification init(String iconPath, String title, String subtile) {
        boolean hasHeaderImage = false;
        boolean hasIcImage = false;

        if(iconPath != null && !iconPath.isEmpty()){
            icTxtRegion.setRegion(MyAssetManager.get().get(iconPath, Texture.class));
            icTxtDrawable.setRegion(icTxtRegion);
            icImage.setDrawable(icTxtDrawable);
            mainTable.add(icImage).width(50).height(50).padRight(10).expandY().top();
            hasIcImage = true;
        }
        if(title != null){
            int width = hasIcImage ? 200 : 260;
            titleLabel.setText(StringUtils.capitalize(title));
            vertical.add(titleLabel).width(width).expandX().left().row();
        }
        if(subtile != null){
            int width = hasIcImage ? 200 : 260;
            subTitleLabel.setText(StringUtils.capitalize(subtile));
            vertical.add(subTitleLabel).width(width).expand().left().row();
        }
        if(title != null || subtile != null){
            mainTable.add(vertical).fillX().expandX().left();
        }
        return this;
    }

    public InAppNotification setListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    private void click() {
        removeNotif();
        if(listener != null){
            listener.onClick();
        }
        Logx.d("Notif clicked");
    }

    private void removeNotif(){
        StageManager.get().getNotifTable().removeNotif(
                this
        );
        notifPool.free(this);
    }

    private void removeAndNotify(){
        removeNotif();
        if(listener != null){
            listener.onCancel();
        }
    }

    //TODO BUILDER

    public static InAppNotification show(String iconPath, String title, String subtile){
        InAppNotification notif;
        StageManager.get().getNotifTable().addNotif(
                notif = notifPool.obtain().init(iconPath, title, subtile)
        );
        return notif;
    }

    @Override
    public void reset() {
        setFont(sfont);
        topImage.setDrawable(null);
        topTxtDrawable.setRegion(null);
        icImage.setDrawable(null);
        icTxtDrawable.setRegion(null);
        mainTable.clearChildren();
        vertical.clearChildren();
        setX(getPrefWidth());
        getColor().a = 1;
        listener = null;
    }
}

