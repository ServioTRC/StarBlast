package mx.itesm.starblast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

interface INaveEspacial {
    Bullet disparar(long time, boolean enemy);
    void acelerar(float aceleracion);
    void mover(Vector2 destino, float delta);
    Body getBody();
}
