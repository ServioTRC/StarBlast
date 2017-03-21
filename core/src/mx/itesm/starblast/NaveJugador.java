package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import static java.lang.Math.*;

public class NaveJugador extends NavesEspaciales {


    private enum EstadoMovimiento{
        GIRANDO,
        PARADO
    }

    private final float RANGO_GIRO = 80;
    private final float VELOCIDAD_MAX = 8;

    private final float CONSTANTE_FRENADO = 0.9f;

    private final float RANGO_GIRO_MAX = 2;

    private float aceleracion;

    private EstadoMovimiento estado = EstadoMovimiento.PARADO;
    private float porcentajeGiro;
    private float velocidad;
    private float theta;

    public NaveJugador(String ubicacion,float x,float y,World world) {
        super(world);
        COOLDOWN_DISPARO = 500;
        sprite = new Sprite(new Texture(ubicacion));
        this.sprite.setRotation(90);
        this.aceleracion= 0;
        this.velocidad = 0;

        this.bodyDef = new BodyDef();
        this.bodyDef.type = BodyDef.BodyType.DynamicBody;
        this.bodyDef.position.set(Constantes.toWorldSize(x),Constantes.toWorldSize(y));
        this.bodyDef.angle = 90;

        body = world.createBody(bodyDef);

        makeFixture(0.7f,0.7f);
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

        aceleracion = (float) sqrt(destino.x * destino.x + destino.y * destino.y);

        velocidad = min(velocidad, VELOCIDAD_MAX);

        if (aceleracion == 0) {
            velocidad *= CONSTANTE_FRENADO;
            vector = new Vector2((float) (velocidad * cos(theta)), (float) (velocidad * sin(theta)));
        } else {
            vector = new Vector2((float) (velocidad * cos(theta)), (float) (velocidad * sin(theta)));
            vector.add(porcentajeGiro * -1, porcentajeAceleracion);
        }
        theta = (float) atan2(vector.y, vector.x);
        velocidad = (float) sqrt(vector.x * vector.x + vector.y * vector.y);
        if(!body.getLinearVelocity().isZero(0.05f))Gdx.app.log("Nave Jugador:","LV: "+body.getLinearVelocity());
        body.setLinearVelocity(vector.scl(0.5f));

        sprite.setCenter(Constantes.toScreenSize(body.getPosition().x), Constantes.toScreenSize(body.getPosition().y));
    }


    public void girar(float porcentaje){
        this.porcentajeGiro = porcentaje*-1;
        this.estado = porcentaje == 0?EstadoMovimiento.PARADO:EstadoMovimiento.GIRANDO;
    }

    private void girar(){
        sprite.setRotation(sprite.getRotation()+RANGO_GIRO_MAX*porcentajeGiro);
        sprite.setRotation(min(sprite.getRotation(),90+RANGO_GIRO/2));
        sprite.setRotation(max(sprite.getRotation(),90-RANGO_GIRO/2));
    }

}
