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

/**
 * Created by delag on 3/20/2017.
 */

public class Bullet {

    private static Texture textura = new Texture(Constantes.BULLET_SPRITE);
    private Body body;
    private CircleShape bodyShape;
    private Sprite sprite;
    private static float VELOCITY = 10;

    public Bullet(float x, float y, World world, float angle){
        sprite = new Sprite(textura);
        sprite.setCenter(x,y);
        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x,y);
        body = world.createBody(bodyDef);
        makeFixture(0.7f,0.7f);
        body.setBullet(true);
        body.setLinearVelocity(MathUtils.cosDeg(angle)*VELOCITY,
                            MathUtils.sinDeg(angle)*VELOCITY);
    }

    public Bullet(Vector2 v, World world, float angle){
        this(v.x,v.y,world,angle);
    }

    private void makeFixture(float density,float restitution){

        for(Fixture fix: body.getFixtureList()){
            body.destroyFixture(fix);
        }
        bodyShape = new CircleShape();

        float w=Constantes.toWorldSize(sprite.getWidth()*sprite.getScaleX()/2f);

        bodyShape.setRadius(w);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density=density;
        fixtureDef.restitution=restitution;
        fixtureDef.shape=bodyShape;
        fixtureDef.friction = 0;

        body.createFixture(fixtureDef);
    }

    public void draw(SpriteBatch batch){
        sprite.draw(batch);
    }

    public Shape getShape(){
        return bodyShape;
    }

    public float getX() {
        return Constantes.toScreenSize(body.getPosition().x);
    }

    public float getY() {
        return Constantes.toScreenSize(body.getPosition().y);
    }
}
