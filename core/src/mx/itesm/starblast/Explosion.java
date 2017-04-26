package mx.itesm.starblast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Ian Neumann on 26/04/2017.
 */

class Explosion {
    private final float INITIAL_RADIUS = 10;
    private final float FINAL_RADIUS = 50;

    private Body body;
    private FixtureDef fixtureDef;

    public Explosion(Vector2 position, World world, float angle) {
        fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;

    }

    private void MakeFixture(float density, float restitution){
        while (body.getFixtureList().size > 0){
            body.destroyFixture(body.getFixtureList().first());
        }
        CircleShape bodyShape = new CircleShape();
    }
}
