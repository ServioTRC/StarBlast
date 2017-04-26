package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Ian Neumann on 25/04/2017.
 */

public class Missile extends Bullet {

    private final float ACCELERATION = 2;
    private final float VELOCITY = 10;
    private World world;
    private int life = 50;

    Missile(Vector2 v, World world, float angle, boolean enemy, int damage) {
        this(v.x,v.y,world,angle,enemy,damage);
    }

    Missile(float x,float y, World world, float angle, boolean enemy, int damage) {
        super(x,y, world, angle, enemy, damage);

        //sprite = new Sprite(Constant.MANAGER.get("GameScreen/"+(enemy? "BulletSpriteEnemy.png": "BulletSprite.png"), Texture.class));
        sprite = new Sprite(Constant.MANAGER.get("GameScreen/MissileSprite.png", Texture.class));
        sprite.setCenter(x, y);
        sprite.setRotation(angle);

        body.setLinearVelocity(MathUtils.cosDeg(angle)*VELOCITY,MathUtils.sinDeg(angle)*VELOCITY);

        this.world = world;
    }

    @Override
    public void draw(SpriteBatch batch) {
        float x = ACCELERATION*Gdx.graphics.getDeltaTime()* MathUtils.cosDeg(sprite.getRotation());
        float y = ACCELERATION*Gdx.graphics.getDeltaTime()* MathUtils.sinDeg(sprite.getRotation());
        body.setLinearVelocity(body.getLinearVelocity().add(x,y));
        super.draw(batch);
    }

    @Override
    public boolean doDamage(int damage) {
        //TODO hacer la explosion y que dañe a enemigos cercanos
        this.life -= damage;
        if(life < 0){
            new Explosion(body.getPosition(),world,sprite.getRotation());
            return true;
        }
        else{
            return false;
        }
    }
}
