package mx.itesm.starblast;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
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

    private Queue<Vector2> velocidadesAnteriores;
    private float velocidad;


    public NaveEnemiga(String ubicacion, float x, float y,World world) {
        COOLDOWN_DISPARO = 500;
        sprite = new Sprite(new Texture(ubicacion));
        sprite.setRotation(-90);
        velocidad = 0;
        velocidadesAnteriores = new Queue<Vector2>(MOVEMENT_OFFSET);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x,y);
        bodyDef.angle = -90;
        body = world.createBody(bodyDef);

        makeFixture(0.1f,0.1f);
    }

    private void mover(){
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

    @Override
    public void mover(Vector2 target,float delta){
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
        Vector2 v = new Vector2(
                (float)Math.cos(Math.toRadians(sprite.getRotation()))*velocidad*100,
                (float)Math.sin(Math.toRadians(sprite.getRotation()))*velocidad*100);
        body.setLinearVelocity(v);
        sprite.setCenter(body.getPosition().x,body.getPosition().y);
    }
}
