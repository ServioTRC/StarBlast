package mx.itesm.starblast;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

class NaveEnemiga extends NavesEspaciales {

    private final int ACELERACION_MAX = 10;
    private final int ACELERACION_MIN = -10;
    private final int RANGO_GIRO_MAX = 2;
    private final int VELOCIDAD_MAX = 4;
    private final int IMPULSO = 30;
    private final float CONSTANTE_FRENADO = 0.97f;

    private float velocidad;
    private boolean puedeDisparar;

    private Vector2  previo;


    NaveEnemiga(String ubicacion, float x, float y,World world) {
        super(ubicacion,x,y,world,-90,0.1f,0.7f);


        CATEGORY = Constantes.CATEGORY_ENEMY;
        MASK = Constantes.MASK_ENEMY;
        COOLDOWN_DISPARO = 500;

        vida = 30;
        velocidad = 0;
        damage = 5;
        puedeDisparar = false;
        previo = new Vector2(0,0);
    }

    @Override
    public Bullet disparar(long time,boolean enemy) {
        if(puedeDisparar){
            return super.disparar(time,enemy);
        }
        return null;
    }

    @Override
    public void acelerar(float aceleracion) {
        aceleracion = Math.min(aceleracion, ACELERACION_MAX);
        aceleracion = Math.max(aceleracion, ACELERACION_MIN);

        velocidad = body.getLinearVelocity().len();
        velocidad += aceleracion;
        velocidad = Math.min(velocidad,VELOCIDAD_MAX);

    }

    private void girar(float angulo) {
        angulo += 360;
        angulo %= 360;

        if(sprite.getRotation() < 0){
            sprite.setRotation(360+sprite.getRotation());
        }
        float theta = sprite.getRotation();
        theta += 360;
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
            puedeDisparar = false;
        }
        else{
            theta += signo*Math.abs(sprite.getRotation()-angulo);
            puedeDisparar = true;
        }

        sprite.setRotation(theta);
    }

    @Override
    public void mover(Vector2 target,float delta){
        float deltaX;
        float deltaY;

        if(Math.abs(Constantes.toWorldSize(target.x)-body.getPosition().x) < Math.abs(body.getLinearVelocity().x)){
            deltaX = Constantes.toWorldSize(target.x)-body.getPosition().x;
        }
        else{
            deltaX = Constantes.toWorldSize(target.x)-(body.getLinearVelocity().x+body.getPosition().x);
        }

        if(Math.abs(Constantes.toWorldSize(target.y)-body.getPosition().y) < Math.abs(body.getLinearVelocity().y)){
            deltaY = Constantes.toWorldSize(target.y)-body.getPosition().y;
        }
        else{
            deltaY = Constantes.toWorldSize(target.y)-(body.getLinearVelocity().y+body.getPosition().y);
        }

        float angulo;
        angulo = MathUtils.atan2(deltaY,deltaX);
        angulo = (angulo*180/(MathUtils.PI));
        angulo+=360;
        angulo%=360;
        girar(angulo);

        acelerar(IMPULSO * delta);

        updateBody(angulo);
    }

    private void updateBody(float angulo) {
        Vector2 v = new Vector2(
                MathUtils.cosDeg(sprite.getRotation()),
                MathUtils.sinDeg(sprite.getRotation()));
        float hip = body.getLinearVelocity().len();


        //if(Math.abs(body.getLinearVelocity().angle()-angulo) < 20*RANGO_GIRO_MAX || hip < 3) {
            body.applyForceToCenter(v.scl(0.3f), true);
        /*}
        else{
            body.setLinearVelocity(body.getLinearVelocity().scl(CONSTANTE_FRENADO));
        }*/
        if(hip > VELOCIDAD_MAX){
            body.setLinearVelocity(body.getLinearVelocity().scl(VELOCIDAD_MAX/hip));
        }
        sprite.setCenter(Constantes.toScreenSize(body.getPosition().x),Constantes.toScreenSize(body.getPosition().y));
    }
}
