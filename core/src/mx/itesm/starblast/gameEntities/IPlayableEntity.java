package mx.itesm.starblast.gameEntities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;

import mx.itesm.starblast.Constant;

public interface IPlayableEntity {

    void setDamage(int dmg);
    int getDamage();
    boolean doDamage(int damage);
    boolean draw(SpriteBatch batch);
    Body getBody();
    float getX();
    float getY();

}
