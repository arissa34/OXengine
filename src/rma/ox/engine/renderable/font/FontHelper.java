package rma.ox.engine.renderable.font;

public class FontHelper {

    private static FontHelper instance;

    public static FontHelper get() {
        if (instance == null) instance = new FontHelper();
        return instance;
    }

    /*******************************/


}
