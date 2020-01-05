package rma.ox.engine.ui.screen;

import com.badlogic.gdx.Game;
import rma.ox.engine.ressource.MyAssetManager;

public abstract class AbsScreenLoader extends AbsScreen {

    protected AbsScreen nextScreen;
    protected float progress;

    public AbsScreenLoader( AbsScreen nextScreen){
        super();
        this.nextScreen = nextScreen;
    }

    @Override
    public void show() {
        initBeforeShow(); 
        progress = 0f;
        nextScreen.initAssets();
    }

    @Override
    public void render(float delta) {
        renderAndProgress(delta);
        update();
    }

    //Need to be called in the end of your render(float delta)
    protected void update() {
        if (MyAssetManager.get().update()) {
            System.gc();
            nextScreen.loadingFinished(game);
            ScreenHelper.get().setScreen(nextScreen);
            nextScreen = null;
        }
        progress = MyAssetManager.get().getProgress();
    }

    protected abstract void initBeforeShow();
    protected abstract void renderAndProgress(float delta);
    protected abstract void initAssets();
}
