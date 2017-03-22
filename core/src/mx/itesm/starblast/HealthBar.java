package mx.itesm.starblast;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Ian Neumann on 21/03/2017.
 */

public class HealthBar extends Actor {

    private TextureRegion textureRegion;
    private Texture life;
    private Texture frame;
    private boolean vertical;
    private float healthPorcentage;
    int cont = 1;


    public HealthBar(Texture texture){
        this.life = texture;
        this.textureRegion = new TextureRegion(texture);
        this.vertical = vertical;
        this.healthPorcentage = 1;
        textureRegion.flip(false,true);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion,getX(),getY()-(int)(life.getHeight()*(1-healthPorcentage)));
        if(frame != null){
            batch.draw(frame,getX()+(life.getWidth()-frame.getWidth())/2, getY() + (life.getHeight()-frame.getHeight())/2);
        }
    }

    public void setHealthPorcentage(float healthPorcentage){
        cont++;
        this.healthPorcentage = healthPorcentage;
        textureRegion.setRegion(0,(int)(life.getHeight()*(1-healthPorcentage)), life.getWidth(), life.getHeight());
    }

    public void setFrame(Texture frame){
        this.frame = frame;
    }
}
