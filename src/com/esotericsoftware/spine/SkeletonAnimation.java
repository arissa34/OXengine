package com.esotericsoftware.spine;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

import rma.ox.engine.settings.Config;
import rma.ox.engine.utils.Logx;

public class SkeletonAnimation {

    private static float MIX_DURATION = 0.5f;
    private Skeleton skeleton;
    public AnimationState state;
    private ArrayMap<String, Integer> animationAlreadyUsed;

    public SkeletonAnimation(Skeleton skeleton) {
        this.skeleton = skeleton;
        animationAlreadyUsed = new ArrayMap<>();

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

    public void playLoopAnimation(int trackIndex, String animationName){
        playAnimation(trackIndex, animationName, true, MIX_DURATION);
    }

    public void addLoopAnimation(int trackIndex, String animationName, boolean isAddOrMix){
        if(skeleton.getData().findAnimation(animationName) == null){
            Logx.l(this.getClass(), "Animation "+animationName+ " not found !");
            return;
        }
        AnimationState.TrackEntry current = state.getCurrent(trackIndex);
        AnimationState.TrackEntry entry;
        if (current == null) {
            state.setEmptyAnimation(trackIndex, 0);
            entry = state.addAnimation(trackIndex, animationName, true, 0);
            entry.setMixDuration(0.5f);
        } else {
            entry = state.setAnimation(trackIndex, animationName, true);
        }
        entry.setMixBlend(isAddOrMix ? Animation.MixBlend.add : Animation.MixBlend.replace);
        entry.setAlpha(0.5f);
        state.addAnimation(trackIndex, animationName, true, 0);
    }

    public void clearAnimation(){
        state.clearTracks();
    }

    public  void playAnimation(int trackIndex, String animationName, boolean loop, float mixDuration){
        if(skeleton.getData().findAnimation(animationName) == null){
            Logx.l(this.getClass(), "Animation "+animationName+ " not found !");
            return;
        }
        state.setAnimation(trackIndex, animationName, loop).setMixDuration(mixDuration);
        state.setTimeScale(1f);
    }

    public void setEmptyAnimation(String animationName, float mixDuration){
        if(animationAlreadyUsed.containsKey(animationName)){
            state.setEmptyAnimation(animationAlreadyUsed.get(animationName), mixDuration);
            animationAlreadyUsed.removeKey(animationName);
        }
    }

    public AnimationState.TrackEntry setAnimation(int trackIndex, String animationName, boolean isLoop, boolean isAddOrMix, float mixDuration, float speed, float alpha){
        if(skeleton.getData().findAnimation(animationName) == null){
            Logx.l(this.getClass(), "Animation "+animationName+ " not found !");
            return null;
        }

        if(animationAlreadyUsed.containsKey(animationName)) return null;

        while (animationAlreadyUsed.containsValue(trackIndex, true)){
            trackIndex ++;
        }

        Logx.e("===/// setAnimation animationName : "+animationName+ " track Index : "+trackIndex+ " speed : "+speed);
        AnimationState.TrackEntry current = state.getCurrent(trackIndex);
        AnimationState.TrackEntry entry;
        if (current == null) {
            state.setEmptyAnimation(trackIndex, mixDuration);
            entry = state.addAnimation(trackIndex, animationName, isLoop, 0);
        } else {
            entry = state.setAnimation(trackIndex, animationName, isLoop);
        }
        entry.setMixBlend(isAddOrMix ? Animation.MixBlend.add : Animation.MixBlend.replace);
        entry.setMixDuration(mixDuration);
        entry.setTimeScale(speed);
        entry.setAlpha(alpha);

        animationAlreadyUsed.put(animationName, trackIndex);

        return entry;
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
