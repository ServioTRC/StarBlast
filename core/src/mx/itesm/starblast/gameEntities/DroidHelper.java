package mx.itesm.starblast.gameEntities;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import mx.itesm.starblast.Constant;

/**
 * Created by Ian Neumann on 05/05/2017.
 */

public class DroidHelper extends Ship {

    private final float BRAKE_CONSTANT = 0.97f;
    private boolean dead;

    DroidHelper(Texture texture, float x, float y, World world, float angle, float density, float restitution, SpriteBatch batch) {
        super(texture, x, y, world, angle, density, restitution, false, batch);

        CATEGORY = Constant.CATEGORY_PLAYER;
        MASK = Constant.MASK_PLAYER;
        COOLDOWN_SHOT = 100;
        BULLET_DAMAGE = 10;

        damage = 20;
        health = 50;
        dead = false;

        fireSound = Constant.MANAGER.get("SoundEffects/ShootingSound1.mp3", Sound.class);
        explosionSound = Constant.MANAGER.get("SoundEffects/Explosion2.mp3", Sound.class);
        bulletTexture = Constant.MANAGER.get("GameScreen/BulletSprite.png", Texture.class);
    }

    @Override
    public void playSound() {
        fireSound.play(0.5f);
    }

    @Override
    public void move(Vector2 force, float delta) {
        body.applyForceToCenter(force,true);
        sprite.setCenter(getX(),getY());
        body.setLinearVelocity(body.getLinearVelocity().scl(BRAKE_CONSTANT));
    }

    public void move(float fx, float fy, float delta){
        body.applyForceToCenter(fx,fy,true);
        sprite.setCenter(getX(),getY());
        body.setLinearVelocity(body.getLinearVelocity().scl(BRAKE_CONSTANT));
    }

    @Override
    public void die() {
        dead = true;
        super.die();
    }

    public boolean isDead() {
        return dead;
    }

    protected void setRotation(float degrees){
        sprite.setRotation(degrees);
    }
}
