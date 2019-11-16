package rma.ox.engine.ui.gui.stage;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

import rma.ox.engine.renderable.StageManager;

public abstract class AbsStage extends Stage {

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

    public AbsStage registerForRender(){
        StageManager.get().addStage(this);
        return this;
    }

    public AbsStage unregisterForRender(){
        StageManager.get().removeStage(this);
        return this;
    }
}
