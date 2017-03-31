package mx.itesm.starblast;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

interface IPlayableEntity {

    void setDamage(int dmg);
    int getDamage();
    boolean doDamage(int damage);
    void draw(SpriteBatch batch);
    Body getBody();
    float getX();
    float getY();
}
