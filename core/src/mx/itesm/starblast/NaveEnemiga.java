package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Queue;


/**
 * Created by Ian Neumann on 16/02/2017.
 */

public class NaveEnemiga extends NavesEspaciales {

    private static final int ACELERACION_MAX = 10;
    private static final int ACELERACION_MIN = -10;
    private static final int RANGO_GIRO_MAX = 3;
    private static final int VELOCIDAD_MAX = 7;
    private static final int IMPULSO = 30;
    private final int MOVEMENT_OFFSET = 35;

    private final float dispararCooldown = 500;
    private float disparoAnterior = 0;
    private GeneralSprite sprite;
    private Queue<Vector2> velocidadesAnteriores;
    private float velocidad;

    private Body body;

    private CircleShape bodyShape;

    public NaveEnemiga(String ubicacion, float x, float y,World world) {
        sprite = new GeneralSprite(ubicacion,x,y);
        this.sprite.getSprite().setRotation(-90);
        this.velocidad = 0;
        velocidadesAnteriores = new Queue<Vector2>(MOVEMENT_OFFSET);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x,y);
        bodyDef.angle = -90;
        body = world.createBody(bodyDef);

        makeFixture(0.1f,0.1f);
    }

    private void makeFixture(float density,float restitution){

        for(Fixture fix: body.getFixtureList()){
            body.destroyFixture(fix);
        }
        bodyShape = new CircleShape();
        Sprite sprite = this.sprite.getSprite();

        float w=sprite.getWidth()*sprite.getScaleX()/2f;

        bodyShape.setRadius(w);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density=density;
        fixtureDef.restitution=restitution;
        fixtureDef.shape=bodyShape;
        fixtureDef.friction = 0;

        body.createFixture(fixtureDef);
    }


    private void generarDisparo() {

    }


    @Override
    public void disparar(long time) {
        if(disparoAnterior+dispararCooldown < time){
            disparoAnterior = time;
            generarDisparo();
        }
    }

    private void mover(){
        Sprite sprite = this.sprite.getSprite();
        Vector2 v = new Vector2(
                (float)Math.cos(Math.toRadians(sprite.getRotation())),
                (float)Math.sin(Math.toRadians(sprite.getRotation())));
        while (velocidadesAnteriores.size > MOVEMENT_OFFSET){
            velocidadesAnteriores.removeLast();
        }
        float sumaX = 0;
        float sumaY = 0;
        int cont = MOVEMENT_OFFSET;
        for(Vector2 vector:velocidadesAnteriores){
            sumaX += vector.x*cont;
            sumaY += vector.y*cont;
            cont--;
        }
        sprite.setX((sprite.getX()+v.x*velocidad+sumaX));
        sprite.setY((sprite.getY()+v.y*velocidad+sumaY));
    }

    @Override
    public void acelerar(float aceleracion) {
        aceleracion = Math.min(aceleracion,ACELERACION_MAX);
        aceleracion = Math.max(aceleracion,ACELERACION_MIN);

        velocidad += aceleracion;

        velocidad = Math.min(velocidad,VELOCIDAD_MAX);

        mover();
    }

    private void girar(float angulo) {
        Sprite sprite = this.sprite.getSprite();

        if(angulo < 0){
            angulo = 360+angulo;
        }
        angulo%=360;

        if(sprite.getRotation() < 0){
            sprite.setRotation(360+sprite.getRotation());
        }
        float theta = sprite.getRotation();
        if(theta < 0){
            theta = 360+angulo;
        }
        theta%=360;
        int signo;
        if((sprite.getRotation()-180 <= angulo&&angulo <= sprite.getRotation()) || 360+(sprite.getRotation()-180) <= angulo) {
            signo = -1;
        }
        else {
            signo = 1;
        }
        if(Math.abs(sprite.getRotation()-angulo) > RANGO_GIRO_MAX) {
            theta += signo*RANGO_GIRO_MAX;
        }
        else{
            theta += signo*Math.abs(sprite.getRotation()-angulo);
        }

        sprite.setRotation(theta);
    }

    public void draw(SpriteBatch batch){
        sprite.draw(batch);
    }

    @Override
    public void mover(Vector2 target,float delta){

        Sprite sprite = this.sprite.getSprite();
        float angulo;
        angulo = (float)Math.atan2(target.y-sprite.getY(),target.x-sprite.getX());
        angulo = (float) (angulo*180/(Math.PI));
        angulo%=360;
        girar(angulo);



        acelerar(IMPULSO * delta);


        velocidadesAnteriores.addFirst(new Vector2(
                (float)Math.cos(Math.toRadians(sprite.getRotation()))*velocidad*0.001f,
                (float)Math.sin(Math.toRadians(sprite.getRotation()))*velocidad*0.001f));

        updateBody();
    }

    private void updateBody() {
        Sprite sprite = this.sprite.getSprite();
        Vector2 v = new Vector2(
                (float)Math.cos(Math.toRadians(sprite.getRotation()))*velocidad*100,
                (float)Math.sin(Math.toRadians(sprite.getRotation()))*velocidad*100);
        body.setLinearVelocity(v);
        sprite.setCenter(body.getPosition().x,body.getPosition().y);
    }

    @Override
    public Body getBody() {
        return body;
    }

    public void escalar(float escala){
        bodyShape.dispose();

        this.sprite.escalar(escala);
        bodyShape = new CircleShape();
        this.bodyShape.setRadius(sprite.getSprite().getWidth()*sprite.getSprite().getScaleX()/2);
        makeFixture(0.1f,0.1f);
    }

    @Override
    public float getX(){
        return body.getPosition().x;
    }
    @Override
    public float getY(){
        return body.getPosition().y;
    }

    public Shape getShape(){
        return bodyShape;
    }
}
