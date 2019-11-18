package rma.ox.engine.ui.gui.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;

import rma.ox.engine.utils.Logx;

public class ScreenUtils {

    public static float getWidhtScreenPourcent(float pourcent){
        return pourcent * Gdx.graphics.getBackBufferWidth() / 100;
    }

    public static float getHeightScreenPourcent(float pourcent){
        return pourcent * Gdx.graphics.getBackBufferHeight() / 100;
    }

    public static float getWidhtPourcent(float pourcent, float width){
        return pourcent * width / 100;
    }

    public static float getHeightPourcent(float pourcent, float height){
        return pourcent * height / 100;
    }

    public static Graphics.DisplayMode getHigherResolution(){
        Graphics.DisplayMode[] modes = Gdx.graphics.getDisplayModes();

        int higher = 0;
        Graphics.DisplayMode higherMode = null;
        for (int i =0; i < modes.length; i++) {
            Logx.d(null, "modes["+i+"].width : "+modes[i]+" " +modes[i].width);
            if(higher < modes[i].width ){
                higher = modes[i].width;
                higherMode = modes[i];
                Logx.d(null, "higher : "+higher);
            }
        }
        return higherMode;
    }

    public static Graphics.DisplayMode getDisplayMode(int width, int height){
        Graphics.DisplayMode[] modes = Gdx.graphics.getDisplayModes();

        for (int i =0; i < modes.length; i++) {
            if(width == modes[i].width && height == modes[i].height){
                return modes[i];
            }
        }
        return null;
    }
}
