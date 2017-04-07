package mx.itesm.starblast;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.math.MathUtils;
import static java.lang.Math.*;

class NaveJugador extends NavesEspaciales {

    private enum EstadoMovimiento{
        GIRANDO,
        PARADO
    }

    private final float RANGO_GIRO = 80;
    private final float VELOCIDAD_MAX = 8;
    private final float CONSTANTE_FRENADO = 0.97f;
    private final float RANGO_GIRO_MAX = 2;

    private float aceleracion;

    private EstadoMovimiento estado = EstadoMovimiento.PARADO;
    private float porcentajeGiro;
    private float velocidad;
    private float theta;
    float vidaTotal;

    NaveJugador(Texture textura,float x,float y,World world) {
        super(textura,x,y,world,90,0.1f,0.7f, false);

        CATEGORY = Constants.CATEGORY_PLAYER;
        MASK = Constants.MASK_PLAYER;
        COOLDOWN_DISPARO = 100;
        BULLET_DAMAGE = 10;

        velocidad = 0;
        aceleracion = 0;
        damage = 20;

        vidaTotal = vida;
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

        aceleracion = (float)sqrt(porcentajeGiro*porcentajeGiro + porcentajeAceleracion*porcentajeAceleracion);

        velocidad = body.getLinearVelocity().len();
        velocidad = min(velocidad, VELOCIDAD_MAX);
        theta = body.getAngle();
        if (aceleracion == 0) {
            vector = body.getLinearVelocity().scl(CONSTANTE_FRENADO);
        } else {
            vector = body.getLinearVelocity().scl(2);
            vector.add(porcentajeGiro * -1, porcentajeAceleracion);
        }
        theta = MathUtils.atan2(vector.y, vector.x);
        velocidad = vector.len();

        if(aceleracion != 0) {
            body.applyForceToCenter(new Vector2(porcentajeGiro * -1, porcentajeAceleracion), true);
        }
        body.setLinearVelocity(body.getLinearVelocity().scl(CONSTANTE_FRENADO));
        sprite.setCenter(Constants.toScreenSize(body.getPosition().x), Constants.toScreenSize(body.getPosition().y));
    }


    void girar(float porcentaje){
        this.porcentajeGiro = porcentaje*-1;
        this.estado = porcentaje == 0?EstadoMovimiento.PARADO:EstadoMovimiento.GIRANDO;
    }

    private void girar(){
        sprite.setRotation(sprite.getRotation()+RANGO_GIRO_MAX*porcentajeGiro);
        sprite.setRotation(min(sprite.getRotation(),90+RANGO_GIRO/2));
        sprite.setRotation(max(sprite.getRotation(),90-RANGO_GIRO/2));
    }

}
