package rma.ox.engine.camera.controler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;

import rma.ox.engine.camera.GhostCamera;
import rma.ox.engine.settings.Config;
import rma.ox.engine.utils.Logx;

public abstract class AbsController implements GestureDetector.GestureListener, InputProcessor {

    protected static Vector3 tmp = new Vector3();
    protected static Vector3 tmp2 = new Vector3();

    protected GhostCamera camera;

    private GestureDetector gestureListener;

    public AbsController(GhostCamera camera){
        this.camera = camera;
    }

    public abstract void update(float delta);

    public void enable(){
        InputMultiplexer inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
        if(inputMultiplexer == null){
            Gdx.input.setInputProcessor(inputMultiplexer = new InputMultiplexer());
        }
        if(Config.isDesktop()) {
            inputMultiplexer.addProcessor(this);
        }else{
            if(gestureListener == null){
                gestureListener = new GestureDetector(this);
            }
            inputMultiplexer.addProcessor(gestureListener);
        }
    }

    public void disable(){
        InputMultiplexer inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
        if(inputMultiplexer != null){
            if(Config.isDesktop()) {
                inputMultiplexer.removeProcessor(this);
            }else if(!Config.isDesktop() && gestureListener != null){
                inputMultiplexer.removeProcessor(gestureListener);
            }
        }
    }

}
