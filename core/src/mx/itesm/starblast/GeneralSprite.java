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
        this.texture = new Texture(ubicacion);
        this.sprite = new Sprite(texture);
        this.assetManager = Constantes.ASSET_GENERAL;
        this.sprite.setCenter(sprite.getWidth()/2,sprite.getHeight()/2);
        this.sprite.setPosition(x-sprite.getWidth()/2,y-sprite.getHeight()/2);
    }

    public void setAlpha(float alpha){
        this.sprite.setAlpha(alpha);
    }

    public void setTexture(String ubicacion){
        this.texture = new Texture(ubicacion);
        this.sprite.setTexture(texture);
    }

    public void draw(SpriteBatch batch) {
        this.sprite.draw(batch);
    }

    //Gira en sentido contrario a las manecillas del reloj
    public void rotar(float angulo){
        this.sprite.setRotation(angulo);
    }

    public boolean isTouched(float x, float y, Camera camara) {
        Vector3 vector = new Vector3(x,y,0);
        camara.unproject(vector);
        return this.sprite.getBoundingRectangle().contains(vector.x, vector.y);
    }

    public void escalar(float escala){
        this.sprite.scale(escala);
    }

    public Sprite getSprite(){
        return this.sprite;
    }
}
