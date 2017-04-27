package mx.itesm.starblast.gameEntities;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

import mx.itesm.starblast.Constant;
import mx.itesm.starblast.PreferencesSB;

public abstract class Ship implements IPlayableEntity,IExplotable {

    //TODO considerar hacer más exactos los coliders

    Body body;
    Sprite sprite;
    float accelerationPercentage;
    long COOLDOWN_SHOT;
    long previousShot = 0;
    final World world;
    short CATEGORY = -1;
    short MASK = -1;
    public int life;
    int damage;
    int BULLET_DAMAGE;
    boolean enemy;
    Sound fireSound;
    Sound explosionSound;
    SpriteBatch batch;
    Explosion explosion;
    Random r;

    Ship(Texture texture, float x, float y, World world, float angle, float density, float restitution, boolean enemy, SpriteBatch batch) {
        this.world = world;
        this.enemy = enemy;
        life = 100;
        damage = 10;
        sprite = new Sprite(texture);
        sprite.setRotation(angle);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(Constant.toWorldSize(x), Constant.toWorldSize(y));
        bodyDef.angle = angle;
        body = world.createBody(bodyDef);
        body.setUserData(this);
        BULLET_DAMAGE = 10;
        makeFixture(density, restitution);

        r = new Random();
        this.batch = batch;

    }

    public void shoot(long time) {
        if (previousShot + COOLDOWN_SHOT < time) {
            previousShot = time;
            shoot();
            if(PreferencesSB.SOUNDS_ENABLE) {
                playSound();
            }
        }
    }

    public abstract void playSound();

    protected void shoot(){
        new Bullet(body.getPosition().x, body.getPosition().y,world, sprite.getRotation()+r.nextFloat()*10-5, enemy,BULLET_DAMAGE);
    }

    public void accelerate(float porcentaje) {
        this.accelerationPercentage = porcentaje;
    }

    public abstract void move(Vector2 punto, float delta);

    @Override
    public float getX() {
        return Constant.toScreenSize(body.getPosition().x);
    }

    @Override
    public float getY() {
        return Constant.toScreenSize(body.getPosition().y);
    }

    public void scaling(float scale) {
        this.sprite.scale(scale);
        Fixture fix = body.getFixtureList().first();
        makeFixture(fix.getDensity(), fix.getRestitution());
    }

    void makeFixture(float density, float restitution){
        while (body.getFixtureList().size > 0){
            body.destroyFixture(body.getFixtureList().first());
        }
        CircleShape bodyShape = new CircleShape();

        float w= Constant.toWorldSize(sprite.getWidth()*sprite.getScaleX()/2f);

        bodyShape.setRadius(w);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density=density;
        fixtureDef.restitution=restitution;
        fixtureDef.shape=bodyShape;
        fixtureDef.friction = 0;
        fixtureDef.filter.categoryBits = CATEGORY;
        fixtureDef.filter.maskBits = MASK;
        body.createFixture(fixtureDef);
        bodyShape.dispose();
    }

    @Override
    public void setDamage(int dmg){
        damage = dmg;
    }

    @Override
    public int getDamage(){
        return damage;
    }

    @Override
    public boolean doDamage(int damage) {
        life -= damage;
        if(life <=0){
            die();
            return true;
        }
        return false;
    }

    @Override
    public boolean draw(SpriteBatch batch) {
        sprite.draw(batch);
        return false;
    }

    public void die(){
        explosion = new Explosion(body.getPosition(),world,batch);
        if(PreferencesSB.SOUNDS_ENABLE) {
            explosionSound.play(0.8f);
        }
    }

    @Override
    public Body getBody() {
        return body;
    }

    public Explosion getExplosion(){
        return explosion;
    }
}
