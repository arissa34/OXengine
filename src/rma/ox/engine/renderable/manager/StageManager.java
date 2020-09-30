package rma.ox.engine.renderable.manager;

import com.badlogic.gdx.utils.Array;

import rma.ox.engine.ui.gui.notification.InAppNotificationTable;
import rma.ox.engine.ui.gui.stage.AbsStage;

public class StageManager {

    private static StageManager instance;

    public static StageManager get() {
        if (instance == null) instance = new StageManager();
        return instance;
    }

    /*******************************/

    private AbsStage mainStage;
    private InAppNotificationTable notifTable = new InAppNotificationTable();
    public Array<AbsStage> listStage;

    public StageManager() {
        listStage = new Array<>();
    }

    public void addStage(AbsStage stage){
        if(!listStage.contains(stage, true))
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
            listStage.get(i).getViewport().apply();
            listStage.get(i).render();
        }
    }

    public AbsStage getMainStage() {
        return mainStage;
    }

    public void setMainStage(AbsStage mainStage) {
        this.mainStage = mainStage;
    }

    public InAppNotificationTable getNotifTable() {
        return notifTable;
    }
}
