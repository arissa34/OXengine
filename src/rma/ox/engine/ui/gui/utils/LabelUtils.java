package rma.ox.engine.ui.gui.utils;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class LabelUtils {


    public static float getTexttWidth(Label label){
        label.layout();
        GlyphLayout glyphLayout = label.getGlyphLayout();
        return glyphLayout.width;
    }

    public static float getTexttHeight(Label label){
        label.layout();
        GlyphLayout glyphLayout = label.getGlyphLayout();
        return glyphLayout.height;
    }

}
