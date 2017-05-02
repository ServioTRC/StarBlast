package mx.itesm.starblast.gameEntities;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import mx.itesm.starblast.Constant;

public class ShipEnemy extends Ship {

    private final int MAX_ACCELERATION = 10;
    private final int MIN_ACCELERATION = -10;
    private final int MAX_TURN_RANGE = 2;
    private final int TOP_SPEED = 4;
    private final int IMPULSE = 30;

    float speed;
    boolean canShoot;

    ShipEnemy(Texture texture, float x, float y, World world, float angle, float density, float restitution, SpriteBatch batch) {
        super(texture, x, y, world, angle, density, restitution, true,batch);

        CATEGORY = Constant.CATEGORY_ENEMY;
        MASK = Constant.MASK_ENEMY;

        bulletTexture = Constant.MANAGER.get("GameScreen/BulletSpriteEnemy.png",Texture.class);
    }

    public ShipEnemy(Texture texture, float x, float y, World world,SpriteBatch batch) {
        super(texture, x, y, world, -90, 0.1f, 0.7f, true,batch);


        CATEGORY = Constant.CATEGORY_ENEMY;
        MASK = Constant.MASK_ENEMY;
        COOLDOWN_SHOT = 500;
        BULLET_DAMAGE = 5;

        health = 30;
        speed = 0;
        damage = 15;
        canShoot = false;

        fireSound = Constant.MANAGER.get("SoundEffects/ShootingSound1.mp3", Sound.class);
        explosionSound = Constant.MANAGER.get("SoundEffects/Explosion2.mp3",Sound.class);

        bulletTexture = Constant.MANAGER.get("GameScreen/BulletSpriteEnemy.png",Texture.class);

    }

    @Override
    public void shoot(long time) {
        if (canShoot && getX() > 0 && getX() < Constant.SCREEN_WIDTH &&
                getY() > 0 && getY() < Constant.SCREEN_HEIGTH) {
            super.shoot(time);
        }
    }

    @Override
    public void playSound() {
        fireSound.play(0.2f);
    }


    @Override
    public void accelerate(float acceleration) {
        acceleration = Math.min(acceleration, MAX_ACCELERATION);
        acceleration = Math.max(acceleration, MIN_ACCELERATION);

        speed = body.getLinearVelocity().len();
        speed += acceleration;
        speed = Math.min(speed, TOP_SPEED);

    }

    private void turn(float angle) {
        angle += 360;
        angle %= 360;

        if (sprite.getRotation() < 0) {
            sprite.setRotation(360 + sprite.getRotation());
        }
        float theta = sprite.getRotation();
        theta += 360;
        theta %= 360;

        int sign;
        if ((sprite.getRotation() - 180 <= angle && angle <= sprite.getRotation()) || 360 + (sprite.getRotation() - 180) <= angle) {
            sign = -1;
        } else {
            sign = 1;
        }
        if (Math.abs(sprite.getRotation() - angle) > MAX_TURN_RANGE) {
            theta += sign * MAX_TURN_RANGE;
            canShoot = false;
        } else {
            theta += sign * Math.abs(sprite.getRotation() - angle);
            canShoot = true;
        }

        sprite.setRotation(theta);
    }

    @Override
    public void move(Vector2 target, float delta) {
        float deltaX;
        float deltaY;

        if (Math.abs(Constant.toWorldSize(target.x) - body.getPosition().x) < Math.abs(body.getLinearVelocity().x)) {
            deltaX = Constant.toWorldSize(target.x) - body.getPosition().x;
        } else {
            deltaX = Constant.toWorldSize(target.x) - (body.getLinearVelocity().x + body.getPosition().x);
        }

        if (Math.abs(Constant.toWorldSize(target.y) - body.getPosition().y) < Math.abs(body.getLinearVelocity().y)) {
            deltaY = Constant.toWorldSize(target.y) - body.getPosition().y;
        } else {
            deltaY = Constant.toWorldSize(target.y) - (body.getLinearVelocity().y + body.getPosition().y);
        }

        float angle;
        angle = MathUtils.atan2(deltaY, deltaX);
        angle = (angle * 180 / (MathUtils.PI));
        angle += 360;
        angle %= 360;
        turn(angle);

        accelerate(IMPULSE * delta);

        updateBody();
    }

    private void updateBody() {
        float x = MathUtils.cosDeg(sprite.getRotation());
        float y = MathUtils.sinDeg(sprite.getRotation());
        float hip = body.getLinearVelocity().len();
        body.applyForceToCenter(x*0.3f,y*0.3f, true);
        if (hip > TOP_SPEED) {
            body.setLinearVelocity(body.getLinearVelocity().scl(TOP_SPEED / hip));
        }
        sprite.setCenter(Constant.toScreenSize(body.getPosition().x), Constant.toScreenSize(body.getPosition().y));
    }
}
