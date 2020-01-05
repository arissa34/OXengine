package rma.ox.engine.ui.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import rma.ox.engine.ressource.MyAssetManager;

public abstract class AbsScreen implements Screen {

    protected Game game;

    public AbsScreen(){
        this.game =  ((Game) Gdx.app.getApplicationListener());
    }

    protected abstract void initAssets();
    protected abstract void loadingFinished(Game game);
    protected abstract void unload();
}
