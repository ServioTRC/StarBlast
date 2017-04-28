package mx.itesm.starblast.gameEntities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Ian Neumann on 28/04/2017.
 */

public class PowerUp implements IPlayableEntity {

    @Override
    public void setDamage(int dmg) {

    }

    @Override
    public int getDamage() {
        return 0;
    }

    @Override
    public boolean doDamage(int damage) {
        return false;
    }

    @Override
    public boolean draw(SpriteBatch batch) {
        return false;
    }

    @Override
    public Body getBody() {
        return null;
    }

    @Override
    public float getX() {
        return 0;
    }

    @Override
    public float getY() {
        return 0;
    }
}
