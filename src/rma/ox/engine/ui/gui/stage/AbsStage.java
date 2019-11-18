package rma.ox.engine.ui.gui.stage;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

import rma.ox.engine.renderable.manager.StageManager;
import rma.ox.engine.ui.gui.IGui;

public abstract class AbsStage<T extends AbsStage> extends Stage implements IGui {

    public AbsStage(){
        super();
        initializeUi();
        layoutUi();
        eventsUi();
    }

    public AbsStage(Viewport viewport){
        super(viewport);
        initializeUi();
        layoutUi();
        eventsUi();
    }

    public abstract void initializeUi();
    public abstract void layoutUi();
    public abstract void eventsUi();
    public abstract void update(float delta);

    public void render() {
        act();
        draw();
    }

    public T registerForRender(){
        StageManager.get().addStage(this);
        return (T) this;
    }

    public T unregisterForRender(){
        StageManager.get().removeStage(this);
        return (T) this;
    }
}
