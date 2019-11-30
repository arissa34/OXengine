package rma.ox.engine.ui.gui.notification;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.FlushablePool;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Scaling;

import rma.ox.engine.pool.FlushableManager;
import rma.ox.engine.renderable.manager.StageManager;
import rma.ox.engine.ressource.MyAssetManager;
import rma.ox.engine.ui.gui.utils.DrawableUtils;
import rma.ox.engine.ui.utils.StudioTexture;
import rma.ox.engine.utils.Logx;
import rma.ox.engine.utils.StringUtils;

public class InAppNotification extends Container implements Pool.Poolable {

    public interface Listener {
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
    private static Image
            fadingImage = new Image(new TextureRegionDrawable(new TextureRegion(
            StudioTexture.faddingTexture(300, 100, Color.BLACK, Color.CLEAR)
    )));

    private BitmapFont font;
    private Label.LabelStyle headerTitlestyle;
    private Label.LabelStyle titlestyle;
    private Label.LabelStyle descriptionstyle;

    private Stack header;
    private Table mainTable;
    private Image topFadingImage;
    private Image topImage;
    private Image icImage;
    private Table vertical;
    private Label headerTitleLabel;
    private Label titleLabel;
    private Label descriptionLabel;

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

        headerTitlestyle = new Label.LabelStyle(this.font, Color.WHITE);
        titlestyle = new Label.LabelStyle(this.font, Color.WHITE);
        descriptionstyle = new Label.LabelStyle(this.font, Color.WHITE);

        headerTitleLabel = new Label("", headerTitlestyle);
        titleLabel = new Label("", titlestyle);
        descriptionLabel = new Label("", descriptionstyle);

        headerTitleLabel.setWrap(true);
        headerTitleLabel.setAlignment(Align.center | Align.bottom );
        titleLabel.setWrap(true);
        descriptionLabel.setWrap(true);

        topTxtRegion = new TextureRegion();
        icTxtRegion = new TextureRegion();
        topTxtDrawable = new TextureRegionDrawable(topTxtRegion);
        icTxtDrawable = new TextureRegionDrawable(icTxtRegion);

        topImage = new Image();
        icImage = new Image();
        topImage.setScaling(Scaling.stretch);

        mainTable = new Table();
        vertical = new Table();
        header = new Stack();
        header.add(new Container<>(topImage).fill());
        header.add(new Container<>(topFadingImage = new Image(fadingImage.getDrawable())).fill());
        header.add(new Container<>(headerTitleLabel).fill().center().pad(10, 8, 10, 8));

        mainTable.setBackground(DrawableUtils.getColorDrawable(Color.FOREST));
        setActor(mainTable);

        addListener(new InputListener() {
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
                setX(getX() - (xInit - x));
                float v = Math.abs(getX() - (xInit - x));
                getColor().a = 1 - (v / 150);
                if (getColor().a <= 0.15f) {
                    remove = true;
                } else {
                    remove = false;
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (remove) {
                    removeAndNotify();
                    remove = false;
                } else {
                    setX(0);
                    getColor().a = 1;
                }
            }
        });

        addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                click();
            }
        });
    }

    private InAppNotification setFont(BitmapFont font) {
        this.font = font;
        headerTitlestyle.font = font;
        titlestyle.font = font;
        descriptionstyle.font = font;
        titleLabel.setStyle(headerTitlestyle);
        descriptionLabel.setStyle(titlestyle);
        headerTitleLabel.setStyle(descriptionstyle);
        return this;
    }

    protected InAppNotification init() {
        if(Builder.hasHeader){
            topTxtRegion.setRegion(MyAssetManager.get().get(Builder.headerImgPath, Texture.class));
            topTxtDrawable.setRegion(topTxtRegion);
            topImage.setDrawable(topTxtDrawable);
            topFadingImage.setVisible(Builder.headerTitle != null);

            if (Builder.headerTitle != null ) {
                headerTitlestyle.font = Builder.headerTitleFont;
                headerTitleLabel.setStyle(headerTitlestyle);
                headerTitleLabel.setText(StringUtils.capitalize(Builder.headerTitle));
            }
            Cell cell = mainTable.add(header);
            if(Builder.hasIcon){
                cell.colspan(2);
            }
            if (Builder.title != null || Builder.description != null) {
                cell.pad(-5, -10, 10, -10);
            }
            cell.row();
        }
        if(Builder.hasIcon){
            icTxtRegion.setRegion(MyAssetManager.get().get(Builder.iconPath, Texture.class));
            icTxtDrawable.setRegion(icTxtRegion);
            icImage.setDrawable(icTxtDrawable);
            mainTable.add(icImage).width(50).height(50).top().padRight(10);
        }
        if (Builder.title != null && !Builder.title.isEmpty()) {
            int width = Builder.hasIcon ? 200 : 260;
            titlestyle.font = Builder.titleFont;
            titleLabel.setStyle(titlestyle);
            titleLabel.setText(StringUtils.capitalize(Builder.title));
            vertical.add(titleLabel).width(width).expandX().left().row();
        }
        if (Builder.description != null && !Builder.description.isEmpty()) {
            int width = Builder.hasIcon ? 200 : 260;
            descriptionstyle.font = Builder.descriptionFont;
            descriptionLabel.setStyle(descriptionstyle);
            descriptionLabel.setText(StringUtils.capitalize(Builder.description));
            vertical.add(descriptionLabel).width(width).expand().left().row();
        }
        if (Builder.title != null || Builder.description != null) {
            mainTable.add(vertical).fillX().expandX().left();
            mainTable.pad(5, 10, 5, 10);
        }

        setListener(Builder.listener);

        return this;
    }

    private InAppNotification setListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    private void click() {
        if (listener != null) {
            listener.onClick();
        }
        removeNotif();
    }

    private void removeNotif() {
        StageManager.get().getNotifTable().removeNotif(
                this
        );
        notifPool.free(this);
    }

    private void removeAndNotify() {
        if (listener != null) {
            listener.onCancel();
        }
        removeNotif();
    }

    public void show() {
        StageManager.get().getNotifTable().addNotif(
                this
        );
    }

    public static class Builder {
        private static Builder builder = new Builder();

        protected static boolean hasHeader;
        protected static boolean hasIcon;
        protected static String headerImgPath;
        protected static String headerTitle;
        protected static BitmapFont headerTitleFont;
        protected static String iconPath;
        protected static String title;
        protected static BitmapFont titleFont;
        protected static String description;
        protected static BitmapFont descriptionFont;
        protected static Listener listener;

        public static Builder setHeader(String headerImgPath) {
            hasHeader = true;
            Builder.headerImgPath = headerImgPath;
            return builder;
        }

        public static Builder setHeader(String headerImgPath, String headerTitle) {
            return setHeader(headerImgPath, headerTitle, sfont);
        }

        public static Builder setHeader(String headerImgPath, String headerTitle, BitmapFont headerTitleFont) {
            hasHeader = true;
            Builder.headerImgPath = headerImgPath;
            Builder.headerTitle = headerTitle;
            Builder.headerTitleFont = headerTitleFont;
            return builder;
        }

        public static Builder setIcon(String iconPath) {
            hasIcon = true;
            Builder.iconPath = iconPath;
            return builder;
        }

        public static Builder setTitle(String title) {
            return setTitle(title, sfont);
        }

        public static Builder setTitle(String title, BitmapFont titleFont) {
            Builder.title = title;
            Builder.titleFont = titleFont;
            return builder;
        }

        public static Builder setDescription(String description) {
            return setDescription(description, sfont);
        }

        public static Builder setDescription(String description, BitmapFont descriptionFont) {
            Builder.description = description;
            Builder.descriptionFont = descriptionFont;
            return builder;
        }

        public static Builder setListener(Listener listener) {
            Builder.listener = listener;
            return builder;
        }

        public static InAppNotification build() {
            InAppNotification notif = notifPool.obtain().init();
            reset();
            return notif;
        }

        private static void reset() {
            hasHeader = false;
            hasIcon = false;
            headerImgPath = null;
            headerTitle = null;
            headerTitleFont = null;
            iconPath = null;
            title = null;
            titleFont = null;
            description = null;
            descriptionFont = null;
            listener = null;
        }
    }

    @Override
    public void reset() {
        setFont(sfont);
        topImage.setDrawable(null);
        topTxtDrawable.setRegion(null);
        icImage.setDrawable(null);
        icTxtDrawable.setRegion(null);
        mainTable.clearChildren();
        mainTable.pad(0);
        vertical.clearChildren();
        setX(getPrefWidth());
        getColor().a = 1;
        listener = null;
    }
}

