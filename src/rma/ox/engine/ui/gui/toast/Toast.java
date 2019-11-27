package rma.ox.engine.ui.gui.toast;

import rma.ox.engine.ui.gui.utils.DrawableUtils;
import rma.ox.engine.ui.gui.utils.ScreenUtils;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FlushablePool;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Timer;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class Toast {

    public enum Duration{
        SHORT(1f), MEDIUM(2f), LONG(4f), EXTRA_LONG(8f);

        float timeDuration;
        Duration(float timeDuration) {
            this.timeDuration =timeDuration;
        }
    }

    private static ToastMsgPool toastMsgPool = new ToastMsgPool();
    private static ToastDialog toastDialog;
    private static Array<ToastMsg> listToast = new Array<>();

    public static void show(Stage stage, String msg, Duration duration) {
        show(stage, msg, duration.timeDuration);
    }
    public static void show(Stage stage, String msg, float timeDuration) {

        if(stage == null || msg == null || timeDuration <= 0 ) return;

        ToastMsg toastMsg = toastMsgPool.obtain();
        toastMsg.msg = msg;
        toastMsg.stage = stage;
        toastMsg.timeDuration = timeDuration;
        listToast.add(toastMsg);

        if (listToast.size == 1) {
            execute();
        }
    }

    static void execute() {
        ToastMsg toastMsg = listToast.first();
        if (toastDialog == null) {
            toastDialog = new ToastDialog();
        }

        if(toastMsg.stage == null || toastMsg.msg == null || toastMsg.timeDuration <= 0 ) return;

        toastDialog.setText(toastMsg.msg)
                .center(toastMsg.stage).down(toastMsg.stage)
                .show(toastMsg.stage);

        Timer.schedule(task, toastMsg.timeDuration);
    }

    static void releaseObject(){
        toastMsgPool.flush();
    }

    static Timer.Task task = new Timer.Task() {

        @Override
        public void run() {
            toastDialog.hide();
            if (listToast.size > 0) {
                toastMsgPool.free(listToast.first());
                listToast.removeIndex(0);
            }
            if (listToast.size > 0) {
                execute();
            }
        }
    };

    static class ToastMsg implements Pool.Poolable {
        public String msg;
        public Stage stage;
        public float timeDuration;

        @Override
        public void reset() {
            msg = null;
            stage = null;
            timeDuration = -1;
        }
    }

    static class ToastMsgPool extends FlushablePool<ToastMsg> {

        @Override
        protected ToastMsg newObject() {
            return new ToastMsg();
        }
    }

    static class ToastDialog extends Dialog {

        private static BitmapFont font = new BitmapFont();
        private static WindowStyle windowStyle = new Window.WindowStyle(font, Color.WHITE, DrawableUtils.getColorDrawable(Color.BLACK));
        private Label msgLabel;

        public ToastDialog() {
            super("", windowStyle);

            setResizable(false);
            setModal(false);
            setMovable(false);
            setKeepWithinStage(true);

            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
            msgLabel = new Label("", labelStyle);
            getContentTable().add(msgLabel).pad(8, 16, 3, 16);
        }

        public ToastDialog setText(String text) {
            msgLabel.setText(text);
            pack();
            return this;
        }

        public ToastDialog show(Stage stage) {
            show(stage, sequence(Actions.alpha(0), Actions.fadeIn(0.4f, Interpolation.fade)));
            return this;
        }

        public ToastDialog center(Stage stage) {
            setPosition(Math.round((stage.getWidth() - getWidth()) / 2), Math.round((stage.getHeight() - getHeight()) / 2));
            return this;
        }

        public ToastDialog left() {
            setX(0);
            return this;
        }

        public ToastDialog right(Stage stage) {
            setX(Math.round((stage.getWidth() - getWidth())));
            return this;
        }

        public ToastDialog top(Stage stage) {
            setY(Math.round((stage.getHeight() - getHeight())));
            return this;
        }

        public ToastDialog down(Stage stage) {
            setY(ScreenUtils.getHeightPourcent(15, stage.getHeight()));
            return this;
        }

    }
}