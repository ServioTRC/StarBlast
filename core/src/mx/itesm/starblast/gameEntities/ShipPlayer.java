package mx.itesm.starblast.gameEntities;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.math.MathUtils;

import mx.itesm.starblast.Constant;
import mx.itesm.starblast.PreferencesSB;

import static java.lang.Math.*;

public class ShipPlayer extends Ship {

    private enum movementState {
        TURNING,
        STOPPED
    }

    private final float TURN_RANGE = 80;
    private final float MAX_SPEED = 8;
    private final float BRAKE_CONSTANT = 0.97f;
    private final float MAX_TURN_RANGE = 2;

    private final int MISSILE_DAMAGE;
    private final int COOLDOWN_MISSILE = 1000;
    private long previousMissile = 0;

    private float acceleration;

    private movementState state = movementState.STOPPED;
    private float turnPercentage;
    private float speed;
    private float theta;
    public float totalLife;

    public ShipPlayer(Texture texture, float x, float y, World world, SpriteBatch batch) {

        super(texture,x,y,world,90,0.1f,0.7f, false,batch);

        CATEGORY = Constant.CATEGORY_PLAYER;
        MASK = Constant.MASK_PLAYER;
        COOLDOWN_SHOT = 100;
        BULLET_DAMAGE = 10;
        MISSILE_DAMAGE = 100;

        speed = 0;
        acceleration = 0;
        damage = 20;

        totalLife = life;

        fireSound = Constant.MANAGER.get("SoundEffects/ShootingSound1.mp3", Sound.class);
        explosionSound = Constant.MANAGER.get("SoundEffects/Explosion1.mp3", Sound.class);
    }

    private void shootMissile(){
        new Missile(body.getPosition().x,body.getPosition().y,world,sprite.getRotation(),enemy,MISSILE_DAMAGE,batch);
    }

    public void shootMissile(long time){
        if(previousMissile + COOLDOWN_MISSILE < time){
            previousMissile = time;
            shootMissile();
            if(PreferencesSB.SOUNDS_ENABLE){
                //TODO sonido para cuando se dispare un misil
            }
        }
    }

    @Override
    public void playSound() {
        fireSound.play(0.5f);
    }

    @Override
    public void move(Vector2 vector, float delta) {

        switch (state){
            case TURNING:
                turn();
                break;
            default:
                break;
        }

        acceleration = (float)sqrt(turnPercentage * turnPercentage + accelerationPercentage * accelerationPercentage);

        speed = body.getLinearVelocity().len();
        speed = min(speed, MAX_SPEED);
        theta = body.getAngle();
        if (acceleration == 0) {
            vector = body.getLinearVelocity().scl(BRAKE_CONSTANT);
        } else {
            vector = body.getLinearVelocity().scl(2);
            vector.add(turnPercentage * -1, accelerationPercentage);
        }
        theta = MathUtils.atan2(vector.y, vector.x);
        speed = vector.len();

        if(acceleration != 0) {
            body.applyForceToCenter(new Vector2(turnPercentage * -1, accelerationPercentage), true);
        }
        body.setLinearVelocity(body.getLinearVelocity().scl(BRAKE_CONSTANT));
        sprite.setCenter(Constant.toScreenSize(body.getPosition().x), Constant.toScreenSize(body.getPosition().y));
    }


    public void turn(float porcentaje){
        this.turnPercentage = porcentaje*-1;
        this.state = porcentaje == 0? movementState.STOPPED : movementState.TURNING;
    }

    private void turn(){
        sprite.setRotation(sprite.getRotation()+ MAX_TURN_RANGE * turnPercentage);
        sprite.setRotation(min(sprite.getRotation(),90+ TURN_RANGE /2));
        sprite.setRotation(max(sprite.getRotation(),90- TURN_RANGE /2));
    }

}
