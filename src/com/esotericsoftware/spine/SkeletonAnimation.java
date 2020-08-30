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

    public void clearAnimation(){
        state.clearTracks();
    }

    public void setEmptyAnimation(int trackIndex, String animationName, float mixDuration){
        if(animationAlreadyUsed.containsKey(animationName)){
            state.setEmptyAnimation(animationAlreadyUsed.get(animationName), mixDuration);
            animationAlreadyUsed.removeKey(animationName);
        }else{
            state.addEmptyAnimation(trackIndex, mixDuration, 0);
            //animationAlreadyUsed.put(animationName, trackIndex);
        }
    }

    public AnimationState.TrackEntry setAnimation(int trackIndex, String animationName, float delay, boolean isLoop, boolean isAddOrMix, float mixDuration, float speed, float alpha){
        if(skeleton.getData().findAnimation(animationName) == null){
            Logx.l(this.getClass(), "Animation "+animationName+ " not found !");
            return null;
        }

        //if(animationAlreadyUsed.containsKey(animationName)){
        //    //return null;
        //    state.addEmptyAnimation(trackIndex, mixDuration, 0);
        //}

       while (animationAlreadyUsed.containsValue(trackIndex, true)){
           trackIndex ++;
       }

        state.addEmptyAnimation(trackIndex, mixDuration, 0);

        Logx.e("===/// setAnimation animationName : "+animationName+ " track Index : "+trackIndex+ " speed : "+speed);
        AnimationState.TrackEntry current = state.getCurrent(trackIndex);
        AnimationState.TrackEntry entry;
        if (current == null) {
            state.setEmptyAnimation(trackIndex, mixDuration);
            entry = state.addAnimation(trackIndex, animationName, isLoop, delay);
        } else {
            entry = state.setAnimation(trackIndex, animationName, isLoop);
            entry.setDelay(delay);
        }
        entry.setMixBlend(isAddOrMix ? Animation.MixBlend.add : Animation.MixBlend.replace);
        entry.setMixDuration(mixDuration);
        entry.setTimeScale(speed);
        entry.setAlpha(alpha);

        animationAlreadyUsed.put(animationName, trackIndex);

        return entry;
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
