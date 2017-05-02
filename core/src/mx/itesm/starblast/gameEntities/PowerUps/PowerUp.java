package mx.itesm.starblast.gameEntities.PowerUps;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

import mx.itesm.starblast.Constant;
import mx.itesm.starblast.gameEntities.PlayableEntity;

/**
 * Created by Ian Neumann on 01/05/2017.
 */

public abstract class PowerUp extends PlayableEntity implements IPowerUp {

    World world;
    Sprite sprite;
    Body body;

    PowerUp(Texture texture, float x, float y, World world, float angle, float density, float restitution) {
        this.world = world;

        sprite = new Sprite(texture);
        sprite.setRotation(angle);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(Constant.toWorldSize(x), Constant.toWorldSize(y));
        bodyDef.angle = angle;
        body = world.createBody(bodyDef);

        body.setLinearVelocity(getVelocityVector(x,y));

        body.setUserData(this);
        CircleShape bodyShape = new CircleShape();
        bodyShape.setRadius(Constant.toWorldSize(sprite.getWidth()/2));

        makeFixture(density, restitution,body,bodyShape,Constant.CATEGORY_POWER_UP,Constant.MASK_POWER_UP, true);
    }

    private Vector2 getVelocityVector(float x, float y) {
        float minAngle = MathUtils.atan2(-y,-x);
        float maxAngle = MathUtils.atan2(-y,Constant.SCREEN_WIDTH-x);
        float angle = new Random().nextFloat()*(maxAngle-minAngle)+minAngle;
        return new Vector2(MathUtils.cos(angle),MathUtils.sin(angle));
    }
}
