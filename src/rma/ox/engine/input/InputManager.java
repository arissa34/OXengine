package rma.ox.engine.input;

import com.badlogic.gdx.utils.ArrayMap;

import rma.ox.engine.utils.Logx;

public class InputManager {

    private static InputManager instance;

    public static InputManager get() {
        if (instance == null) instance = new InputManager();
        return instance;
    }

    /*******************************/

    private AbsClickableManager currentClickableManager;
    private ArrayMap<String, AbsClickableManager> listClickable = new ArrayMap<>();

    public void addClickableManager(AbsClickableManager absClickableManager){
        //Logx.e("type key : "+absClickableManager.getClass().getSimpleName());
        if(!listClickable.containsKey(absClickableManager.getClass().getSimpleName())){
            listClickable.put(absClickableManager.getClass().getSimpleName(), absClickableManager);
        }
    }

    public void removeClickableManager(AbsClickableManager absClickableManager){
        listClickable.removeKey(absClickableManager.getClass().getSimpleName());
    }

    public <T> void removeClickableManager(Class<T> type){
        listClickable.removeKey(type.getSimpleName());
    }

    public <T> void setCurrentClickableManager(Class<T> type){
        if(!listClickable.containsKey(type.getSimpleName())) return;
        if(currentClickableManager != null)
            currentClickableManager.dettach();
        currentClickableManager = listClickable.get(type.getSimpleName());
        currentClickableManager.attach();
    }

    public AbsClickableManager getCurrentClickableManager() {
        return currentClickableManager;
    }

    public <T> T getClickableManager(Class<T> type){
        return (T) listClickable.get(type.getSimpleName());
    }

    public void disable(){
        if(currentClickableManager == null) return;
        currentClickableManager.dettach();
        currentClickableManager = null;
    }

    public void clearAll(){
        for(int i =0; i < listClickable.size; i++){
            listClickable.getValueAt(i).clear();
        }
        listClickable.clear();
    }
}
