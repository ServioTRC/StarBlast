package mx.itesm.starblast;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Ian Neumann on 21/03/2017.
 */

public class ProgressBar extends Actor {

    private TextureRegion textureRegion;
    private Texture bar;
    private Texture frame;
    private boolean vertical;
    private float porcentage;


    public ProgressBar(Texture texture,boolean vertical){
        this.bar = texture;
        this.textureRegion = new TextureRegion(texture);
        this.vertical = vertical;
        this.porcentage = 1;
        setWidth(texture.getWidth());
        setHeight(texture.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(this.vertical) {
            drawVertical(batch);
        }
        else{
            drawHorizontal(batch);
        }
    }

    private void drawHorizontal(Batch batch) {
        if (frame != null) {
            batch.draw(frame, getX() - (frame.getWidth() - bar.getWidth()) / 2, getY() - (frame.getHeight() - bar.getHeight()) / 2);
        }
        batch.draw(textureRegion, getX(), getY());
    }

    private void drawVertical(Batch batch) {
        if (frame != null) {
            batch.draw(frame, getX() - (frame.getWidth() - bar.getWidth()) / 2, getY() - (frame.getHeight() - bar.getHeight()) / 2);
        }
        batch.draw(textureRegion, getX(), getY() - (int) (bar.getHeight() * (1 - porcentage)));
    }

    public void setPorcentage(float porcentage){
        this.porcentage = porcentage;
        if(vertical) {
            textureRegion.setRegion(0, (int) (bar.getHeight() * (1 - porcentage)), bar.getWidth(), bar.getHeight());
        }
        else{
            textureRegion.setRegion(0,0,(int)(bar.getWidth()* porcentage),bar.getHeight());
        }
    }

    public void setFrame(Texture frame){
        this.frame = frame;
    }
}
