package mx.itesm.starblast.gameEntities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

public interface IPlayableEntity {

    void setDamage(int dmg);
    int getDamage();
    boolean doDamage(int damage);
    boolean draw(SpriteBatch batch);
    Body getBody();
    float getX();
    float getY();
}
