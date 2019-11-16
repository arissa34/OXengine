package rma.ox.engine.ui.gui.drawer;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;

import rma.ox.engine.ressource.MyAssetManager;

public abstract class AbsDrawer extends Table {

    protected final MyAssetManager assets = MyAssetManager.get();

    private final float time = 0.2f;
    private boolean isShown = false;
    private int align;

    public AbsDrawer(int align){
        this.align = align;
        top();
    }

    protected Drawable getBckg(String atlasName, String txtName){
        TextureAtlas atlas = assets.get(atlasName, TextureAtlas.class);
        NinePatch onNinePatch = atlas.createPatch(txtName);
        return new NinePatchDrawable(onNinePatch);
    }

    public void show() {
        if(Align.isLeft(align)){
            showLeft();
        }else if(Align.isRight(align)){
            showRight();
        }else if(Align.isBottom(align)){

        }else if(Align.isTop(align)){

        }
    }

    public void hide() {
        if(Align.isLeft(align)){
            hideLeft();
        }else if(Align.isRight(align)){
            hideRight();
        }else if(Align.isBottom(align)){

        }else if(Align.isTop(align)){

        }
    }

    private void showLeft() {
        if(!isShown){
            setPosition(-getWidth(), 0);
            setVisible(true);
            addAction(Actions.sequence(Actions.moveTo(0, 0, time)));
            isShown = true;
        }
    }

    private void hideLeft() {
        if(isShown) {
            setPosition(0, 0);
            addAction(Actions.sequence(Actions.moveTo(-getWidth(), 0, time), completeAction));
        }
    }

    private void showRight() {
        if(!isShown && getStage() != null){
            setPosition(getStage().getWidth(), 0);
            setVisible(true);
            addAction(Actions.sequence(Actions.moveTo(getStage().getWidth() - getWidth(), 0, time)));
            isShown = true;
        }
    }

    private void hideRight() {
        if(isShown) {
            setPosition(getStage().getWidth() - getWidth(), 0);
            addAction(Actions.sequence(Actions.moveTo(getStage().getWidth(), 0, time), completeAction));
        }
    }

    private Action completeAction = new Action(){
        public boolean act( float delta ) {
            isShown = false;
            setVisible(false);
            return true;
        }
    };

    private void forceHide(){
        setPosition(-getWidth(), 0);
        isShown = false;
    }

    @Override
    public void layout() {
        super.layout();
        forceHide();
    }
}
