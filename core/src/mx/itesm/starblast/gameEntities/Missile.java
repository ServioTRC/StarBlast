package mx.itesm.starblast.gameEntities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import mx.itesm.starblast.Constant;

public class Missile extends Bullet implements IExplotable {

    private final float ACCELERATION = 2;
    private final float VELOCITY = 10;
    private World world;
    private int life = 50;
    SpriteBatch batch;
    private Explosion explosion;

    Missile(Vector2 v, World world, float angle, boolean enemy, int damage,SpriteBatch batch) {
        this(v.x,v.y,world,angle,enemy,damage,batch);
    }

    Missile(float x,float y, World world, float angle, boolean enemy, int damage,SpriteBatch batch) {
        super(x,y, world, angle, enemy, damage);

        density = 0.2f;
        restitution = 0;
        makeFixture(density,restitution);

        sprite = new Sprite(Constant.MANAGER.get("GameScreen/MissileSprite.png", Texture.class));
        sprite.setCenter(x, y);
        sprite.setRotation(angle);

        body.setLinearVelocity(MathUtils.cosDeg(angle)*VELOCITY,MathUtils.sinDeg(angle)*VELOCITY);

        this.world = world;
        this.batch = batch;
    }

    @Override
    public boolean draw(SpriteBatch batch) {
        float x = ACCELERATION*Gdx.graphics.getDeltaTime()* MathUtils.cosDeg(sprite.getRotation());
        float y = ACCELERATION*Gdx.graphics.getDeltaTime()* MathUtils.sinDeg(sprite.getRotation());
        body.setLinearVelocity(body.getLinearVelocity().add(x,y));
        super.draw(batch);
        return false;
    }

    @Override
    public boolean doDamage(int damage) {
        //TODO hacer la explosion y que dañe a enemigos cercanos
        this.life -= damage;
        if(life < 0){
            explosion = new Explosion(body.getPosition(),world, batch);
            return true;
        }
        else{
            return false;
        }
    }

    public Explosion getExplosion(){
        return explosion;
    }
}
