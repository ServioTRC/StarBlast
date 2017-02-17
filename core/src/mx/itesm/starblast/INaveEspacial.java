package mx.itesm.starblast;

import com.badlogic.gdx.math.Vector2;

import java.awt.Point;

/**
 * Created by Ian Neumann on 16/02/2017.
 */
public interface INaveEspacial {
    public void disparar(float time);
    public void acelerar(float aceleracion);
    public void mover(Vector2 destino, float delta);
}
