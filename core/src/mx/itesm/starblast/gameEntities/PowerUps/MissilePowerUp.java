package mx.itesm.starblast.gameEntities.PowerUps;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import mx.itesm.starblast.Constant;

/**
 * Created by Ian Neumann on 29/04/2017.
 */

public class MissilePowerUp extends PowerUp {


    int bonus = 1;
    Type type = Type.missile;

    public MissilePowerUp(Texture texture, float x, float y, World world){
        super(texture, x, y, world, 90, 0.2f, 0.2f);
    }



    @Override
    public Type type() {
        return type;
    }

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
        sprite.draw(batch);
        sprite.setCenter(Constant.toScreenSize(body.getPosition().x), Constant.toScreenSize(body.getPosition().y));
        return false;
    }

    @Override
    public Body getBody() {
        return body;
    }

    @Override
    public float getX() {
        return Constant.toScreenSize(body.getPosition().x);
    }

    @Override
    public float getY() {
        return Constant.toScreenSize(body.getPosition().y);
    }

    @Override
    public float getBonus() {
        return bonus;
    }
}
