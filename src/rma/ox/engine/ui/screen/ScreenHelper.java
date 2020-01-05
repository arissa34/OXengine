package rma.ox.engine.ui.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntMap;

public class ScreenHelper implements Disposable {

    private static ScreenHelper instance;

    public static ScreenHelper get() {
        if (instance == null) instance = new ScreenHelper();
        return instance;
    }

    /*******************************/

    private Game game;
    private AbsScreen currentScreen;
    private Array<AbsScreen> listScreen;

    public ScreenHelper(){
        game = ((Game) Gdx.app.getApplicationListener());
        listScreen = new Array<>();
    }

    public void setScreen(AbsScreen screen){
        if(currentScreen != null){
            currentScreen.unload();
        }
        if(!listScreen.contains(screen, true)){
            listScreen.add(screen);
        }
        currentScreen = screen;
        game.setScreen(screen);
    }

    @Override
    public void dispose(){
        for(int i = 0; i < listScreen.size; i++){
            listScreen.get(i).dispose();
        }
    }
}
