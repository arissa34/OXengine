package rma.ox.engine.input;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.utils.Array;

public abstract class AbsClickableManager implements InputProcessor, GestureDetector.GestureListener {

    protected Array<IClickable> listClickable;
    protected GestureDetector gestureDetector;

    public AbsClickableManager(){
        listClickable = new Array<>();
    }

    public void addObjClickable(IClickable clickable) {
        if(clickable == null) return;
        listClickable.add(clickable);
    }

    public void addObjClickables(Array<IClickable> clickables) {
        for(int i = 0; i < clickables.size; i++){
            listClickable.add(clickables.get(i));
        }
    }

    public void removeObjClickable(IClickable clickable) {
        listClickable.removeValue(clickable, true);
    }

    public void removeObjClickables(Array<IClickable> clickables) {
        for(int i = 0; i < clickables.size; i++){
            listClickable.removeValue(clickables.get(i), true);
        }
    }

    public AbsClickableManager attach(){
        InputMultiplexer inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
        if(Gdx.app.getType() == Application.ApplicationType.Desktop){
            inputMultiplexer.addProcessor(this);
        }else{
            inputMultiplexer.addProcessor(gestureDetector = new GestureDetector(this));
        }
        return this;
    }

    public void dettach(){
        InputMultiplexer inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
        if(Gdx.app.getType() == Application.ApplicationType.Desktop){
            inputMultiplexer.removeProcessor(this);
        }else{
            if(gestureDetector == null) return;
            inputMultiplexer.removeProcessor(gestureDetector);
        }
    }

    public void clear(){
        listClickable.clear();
    }
}
