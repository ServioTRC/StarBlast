package mx.itesm.starblast;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

class Borde implements IPlayableEntity {

    Body body;

    Borde(World world, float x, float y, float hx, float hy){
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(Constantes.toWorldSize(x),Constantes.toWorldSize(y));
        body = world.createBody(bdef);
        body.setUserData(this);
        crearFixture(hx,hy);
    }

    private void crearFixture(float x, float y){
        for (Fixture fix : body.getFixtureList()) {
            body.destroyFixture(fix);
        }
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constantes.toWorldSize(x),Constantes.toWorldSize(y));
        FixtureDef fix = new FixtureDef();
        fix.shape = shape;
        fix.filter.categoryBits = Constantes.CATEGORY_BORDERS;
        fix.filter.maskBits = Constantes.MASK_BORDERS;
        body.createFixture(fix);
    }

    @Override
    public void setDamage(int dmg){

    }

    @Override
    public int getDamage() {
        return 0;
    }

    @Override
    public boolean doDamage(int damage) {
        return false;
    }

    @Override
    public void draw(SpriteBatch batch) {

    }
}
