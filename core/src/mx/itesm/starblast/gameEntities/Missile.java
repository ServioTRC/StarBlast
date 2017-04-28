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

    private final float ACCELERATION = 1.2f;
    private final float VELOCITY = 2;
    private World world;
    private int life = 50;
    SpriteBatch batch;

    private float thrust;
    private Explosion explosion;


    Missile(Vector2 v, World world, float angle, boolean enemy, int damage,SpriteBatch batch) {
        this(v.x,v.y,world,angle,enemy,damage,batch);
    }

    Missile(float x,float y, World world, float angle, boolean enemy, int damage,SpriteBatch batch) {
        super(x,y, world, angle, enemy, damage);

        density = 0.5f;
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
        thrust += ACCELERATION*Gdx.graphics.getDeltaTime();
        float x = thrust* MathUtils.cosDeg(sprite.getRotation());
        float y = thrust* MathUtils.sinDeg(sprite.getRotation());
        body.setLinearVelocity(body.getLinearVelocity().add(x,y));
        super.draw(batch);
        return false;
    }

    @Override
    public boolean doDamage(int damage){
        if(explosion != null){
            return false;
        }
        this.life -= damage;
        if(life < 0){
            AutoAnimation anim = new AutoAnimation(Constant.MANAGER.get("Animations/ExplosionMissileFrames.png", Texture.class), 0.15f, Constant.toScreenSize(body.getPosition().x), Constant.toScreenSize(body.getPosition().y), Constant.MISSILE_EXPLOSION_SIZE_X, Constant.MISSILE_EXPLOSION_SIZE_Y,batch);;
            explosion = new Explosion(body.getPosition(),world, batch,anim);
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
