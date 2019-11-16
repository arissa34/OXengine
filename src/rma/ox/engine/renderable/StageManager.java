package rma.ox.engine.renderable;

import com.badlogic.gdx.utils.Array;

import rma.ox.engine.ui.gui.stage.AbsStage;

public class StageManager {

    private static StageManager instance;

    public static StageManager get() {
        if (instance == null) instance = new StageManager();
        return instance;
    }

    /*******************************/

    public Array<AbsStage> listStage;

    public StageManager() {
        listStage = new Array<>();
    }

    public void addStage(AbsStage stage){
        listStage.add(stage);
    }

    public void removeStage(AbsStage stage){
        listStage.removeValue(stage, true);
    }

    public void clearStage(){
        listStage.clear();
    }

    public void update(float delta) {
        for(int i = 0; i < listStage.size; i++){
            listStage.get(i).update(delta);
        }
    }

    public void render() {
        for(int i = 0; i < listStage.size; i++){
            listStage.get(i).render();
        }
    }
}
