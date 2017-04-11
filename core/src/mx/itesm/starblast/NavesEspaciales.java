package mx.itesm.starblast;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

abstract class NavesEspaciales implements IPlayableEntity {

    //TODO considerar hacer más exactos los coliders

    Body body;
    Sprite sprite;
    float porcentajeAceleracion;
    long COOLDOWN_DISPARO;
    long disparoAnterior = 0;
    final World world;
    short CATEGORY = -1;
    short MASK = -1;
    int vida;
    int damage;
    int BULLET_DAMAGE;
    boolean enemy;

    NavesEspaciales(Texture textura,float x,float y,World world,float angulo,float density, float restitution, boolean enemy) {
        this.world = world;
        this.enemy = enemy;
        vida = 100;
        damage = 10;
        sprite = new Sprite(textura);
        sprite.setRotation(angulo);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(Constant.toWorldSize(x), Constant.toWorldSize(y));
        bodyDef.angle = angulo;
        body = world.createBody(bodyDef);
        body.setUserData(this);
        BULLET_DAMAGE = 10;
        makeFixture(density, restitution);
    }

    public void disparar(long time) {
        if (disparoAnterior + COOLDOWN_DISPARO < time) {
            disparoAnterior = time;
            disparar();
        }
    }

    protected void disparar(){
        new Bullet(body.getPosition().x, body.getPosition().y,world, sprite.getRotation(), enemy,BULLET_DAMAGE);
    }

    public void acelerar(float porcentaje) {
        this.porcentajeAceleracion = porcentaje;
    }

    public abstract void mover(Vector2 punto,float delta);

    @Override
    public float getX() {
        return Constant.toScreenSize(body.getPosition().x);
    }

    @Override
    public float getY() {
        return Constant.toScreenSize(body.getPosition().y);
    }

    void escalar(float escala) {
        this.sprite.scale(escala);
        Fixture fix = body.getFixtureList().first();
        makeFixture(fix.getDensity(), fix.getRestitution());
    }

    private void makeFixture(float density, float restitution){
        while (body.getFixtureList().size > 0){
            body.destroyFixture(body.getFixtureList().first());
        }
        CircleShape bodyShape = new CircleShape();

        float w= Constant.toWorldSize(sprite.getWidth()*sprite.getScaleX()/2f);

        bodyShape.setRadius(w);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density=density;
        fixtureDef.restitution=restitution;
        fixtureDef.shape=bodyShape;
        fixtureDef.friction = 0;
        fixtureDef.filter.categoryBits = CATEGORY;
        fixtureDef.filter.maskBits = MASK;
        body.createFixture(fixtureDef);
        bodyShape.dispose();
    }

    @Override
    public void setDamage(int dmg){
        damage = dmg;
    }

    @Override
    public int getDamage(){
        return damage;
    }

    @Override
    public boolean doDamage(int damage) {
        vida -= damage;
        if(vida<=0){
            die();
            return true;
        }
        return false;
    }

    @Override
    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void die(){
        //TODO chance hace algo
    }

    @Override
    public Body getBody() {
        return body;
    }
}
