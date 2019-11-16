package rma.ox.engine.ui.screen;

import com.badlogic.gdx.Game;

public interface LoaderListener {

    void initAssets();
    void loadingFinished(Game game);

}
