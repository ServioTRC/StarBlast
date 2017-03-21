package mx.itesm.starblast;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class NaveEnemiga extends NavesEspaciales {

    private static final int ACELERACION_MAX = 10;
    private static final int ACELERACION_MIN = -10;
    private static final int RANGO_GIRO_MAX = 3;
    private static final int VELOCIDAD_MAX = 7;
    private static final int IMPULSO = 30;

    private float velocidad;


    public NaveEnemiga(String ubicacion, float x, float y,World world) {
        super(world);
        COOLDOWN_DISPARO = 500;
        CATEGORY = Constantes.CATEGORY_ENEMY;
        MASK = Constantes.MASK_ENEMY;
        sprite = new Sprite(new Texture(ubicacion));
        sprite.setRotation(-90);
        velocidad = 0;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(Constantes.toWorldSize(x),Constantes.toWorldSize(y));
        bodyDef.angle = -90;
        body = world.createBody(bodyDef);

        makeFixture(0.1f,0.1f);
    }

    @Override
    public void acelerar(float aceleracion) {
        aceleracion = Math.min(aceleracion,ACELERACION_MAX);
        aceleracion = Math.max(aceleracion,ACELERACION_MIN);

        velocidad += aceleracion;

        velocidad = Math.min(velocidad,VELOCIDAD_MAX);

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

        updateBody();
    }

    private void updateBody() {
        Vector2 v = new Vector2(
                (float)Math.cos(Math.toRadians(sprite.getRotation()))*velocidad,
                (float)Math.sin(Math.toRadians(sprite.getRotation()))*velocidad);
        body.setLinearVelocity(v);
        sprite.setCenter(Constantes.toScreenSize(body.getPosition().x),Constantes.toScreenSize(body.getPosition().y));
    }
}
