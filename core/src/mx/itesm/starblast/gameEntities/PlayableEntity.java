package mx.itesm.starblast.gameEntities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;

import mx.itesm.starblast.Constant;

/**
 * Created by Ian Neumann on 01/05/2017.
 */

public abstract class PlayableEntity implements IPlayableEntity {


    public static void makeFixture(float density, float restitution, Body body, Shape bodyShape, short CATEGORY, short MASK, boolean isSensor){
        while (body.getFixtureList().size > 0){
            body.destroyFixture(body.getFixtureList().first());
        }

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density=density;
        fixtureDef.restitution=restitution;
        fixtureDef.shape=bodyShape;
        fixtureDef.friction = 0;
        fixtureDef.filter.categoryBits = CATEGORY;
        fixtureDef.filter.maskBits = MASK;
        fixtureDef.isSensor = isSensor;

        body.createFixture(fixtureDef);
        bodyShape.dispose();
    }
}
