package com.esotericsoftware.spine;

import com.badlogic.gdx.utils.Array;

import rma.ox.engine.settings.Config;
import rma.ox.engine.utils.Logx;

public class SkeletonAnimation {

    private static float MIX_DURATION = 0.25f;
    private Skeleton skeleton;
    public AnimationState state;

    public SkeletonAnimation(Skeleton skeleton) {
        this.skeleton = skeleton;

        AnimationStateData stateData = new AnimationStateData(skeleton.getData());
        state = new AnimationState(stateData);

        //logAllAnimations();
    }

    public void logAllAnimations(){
        for(Animation animation : skeleton.getData().getAnimations()){
            Logx.l(this.getClass(), skeleton.getData().getName() + " : " + animation.getName());
        }
    }

    public Array<Animation> getAllAnimations(){
        return skeleton.getData().getAnimations();
    }

    public void playSingleAnimation(String animationName){
        playAnimation(0, animationName, false, MIX_DURATION);
    }

    public void playLoopAnimation(String animationName){
        playAnimation(0, animationName, true, MIX_DURATION);
    }

    public void clearAnimation(){
        state.clearTracks();
    }

    public  void playAnimation(int trackIndex, String animationName, boolean loop, float mixDuration){
        if(skeleton.getData().findAnimation(animationName) == null){
            Logx.l(this.getClass(), "Animation "+animationName+ " not found !");
            return;
        }
        if(state.getTracks().size>0 && state.getTracks().get(trackIndex)!=null && !state.getTracks().get(trackIndex).getAnimation().getName().equals(animationName)){
            state.setAnimation(trackIndex, animationName, loop).setMixDuration(mixDuration);
        }else if(state.getTracks().size==0){
            state.setAnimation(trackIndex, animationName, loop).setMixDuration(mixDuration);
        }
        state.setTimeScale(1f);
    }

    public  void playAnimation(AnimationState state, int trackIndex, String animationName, boolean loop){
        if(skeleton.getData().findAnimation(animationName) == null){
            Logx.l(this.getClass(), "Animation "+animationName+ " not found !");
            return;
        }
        if(state.getTracks().size>0 && state.getTracks().get(trackIndex)!=null && !state.getTracks().get(trackIndex).getAnimation().getName().equals(animationName)){
            state.setAnimation(trackIndex, animationName, loop).setMixDuration(MIX_DURATION);
        }else if(state.getTracks().size==0){
            state.setAnimation(trackIndex, animationName, loop).setMixDuration(MIX_DURATION);
        }
    }

    public void update(float delta){
        state.update(delta);
    }

    public void apply(boolean isVisibleInFrustrum){
        if(isVisibleInFrustrum) {
            state.apply(skeleton);
            skeleton.updateWorldTransform();
        }
    }
}
