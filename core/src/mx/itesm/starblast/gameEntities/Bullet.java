package mx.itesm.starblast.gameEntities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import mx.itesm.starblast.Constant;

public class Bullet implements IPlayableEntity{

    float density = 0.1f;
    float restitution = 0.1f;
    public Body body;
    Sprite sprite;
    private static float VELOCITY = 10;
    boolean isEnemy = false;
    int damage;

    Bullet(float x, float y, World world, float angle, boolean enemy,int damage) {
        isEnemy = enemy;

        sprite = new Sprite(Constant.MANAGER.get("GameScreen/"+(enemy? "BulletSpriteEnemy.png": "BulletSprite.png"), Texture.class));
        sprite.setCenter(x, y);
        sprite.setRotation(angle);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        body = world.createBody(bodyDef);


        makeFixture(density, restitution);

        body.setLinearVelocity(MathUtils.cosDeg(angle) * VELOCITY,
                MathUtils.sinDeg(angle) * VELOCITY);
        body.setUserData(this);

        this.damage = damage;
    }

    Bullet(Vector2 v, World world, float angle, boolean enemy,int damage) {
        this(v.x, v.y, world, angle,enemy, damage);
    }

    void makeFixture(float density, float restitution) {
        while (body.getFixtureList().size > 0){
            body.destroyFixture(body.getFixtureList().first());
        }
        CircleShape bodyShape = new CircleShape();

        float w = Constant.toWorldSize(sprite.getWidth() * sprite.getScaleX() / 2f);

        bodyShape.setRadius(w);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = density;
        fixtureDef.restitution = restitution;
        fixtureDef.shape = bodyShape;
        fixtureDef.friction = 0;
        fixtureDef.filter.categoryBits = isEnemy ? Constant.CATEGORY_BULLET_ENEMY :
                                                    Constant.CATEGORY_BULLET_PLAYER;
        fixtureDef.filter.maskBits = isEnemy ? Constant.MASK_BULLET_ENEMY :
                                                Constant.MASK_BULLET_PLAYER;
        body.createFixture(fixtureDef);

        bodyShape.dispose();
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
        return true;
    }

    public boolean draw(SpriteBatch batch) {
        sprite.setCenter(Constant.toScreenSize(body.getPosition().x),
                Constant.toScreenSize(body.getPosition().y));
        sprite.draw(batch);
        return false;
    }

    @Override
    public Body getBody() {
        return body;
    }

    @Override
    public float getX() {
        return Constant.toScreenSize(body.getPosition().x);
    }

    @Override
    public float getY() {
        return Constant.toScreenSize(body.getPosition().y);
    }

}
