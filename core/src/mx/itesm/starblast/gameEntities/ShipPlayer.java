package mx.itesm.starblast.gameEntities;

import com.badlogic.gdx.Gdx;
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
    private final float BRAKE_CONSTANT = 0.97f;
    private final float MAX_TURN_RANGE = 2;

    private final int MISSILE_DAMAGE;
    private final int COOLDOWN_MISSILE = 200;
    private long previousMissile = 0;
    private int missileCount = 5;
    private movementState state = movementState.STOPPED;
    private float turnPercentage;

    private float shield;

    private Sound missileSound;

    public final float MAX_HEALTH;
    public final float MAX_SHIELD = 50;
    private boolean infHealth;
    private boolean infMissiles;
    private float speedMultiplier = 1;

    public ShipPlayer(Texture texture, float x, float y, World world, SpriteBatch batch) {

        super(texture, x, y, world, 90, 0.1f, 0.7f, false, batch);

        CATEGORY = Constant.CATEGORY_PLAYER;
        MASK = Constant.MASK_PLAYER;
        COOLDOWN_SHOT = 100;
        BULLET_DAMAGE = 10;
        MISSILE_DAMAGE = 500;
        damage = 20;

        MAX_HEALTH = health;
        shield = 0;

        fireSound = Constant.MANAGER.get("SoundEffects/ShootingSound1.mp3", Sound.class);
        missileSound = Constant.MANAGER.get("SoundEffects/MissileSound.wav", Sound.class);
        explosionSound = Constant.MANAGER.get("SoundEffects/Explosion1.mp3", Sound.class);
        infHealth = Gdx.app.getPreferences("Codes").getBoolean("InfHealth", false);
        infMissiles = Gdx.app.getPreferences("Codes").getBoolean("InfMissiles", false);
        if (Gdx.app.getPreferences("Codes").getBoolean("speed", false)) {
            speedMultiplier = 2;
        }
    }

    private void shootMissile() {
        if (!infMissiles && missileCount <= 0) {
            return;
        }
        missileCount--;
        new Missile(body.getPosition().x, body.getPosition().y, world, sprite.getRotation(), enemy, MISSILE_DAMAGE, batch);
    }

    public void shootMissile(long time) {
        if (previousMissile + COOLDOWN_MISSILE < time) {
            previousMissile = time;
            shootMissile();
            if (PreferencesSB.SOUNDS_ENABLE) {
                missileSound.play(0.5f);
            }
        }
    }

    @Override
    public void playSound() {
        fireSound.play(0.5f);
    }

    @Override
    public void move(Vector2 nothing, float delta) {
        if (state == movementState.TURNING) {
            turn();
        }
        body.applyForceToCenter(turnPercentage * -1 * speedMultiplier, accelerationPercentage * speedMultiplier, true);
        body.setLinearVelocity(body.getLinearVelocity().scl(BRAKE_CONSTANT));
        sprite.setCenter(Constant.toScreenSize(body.getPosition().x), Constant.toScreenSize(body.getPosition().y));
    }


    public void turn(float porcentaje) {
        this.turnPercentage = porcentaje * -1;
        this.state = porcentaje == 0 ? movementState.STOPPED : movementState.TURNING;
    }

    private void turn() {
        sprite.setRotation(sprite.getRotation() + MAX_TURN_RANGE * turnPercentage);
        sprite.setRotation(min(sprite.getRotation(), 90 + TURN_RANGE / 2));
        sprite.setRotation(max(sprite.getRotation(), 90 - TURN_RANGE / 2));
    }

    @Override
    public boolean doDamage(int damage) {
        if (infHealth) {
            return false;
        }
        if (shield > 0) {
            shield -= damage;
            if (shield >= 0) {
                return false;
            }
            damage = MathUtils.floor(-shield);
            shield = 0;
        }
        return super.doDamage(damage);
    }

    public void recievePowerUp(IPowerUp powerUp) {
        switch (powerUp.type()) {
            case health:
                recieveHealth(powerUp.getBonus());
                break;
            case damage:
                recieveBulletDamage(powerUp.getBonus());
                break;
            case speed:
                recieveSpeedBoost(powerUp.getBonus());
                break;
            case shield:
                recieveShield(powerUp.getBonus());
                break;
            case missile:
                recieveMissile(powerUp.getBonus());
        }
    }

    private void recieveMissile(float bonus) {
        missileCount += bonus;
    }

    private void recieveShield(float bonus) {
        shield += bonus;
        shield = min(shield, MAX_SHIELD);
    }

    private void recieveSpeedBoost(float bonus) {
        speedMultiplier += bonus;
    }

    private void recieveHealth(float bonus) {
        health += bonus;
        health = min(health, MAX_HEALTH);
    }

    private void recieveBulletDamage(float bonus) {

        BULLET_DAMAGE += bonus;
    }

    public float getHealthPercentage() {
        return health / MAX_HEALTH;
    }

}
