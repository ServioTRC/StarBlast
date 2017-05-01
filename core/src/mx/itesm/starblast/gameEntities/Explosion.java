package mx.itesm.starblast.gameEntities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import mx.itesm.starblast.Constant;

/**
 * Created by Ian Neumann on 26/04/2017.
 */

public class Explosion extends PlayableEntity{

    private int damage;

    private Body body;
    private Vector2 position;

    private AutoAnimation  animation;
    private World world;

    private boolean created = false;

    public Explosion(Vector2 position, World world, SpriteBatch batch,int damage) {
        animation = new AutoAnimation(Constant.MANAGER.get("Animations/ExplosionFrames.png", Texture.class), 0.15f, Constant.toScreenSize(position.x), Constant.toScreenSize(position.y), Constant.EXPLOSION_SIZE_X, Constant.EXPLOSION_SIZE_Y,batch);
        this.position = position;
        this.world = world;
        this.damage = damage;
    }

    public Explosion(Vector2 position, World world, AutoAnimation animation, int damage) {
        this.animation = animation;
        this.position = position;
        this.world = world;
        this.damage = damage;
    }

    public void createExplosion(){
        if(created){
            return;
        }
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position.x,position.y);
        body = world.createBody(bodyDef);

        CircleShape bodyShape = new CircleShape();
        bodyShape.setRadius(Constant.toWorldSize(Constant.EXPLOSION_SIZE_X)/2);

        makeFixture(0.5f,0.5f,body,bodyShape,Constant.CATEGORY_EXPLOSIONS,Constant.MASK_EXPLOSIONS,true);

        body.setUserData(this);
//        makeFixture(0.5f,0.5f);

        created = true;
    }

    /*private void makeFixture(float density, float restitution){
        while (body.getFixtureList().size > 0){
            body.destroyFixture(body.getFixtureList().first());
        }
        CircleShape bodyShape = new CircleShape();
        bodyShape.setRadius(Constant.toWorldSize(Constant.EXPLOSION_SIZE_X)/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;
        fixtureDef.density = density;
        fixtureDef.restitution = restitution;
        fixtureDef.shape = bodyShape;
        fixtureDef.friction = 0;
        fixtureDef.filter.categoryBits = Constant.CATEGORY_EXPLOSIONS;
        fixtureDef.filter.maskBits = Constant.MASK_EXPLOSIONS;

        body.createFixture(fixtureDef);
        body.setUserData(this);

        bodyShape.dispose();
    }
*/
    @Override
    public void setDamage(int dmg) {
        damage = dmg;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public boolean doDamage(int damage) {
        return false;
    }

    @Override
    public boolean draw(SpriteBatch batch) {
        return animation.draw(batch, Gdx.graphics.getDeltaTime());
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

    public boolean isCreated(){
        return created;
    }
}
