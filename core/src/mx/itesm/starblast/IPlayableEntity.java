package mx.itesm.starblast;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

interface IPlayableEntity {

    void setDamage(int dmg);
    int getDamage();
    boolean doDamage(int damage);
    void draw(SpriteBatch batch);

}
