package rma.ox.engine.renderable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ArrayMap;

public class G2DRenderManager {

    private static G2DRenderManager instance;

    public static G2DRenderManager get() {
        if (instance == null) instance = new G2DRenderManager();
        return instance;
    }

    /*******************************/

    public interface G2DRenderable{
        void render(SpriteBatch batch, float delta);
    }

    /*******************************/

    private ArrayMap<G2DRenderable, Integer> listRenderable;
    private SpriteBatch batch;
    private final int zDefault = 10;

    public G2DRenderManager() {
        batch = new SpriteBatch();
        listRenderable = new ArrayMap<>();
    }

    public void render(float delta) {
        batch.begin();
        {
            for (int i = 0; i < listRenderable.size; i++) {
                listRenderable.getKeyAt(i).render(batch, delta);
            }
        }
        batch.end();
    }

    public void addRenderable(G2DRenderable renderable, int zIndex){
        listRenderable.put(renderable, zIndex);
    }

    public void addRenderable(G2DRenderable renderable){
        listRenderable.put(renderable, zDefault);
    }
}
