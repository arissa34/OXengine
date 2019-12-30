package rma.ox.engine.renderable.skeleton;

import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.esotericsoftware.spine.SkeletonRenderer;

public interface SkeletonRender {
    void draw(PolygonSpriteBatch polygonBatch, SkeletonRenderer skeletonRenderer);

    float getZ();
}
