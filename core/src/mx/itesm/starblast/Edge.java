package mx.itesm.starblast;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

class Edge implements IPlayableEntity {

    Body body;

    Edge(World world, float x, float y, float hx, float hy){
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(Constant.toWorldSize(x), Constant.toWorldSize(y));
        body = world.createBody(bdef);
        body.setUserData(this);
        createEdgeFixture(hx,hy);
    }

    private void createEdgeFixture(float x, float y){
        while (body.getFixtureList().size > 0){
            body.destroyFixture(body.getFixtureList().first());
        }
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constant.toWorldSize(x), Constant.toWorldSize(y));
        FixtureDef fix = new FixtureDef();
        fix.shape = shape;
        fix.filter.categoryBits = Constant.CATEGORY_BORDERS;
        fix.filter.maskBits = Constant.MASK_BORDERS;
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

    @Override
    public Body getBody() {
        return body;
    }

    @Override
    public float getX() {
        return 0;
    }

    @Override
    public float getY() {
        return 0;
    }
}
