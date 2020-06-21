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

    public interface Listener{
        void onClosed();
    }

    protected final MyAssetManager assets = MyAssetManager.get();

    private final float time = 0.2f;
    private boolean isShown = false;
    private boolean isFirstLaunch = true;
    private int align;
    private Listener listener;

    public AbsDrawer(int align){
        this.align = align;
        top();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    protected Drawable getBckg(String atlasName, String txtName){
        TextureAtlas atlas = assets.get(atlasName, TextureAtlas.class);
        NinePatch onNinePatch = atlas.createPatch(txtName);
        return new NinePatchDrawable(onNinePatch);
    }

    public void toggle(){
        if(isShown){
            hide();
        }else {
            show();
        }
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

    public boolean isShown() {
        return isShown;
    }

    private Action completeAction = new Action(){
        public boolean act( float delta ) {
            setVisible(isShown = false);
            if(listener != null){
                listener.onClosed();
            }
            return true;
        }
    };

    protected void forceHide(){
        if(getStage() == null) return;
        isShown = false;
        if(Align.isLeft(align)){
            setPosition(-getWidth(), 0);
        }else if(Align.isRight(align)){
            setPosition(getStage().getWidth(), 0);
        }else if(Align.isBottom(align)){

        }else if(Align.isTop(align)){

        }
    }

    public void forceShow() {
        if (getStage() == null) return;
        isShown = true;
        if (Align.isLeft(align)) {
            setPosition(0, 0);
        } else if (Align.isRight(align)) {
            setPosition(getStage().getWidth() - getWidth(), 0);
        } else if (Align.isBottom(align)) {

        } else if (Align.isTop(align)) {

        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }

    @Override
    public void invalidateHierarchy() {
        super.invalidateHierarchy();
    }


    @Override
    public void layout() {
        if(isShown)
            forceShow();
        else
            forceHide();
        super.layout();
    }
}
