package rma.ox.engine.ui.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntMap;

import rma.ox.engine.utils.Logx;

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
        setScreen(screen, false);
    }

    public void setScreen(AbsScreen screen, boolean shouldDispose){
        if(currentScreen != null){
            currentScreen.unload();
        }
        if(shouldDispose && currentScreen != null){
            currentScreen.dispose();
            listScreen.removeValue(currentScreen, true);
            Logx.e("----- REMOVE SCREEN "+currentScreen.getClass());
        }
        if(!listScreen.contains(screen, true)){
            Logx.e("----- ADD SCREEN "+screen.getClass());
            listScreen.add(screen);
        }
        currentScreen = screen;
        game.setScreen(screen);

        Logx.e("-****************-");
        for(int i = 0; i < listScreen.size; i++){

            Logx.e("-****** SCREEN : "+listScreen.get(i).getClass());
        }
        Logx.e("-****************-");
    }

    @Override
    public void dispose(){
        for(int i = 0; i < listScreen.size; i++){
            listScreen.get(i).dispose();
        }
    }
}
