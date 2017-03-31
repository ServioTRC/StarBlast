package mx.itesm.starblast;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Iterator;

class Bullet implements IPlayableEntity{

    public Body body;
    private CircleShape bodyShape;
    private Sprite sprite;
    private static float VELOCITY = 10;
    private boolean isEnemy = false;
    int damage;
    boolean destruido;

    Bullet(float x, float y, World world, float angle, boolean enemy,int damage) {

        destruido = false;

        isEnemy = enemy;
        sprite = new Sprite(Constantes.MANAGER.get("PantallaJuego/"+(enemy? "BulletSpriteEnemigo.png": "BulletSprite.png"), Texture.class));
        sprite.setCenter(x, y);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        body = world.createBody(bodyDef);
        makeFixture(0.1f, 1f);
        sprite.setRotation(angle - 90);
        body.setLinearVelocity(MathUtils.cosDeg(angle) * VELOCITY,
                MathUtils.sinDeg(angle) * VELOCITY);
        body.setUserData(this);

        this.damage = damage;
    }

    Bullet(Vector2 v, World world, float angle, boolean enemy,int damage) {
        this(v.x, v.y, world, angle,enemy, damage);
    }

    private void makeFixture(float density, float restitution) {
        if(destruido){
            return;
        }

        while (body.getFixtureList().size > 0){
            body.destroyFixture(body.getFixtureList().first());
        }
        bodyShape = new CircleShape();

        float w = Constantes.toWorldSize(sprite.getWidth() * sprite.getScaleX() / 2f);

        bodyShape.setRadius(w);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = density;
        fixtureDef.restitution = restitution;
        fixtureDef.shape = bodyShape;
        fixtureDef.friction = 0;
        fixtureDef.filter.categoryBits = isEnemy ? Constantes.CATEGORY_BULLET_ENEMY :
                                                    Constantes.CATEGORY_BULLET;
        fixtureDef.filter.maskBits = isEnemy ? Constantes.MASK_BULLET_ENEMY :
                                                Constantes.MASK_BULLET;
        body.createFixture(fixtureDef);
    }

    @Override
    public void setDamage(int dmg){
        damage = dmg;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public boolean doDamage(int damage) {
        //TODO do something like remove the instance or whatever
        bodyShape.dispose();
        return true;
    }

    public void draw(SpriteBatch batch) {
        //TODO ponerlo en la punta, no en el centro
        sprite.setCenter(Constantes.toScreenSize(body.getPosition().x),
                Constantes.toScreenSize(body.getPosition().y));
        sprite.draw(batch);
    }

    @Override
    public Body getBody() {
        return body;
    }

    @Override
    public float getX() {
        return Constantes.toScreenSize(body.getPosition().x);
    }

    @Override
    public float getY() {
        return Constantes.toScreenSize(body.getPosition().y);
    }

}
