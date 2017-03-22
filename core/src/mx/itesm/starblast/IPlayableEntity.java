package mx.itesm.starblast;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

interface IPlayableEntity {

    int getDamage();
    void doDamage(int damage);
    void draw(SpriteBatch batch);

}
