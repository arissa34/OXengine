package rma.ox.engine.ui.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import rma.ox.engine.ressource.MyAssetManager;

public abstract class AbsScreenLoader implements Screen {

    protected LoaderListener nextScreen;
    protected float progress;
    protected Game game;

    public AbsScreenLoader(Game game, LoaderListener nextScreen){
        this.game = game;
        this.nextScreen = nextScreen;
    }

    @Override
    public void render(float delta) {
        renderAndProgress(delta);
        update();
    }

    protected abstract void renderAndProgress(float delta);

    //Need to be called in the end of your render(float delta)
    protected void update() {
        if (MyAssetManager.get().update()) {
            nextScreen.loadingFinished(game);
            dispose();
        }
        progress = MyAssetManager.get().getProgress();
    }
}
