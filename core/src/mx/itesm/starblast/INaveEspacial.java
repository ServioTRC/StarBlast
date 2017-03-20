package mx.itesm.starblast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import java.awt.Point;

/**
 * Created by Ian Neumann on 16/02/2017.
 */
public interface INaveEspacial {
    public Bullet disparar(long time);
    public void acelerar(float aceleracion);
    public void mover(Vector2 destino, float delta);
    public Body getBody();
}
