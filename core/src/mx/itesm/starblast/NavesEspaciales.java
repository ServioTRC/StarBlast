package mx.itesm.starblast;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Queue;

import java.util.ArrayList;


/**
 * Created by Ian Neumann on 16/02/2017.
 */

public abstract class NavesEspaciales implements INaveEspacial {





    public NavesEspaciales() {
    }

    @Override
    public abstract void disparar(long time);

    @Override
    public abstract void acelerar(float aceleracion);

    public abstract void draw(SpriteBatch batch);

    @Override
    public abstract void mover(Vector2 punto,float delta);

    @Override
    public abstract Body getBody();

    public abstract void escalar(float escala);

    public abstract float getX();

    public abstract float getY();

    public abstract Shape getShape();

}
