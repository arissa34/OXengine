package rma.ox.engine.renderable.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import rma.ox.engine.camera.helper.CameraHelper;
import rma.ox.engine.update.UpdatableMainThread;
import rma.ox.engine.update.UpdateManager;

public class FlareManager implements Disposable, UpdatableMainThread {

    private final float spacing;
    private float brightness;
    private boolean isEnable = true;

    private final Vector2 screenCenter = new Vector2(0.5f, 0.5f);
    private final Vector2 sunToCenter = new Vector2();
    private final Vector2 sunScreenCoorV2 = new Vector2();
    private final Vector2 direction = new Vector2();
    private final Vector2 flarePos = new Vector2();

    private final Vector3 sunPosition = new Vector3();
    private final Vector3 sunScreenCoor = new Vector3();

    private Array<Sprite> listSprites;

    public FlareManager(float spacing){
        this.spacing  = spacing;
        listSprites = new Array<>();
    }

    public FlareManager setLensSprites(Array<Sprite> listSprites){
        this.listSprites = listSprites;
        return this;
    }

    public FlareManager setSunPosition(Vector3 sunPosition){
        this.sunPosition.set(sunPosition);
        return this;
    }

    @Override
    public void updateOnMainThread(float delta) {
        sunScreenCoor.set(sunPosition);
        CameraHelper.get().getCamera().project(sunScreenCoor);
        sunScreenCoor.x /= Gdx.graphics.getBackBufferWidth();
        sunScreenCoor.y /= Gdx.graphics.getBackBufferHeight();
        sunScreenCoorV2.set(sunScreenCoor.x, sunScreenCoor.y);

        sunToCenter.set(screenCenter).sub(sunScreenCoorV2);
        brightness = 0.35f - (sunToCenter.len() / 0.8f);

        calcFlarePosition();
    }

    public void render(SpriteBatch spriteBatch){
        if(!isEnable) return;
        spriteBatch.enableBlending();
        spriteBatch.flush();
        spriteBatch.begin();
        for(int i = 0; i < 9; i++){
            listSprites.get(i).draw(spriteBatch);
        }
        spriteBatch.end();
    }

    private void calcFlarePosition(){
        if(brightness <= 0) return;
        for(int i = 0; i < listSprites.size; i++){
            direction.set(sunToCenter).scl(i * spacing);
            flarePos.set(sunScreenCoorV2).add(direction);
            listSprites.get(i).setAlpha(brightness);
            listSprites.get(i).setPosition(flarePos.x * Gdx.graphics.getBackBufferWidth() - (listSprites.get(i).getWidth()/2), flarePos.y * Gdx.graphics.getBackBufferHeight() - (listSprites.get(i).getHeight()/2));
        }
    }

    public void setEnabled(boolean isEnable){
        this.isEnable = isEnable;
        if(this.isEnable){
            UpdateManager.get().register(this);
        }else {
            UpdateManager.get().remove(this);
        }
    }

    public FlareManager attachToWorld() {
        UpdateManager.get().register(this);
        return this;
    }

    @Override
    public void dispose() {

    }
}
