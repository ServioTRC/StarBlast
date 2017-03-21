package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
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

    public static final String BULLET_SPRITE = "PantallaJuego/BulletSprite.png";
    private static Texture textura;
    private Body body;
    private CircleShape bodyShape;
    private Sprite sprite;
    private static float VELOCITY = 10;

    public static void CargarTextura(){
        textura = new Texture(BULLET_SPRITE);
    }

    public Bullet(float x, float y, World world, float angle){
        sprite = new Sprite(textura);
        sprite.setCenter(x,y);
//        sprite.setCenter(Constantes.ANCHO_PANTALLA/2,Constantes.ALTO_PANTALLA/2);
        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x,y);
//        bodyDef.position.set(Constantes.toWorldSize(Constantes.ANCHO_PANTALLA/2),
//                Constantes.toWorldSize(Constantes.ALTO_PANTALLA/2));
        body = world.createBody(bodyDef);
        makeFixture(0.1f,0.7f);
        body.setBullet(true);
        sprite.setRotation(angle-90);
        body.setLinearVelocity(MathUtils.cosDeg(angle)*VELOCITY,
                            MathUtils.sinDeg(angle)*VELOCITY);
//        Gdx.app.log("Bullet:","Angle: "+angle+" cos: "+MathUtils.cosDeg(angle));
//        Gdx.app.log("Bullet:","LV: "+body.getLinearVelocity());
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
        fixtureDef.filter.categoryBits = Constantes.CATEGORY_BULLET;
        fixtureDef.filter.maskBits = Constantes.MASK_BULLET;
        body.createFixture(fixtureDef);
    }

    public void draw(SpriteBatch batch){
        //TODO ponerlo en la punta, no en el centro
        sprite.setCenter(Constantes.toScreenSize(body.getPosition().x),
                Constantes.toScreenSize(body.getPosition().y));
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
