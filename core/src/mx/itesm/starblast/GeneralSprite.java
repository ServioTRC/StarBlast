package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Servio T on 07/02/2017.
 */

public class GeneralSprite {
    private Texture texture;
    private Sprite sprite;
    private AssetManager assetManager;

    public GeneralSprite(String ubicacion, float x, float y){
        this.assetManager = StarBlast.ASSET_GENERAL;
        this.texture = assetManager.get(ubicacion);
        this.sprite = new Sprite(texture);
        this.sprite.setPosition(x,y);
    }

    public void setAlpha(float alpha){
        this.sprite.setAlpha(alpha);
    }

    public void draw(SpriteBatch batch) {
        this.sprite.draw(batch);
    }

    public boolean isTouched(float x, float y) {
        return this.sprite.getBoundingRectangle().contains(x, y);
    }

}
