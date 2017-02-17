package mx.itesm.starblast;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;

import java.util.ArrayList;


/**
 * Created by Ian Neumann on 16/02/2017.
 */

public abstract class NavesEspaciales implements INaveEspacial {

    private final float dispararCooldown = 500;
    private float disparoAnterior = 0;
    private float theta;
    private float aceleracion;
    private GeneralSprite sprite;
    private Queue<Vector2> velocidadesAnteriores;
    private float velocidad;
    
    public NavesEspaciales(String ubicacion, float x, float y) {
        sprite = new GeneralSprite(ubicacion,x,y);
        this.sprite.getSprite().setRotation(-90);
        this.velocidad = 0;
        velocidadesAnteriores = new Queue<Vector2>(10);
        for(int i = 0; i< 10;i++){
            velocidadesAnteriores.addFirst(new Vector2(0,0));
        }
    }

    protected abstract void generarDisparo();

    @Override
    public void disparar(float time) {
        if(disparoAnterior+dispararCooldown < time){
            generarDisparo();
        }
    }

    private void mover(){
        Sprite sprite = this.sprite.getSprite();
        Vector2 v = new Vector2(
                (float)Math.cos(Math.toRadians(sprite.getRotation())),
                (float)Math.sin(Math.toRadians(sprite.getRotation())));
        while (velocidadesAnteriores.size > 30){
            velocidadesAnteriores.removeLast();
        }
        float sumaX = 0;
        float sumaY = 0;
        int cont = 10;
        for(Vector2 vector:velocidadesAnteriores){
            sumaX += vector.x*cont*0.01;
            sumaY += vector.y*cont*0.01;
            cont--;
        }
        sprite.setX(sprite.getX()+v.x*velocidad+sumaX);
        sprite.setY(sprite.getY()+v.y*velocidad+sumaY);
    }

    @Override
    public void acelerar(float aceleracion) {
        Sprite sprite = this.sprite.getSprite();
        Vector2 v = new Vector2(
                (float)Math.cos(Math.toRadians(sprite.getRotation())),
                (float)Math.sin(Math.toRadians(sprite.getRotation())));
        if(aceleracion > 10){
            aceleracion = 10;
        }

        velocidad += aceleracion;
        if(velocidad > 20){
            velocidad = 20;
        }

        float anguloTan = theta <0?-90:90;

        mover();
    }


    private void girar(float angulo,float delta) {
        Sprite sprite = this.sprite.getSprite();
        float constanteGiro = 3;
        float rangoGiro;
        if(velocidad == 0){
            rangoGiro = 10;
        }
        else {
            rangoGiro = constanteGiro / 1;
        }
        if(rangoGiro > 10){
            rangoGiro = 10;
        }
        /*if(Math.abs(theta-sprite.getRotation()) < 5){
            theta += (theta-sprite.getRotation());
        }
        else */
        if(theta < 0){
            theta = 360+theta;
        }
        theta%=360;
        if(angulo < 0){
            angulo = 360+angulo;
        }
        angulo%=360;
        if(sprite.getRotation() < 0){
            sprite.setRotation(360+sprite.getRotation());
        }
        if((sprite.getRotation()-180 <= angulo&&angulo <= sprite.getRotation()) || 360+(sprite.getRotation()-180) <= angulo){
            if(Math.abs(sprite.getRotation()-angulo) > rangoGiro) {
                this.theta -= rangoGiro;
            }
            else{
                this.theta -=Math.abs(sprite.getRotation()-angulo);
            }
        }
        else{
            if(Math.abs(sprite.getRotation()-angulo) > rangoGiro) {
                this.theta += rangoGiro;
            }
            else{
                this.theta +=Math.abs(sprite.getRotation()-angulo);
            }
        }
        sprite.setRotation(this.theta);
    }

    public void draw(SpriteBatch batch){
        sprite.draw(batch);
    }

    @Override
    public void mover(Vector2 punto,float delta){
        Sprite sprite = this.sprite.getSprite();
        float angulo =0;
        angulo = (float)Math.atan2(punto.y-sprite.getY(),punto.x-sprite.getX());
        angulo = (float) (angulo*180/(Math.PI));
        angulo%=360;
        girar(angulo,delta);
        acelerar(100*delta);


        velocidadesAnteriores.addFirst(new Vector2(
                (float)Math.cos(Math.toRadians(sprite.getRotation()))*velocidad*0.1f,
                (float)Math.sin(Math.toRadians(sprite.getRotation()))*velocidad*0.1f));
    }

    public void escalar(float escala){
        this.sprite.escalar(escala);
    }
}
