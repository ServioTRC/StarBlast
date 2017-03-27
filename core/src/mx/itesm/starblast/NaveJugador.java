package mx.itesm.starblast;

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

    NaveJugador(String ubicacion,float x,float y,World world) {
        super(ubicacion,x,y,world,90,0.1f,0.7f);

        CATEGORY = Constantes.CATEGORY_PLAYER;
        MASK = Constantes.MASK_PLAYER;
        COOLDOWN_DISPARO = 100;

        velocidad = 0;
        aceleracion = 0;
        damage = 20;
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

        aceleracion = destino.len();

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
//        if(!body.getLinearVelocity().isZero(0.05f))Gdx.app.log("Nave Jugador:","LV: "+body.getLinearVelocity());
        //body.setLinearVelocity(vector.scl(0.5f));
        /*body.applyForceToCenter(vector.scl(0.5f),true);*/
        /*float hipotenusa = vector.len();
        if(hipotenusa > VELOCIDAD_MAX){
            float scale = hipotenusa/VELOCIDAD_MAX;
            body.setLinearVelocity(body.getLinearVelocity().scl(1/scale*0.5f));
        }*/
        if(aceleracion != 0) {
            body.applyForceToCenter(new Vector2(porcentajeGiro * -1, porcentajeAceleracion), true);
        }
        body.setLinearVelocity(body.getLinearVelocity().scl(CONSTANTE_FRENADO));
        sprite.setCenter(Constantes.toScreenSize(body.getPosition().x), Constantes.toScreenSize(body.getPosition().y));
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
