package mx.itesm.starblast;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Ian Neumann on 16/02/2017.
 */

public class NaveEnemiga extends NavesEspaciales {

    private static final int ACELERACION_MAX = 10;
    private static final int ACELERACION_MIN = -10;
    private static final int RANGO_GIRO_MAX = 3;
    private static final int VELOCIDAD_MAX = 7;
    private static final int IMPULSO = 30;
    private static final int DISTANCIA_FRENADO = 8000;
    private final int MOVEMENT_OFFSET = 35;

    private final float dispararCooldown = 500;
    private float disparoAnterior = 0;
    private GeneralSprite sprite;
    private Queue<Vector2> velocidadesAnteriores;
    private float velocidad;

    public NaveEnemiga(String ubicacion, float x, float y) {
        sprite = new GeneralSprite(ubicacion,x,y);
        this.sprite.getSprite().setRotation(-90);
        this.velocidad = 0;
        velocidadesAnteriores = new Queue<Vector2>(MOVEMENT_OFFSET);
    }


    private void generarDisparo() {
        throw new NotImplementedException();
    }


    @Override
    public void disparar(float time) {
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
    public void mover(Vector2 objetivo,float delta){

        Sprite sprite = this.sprite.getSprite();
        float angulo;
        angulo = (float)Math.atan2(objetivo.y-sprite.getY(),objetivo.x-sprite.getX());
        angulo = (float) (angulo*180/(Math.PI));
        angulo%=360;
        girar(angulo);

        if(objetivo.dst2(sprite.getX(),sprite.getY()) < DISTANCIA_FRENADO){
            if(velocidad > 0) {
                acelerar(-IMPULSO * delta);
            }
        }
        else {
            acelerar(IMPULSO * delta);
        }

        velocidadesAnteriores.addFirst(new Vector2(
                (float)Math.cos(Math.toRadians(sprite.getRotation()))*velocidad*0.001f,
                (float)Math.sin(Math.toRadians(sprite.getRotation()))*velocidad*0.001f));
    }

    public void escalar(float escala){
        this.sprite.escalar(escala);
    }

    @Override
    public float getX(){
        return sprite.getSprite().getX();
    }
    @Override
    public float getY(){
        return sprite.getSprite().getY();
    }
}
