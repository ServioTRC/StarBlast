package mx.itesm.starblast;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Queue;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static java.lang.Math.*;

/**
 * Created by Ian Neumann on 16/02/2017.
 */

public class NaveJugador extends NavesEspaciales {


    public enum EstadoMovimiento{
        GIRANDO,
        PARADO
    };

    private final float RANGO_GIRO = 80;
    private final float VELOCIDAD_MAX = 8;

    private final float CONSTANTE_FRENADO = 0.9f;

    private final float RANGO_GIRO_MAX = 2;
    private final long COOLDOWN_DISPARO = 500;

    private long disparoAnterior = 0;
    private GeneralSprite sprite;
    private float aceleracion;

    private EstadoMovimiento estado = EstadoMovimiento.PARADO;
    private float porcentajeGiro;
    private float porcentajeAceleracion;
    private float velocidad;
    private float theta;

    private BodyDef bodyDef;
    private Body body;

    public NaveJugador(String ubicacion,float x,float y) {
        sprite = new GeneralSprite(ubicacion,x,y);
        this.sprite.getSprite().setRotation(90);
        this.aceleracion= 0;
        this.velocidad = 0;
        this.bodyDef = new BodyDef();
        this.bodyDef.type = BodyDef.BodyType.KinematicBody;
        this.bodyDef.position.set(x,y);
        this.bodyDef.angle = 90;

        //makeFixture(0.7f,0.7f);
    }

    /*private void makeFixture(float density,float restitution){

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
    }*/

    private void disparar(){

    }

    @Override
    public void disparar(long time) {
        if(disparoAnterior+COOLDOWN_DISPARO >= time){
            disparar();
        }
    }

    @Override
    public void acelerar(float porcentaje) {
        this.porcentajeAceleracion = porcentaje;
    }

    @Override
    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    @Override
    public void mover(Vector2 vector, float delta) {
        switch (estado){
            case GIRANDO:
                girar();
                break;
            default:
                break;
        }

        Vector2 destino = new Vector2(porcentajeGiro * -1, porcentajeAceleracion);

        aceleracion = (float)sqrt(destino.x*destino.x+destino.y*destino.y);

        velocidad = min(velocidad,VELOCIDAD_MAX);

        if(aceleracion == 0){
            velocidad*=CONSTANTE_FRENADO;
            vector = new Vector2((float) (velocidad * cos(theta)), (float) (velocidad * sin(theta)));
        }
        else {
            vector = new Vector2((float) (velocidad * cos(theta)), (float) (velocidad * sin(theta)));
            vector.add(porcentajeGiro * -1, porcentajeAceleracion);
        }
        theta = (float)atan2(vector.y,vector.x);
        velocidad = (float)sqrt(vector.x*vector.x+vector.y*vector.y);


        Sprite sprite = this.sprite.getSprite();

        sprite.setPosition(vector.x+sprite.getX(),vector.y+sprite.getY());
    }

    @Override
    public Body getBody() {
        return body;
    }


    public void girar(float porcentaje){
        this.porcentajeGiro = porcentaje*-1;
        this.estado = porcentaje == 0?EstadoMovimiento.PARADO:EstadoMovimiento.GIRANDO;
    }

    private void girar(){
        Sprite sprite = this.sprite.getSprite();
        sprite.setRotation(sprite.getRotation()+RANGO_GIRO_MAX*porcentajeGiro);
        sprite.setRotation(min(sprite.getRotation(),90+RANGO_GIRO/2));
        sprite.setRotation(max(sprite.getRotation(),90-RANGO_GIRO/2));
    }

    @Override
    public void escalar(float escala) {
        sprite.escalar(escala);
    }

    @Override
    public float getX() {
        return sprite.getSprite().getX();
    }

    @Override
    public float getY() {
        return sprite.getSprite().getY();
    }
}
