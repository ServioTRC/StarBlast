package mx.itesm.starblast.gameEntities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.math.MathUtils;

import mx.itesm.starblast.Constant;
import mx.itesm.starblast.PreferencesSB;
import mx.itesm.starblast.gameEntities.PowerUps.*;

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

    public float MAX_HEALTH;
    public final float MAX_SHIELD = 50;
    private boolean infHealth;
    private boolean infMissiles;
    private float speedMultiplier = 1;
    private int damageMultiplier;

    private Texture missileTexture;
    private Sprite shieldSprite;

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
        bulletTexture = Constant.MANAGER.get("GameScreen/BulletSprite.png", Texture.class);
        missileTexture = Constant.MANAGER.get("GameScreen/MissileSprite.png", Texture.class);
        shieldSprite = new Sprite(Constant.MANAGER.get("GameScreen/ShieldSprite.png",Texture.class));
        
        Preferences pref = Gdx.app.getPreferences("Codes");
        infHealth = pref.getBoolean("InfHealth", false);
        infMissiles = pref.getBoolean("InfMissiles", false);
        if (pref.getBoolean("speed", false)) {
            speedMultiplier = 2;
        }
        if (pref.getBoolean("konami", false)) {
            missileCount *= 2;
            MAX_HEALTH *= 2;
            health = MAX_HEALTH;
        }
        if (pref.getBoolean("nails", false)) {
            damageMultiplier = 4;
        }
    }

    private void shootMissile() {
        new Missile(body.getPosition().x, body.getPosition().y, world, sprite.getRotation(), enemy, MISSILE_DAMAGE, batch, missileTexture);
    }

    public void shootMissile(long time) {
        if (previousMissile + COOLDOWN_MISSILE < time) {
            previousMissile = time;
            if (!infMissiles && missileCount <= 0) {
                return;
            }
            missileCount--;
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
    public boolean draw(SpriteBatch batch) {
        if(shield > 0) {
            shieldSprite.setCenter(getX(), getY());
            shieldSprite.setAlpha(getShieldPercentage());
            shieldSprite.draw(batch);
        }
        return super.draw(batch);
    }

    @Override
    public int getDamage() {
        return damageMultiplier * super.getDamage();
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

    public void recievePowerUp(PowerUp powerUp) {
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
        bulletTexture = Constant.MANAGER.get("GameScreen/BulletSpritePowered.png", Texture.class);
        BULLET_DAMAGE += bonus;
    }

    public float getHealthPercentage() {
        return health / MAX_HEALTH;
    }

    public float getShieldPercentage(){
        return shield / MAX_SHIELD;
    }

}
