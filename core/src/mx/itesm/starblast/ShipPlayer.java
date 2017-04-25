package mx.itesm.starblast;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.math.MathUtils;
import static java.lang.Math.*;

class ShipPlayer extends Ship {

    private enum movementState {
        TURNING,
        STOPPED
    }

    private final float TURN_RANGE = 80;
    private final float MAX_SPEED = 8;
    private final float BRAKE_CONSTANT = 0.97f;
    private final float MAX_TURN_RANGE = 2;

    private float acceleration;

    private movementState state = movementState.STOPPED;
    private float turnPercentage;
    private float speed;
    private float theta;
    float totalLife;

    ShipPlayer(Texture texture, float x, float y, World world) {
        super(texture,x,y,world,90,0.1f,0.7f, false);

        CATEGORY = Constant.CATEGORY_PLAYER;
        MASK = Constant.MASK_PLAYER;
        COOLDOWN_SHOT = 100;
        BULLET_DAMAGE = 10;

        speed = 0;
        acceleration = 0;
        damage = 20;

        totalLife = life;
/*
        fireSound = Constant.MANAGER.get("EfectosSonoros/SonidoDisparo1.mp3", Sound.class);
        explosionSound = Constant.MANAGER.get("EfectosSonoros/explosion2.mp3", Sound.class);*/
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


    void turn(float porcentaje){
        this.turnPercentage = porcentaje*-1;
        this.state = porcentaje == 0? movementState.STOPPED : movementState.TURNING;
    }

    private void turn(){
        sprite.setRotation(sprite.getRotation()+ MAX_TURN_RANGE * turnPercentage);
        sprite.setRotation(min(sprite.getRotation(),90+ TURN_RANGE /2));
        sprite.setRotation(max(sprite.getRotation(),90- TURN_RANGE /2));
    }

}
