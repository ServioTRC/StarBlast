package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.StringBuilder;
import java.util.ArrayList;
import java.util.Iterator;

class NavesEspaciales implements IPlayableEntity {

    BodyDef bodyDef;
    Body body;
    protected CircleShape bodyShape;
    Sprite sprite;
    float porcentajeAceleracion;
    long COOLDOWN_DISPARO;
    long disparoAnterior = 0;
    final World world;
    short CATEGORY = -1;
    short MASK = -1;
    int vida;
    int damage;
    float density;
    float restitution;
    boolean destruido;
    int BULLET_DAMAGE;

    NavesEspaciales(Texture textura,float x,float y,World world,float angulo,float density, float restitution) {
        this.world = world;
        vida = 100;
        damage = 10;
        sprite = new Sprite(textura);
        sprite.setRotation(angulo);
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(Constantes.toWorldSize(x),Constantes.toWorldSize(y));
        bodyDef.angle = angulo;
        destruido = false;
        body = world.createBody(bodyDef);
        body.setUserData(this);
        BULLET_DAMAGE = 10;
        this.restitution = restitution;
        this.density = density;
        makeFixture();
    }

    public void disparar(long time, boolean enemy) {
        if (disparoAnterior + COOLDOWN_DISPARO < time) {
            disparoAnterior = time;
            disparar(enemy);
        }
    }

    protected void disparar(boolean enemy){
        Vector2 gunPosition = new Vector2(body.getPosition().x+bodyShape.getRadius()*1.5f*MathUtils.cosDeg(sprite.getRotation()),
                                          body.getPosition().y+bodyShape.getRadius()*1.5f*MathUtils.sinDeg(sprite.getRotation()));
        new Bullet(gunPosition,world, sprite.getRotation(), enemy, BULLET_DAMAGE);
    }

    public void acelerar(float porcentaje) {
        this.porcentajeAceleracion = porcentaje;
    }

    public void mover(Vector2 punto,float delta){
        Gdx.app.log("NavesEspaciales","mover");
    }

    public Body getBody() {
        return body;
    }

    public float getX() {
        return Constantes.toScreenSize(body.getPosition().x);
    }

    public float getY() {
        return Constantes.toScreenSize(body.getPosition().y);
    }

    Shape getShape() {
        return bodyShape;
    }

    void escalar(float escala) {
        bodyShape.dispose();

        this.sprite.scale(escala);
        bodyShape = new CircleShape();
        this.bodyShape.setRadius(sprite.getWidth()*sprite.getScaleX()/2);
        makeFixture();
    }

    void makeFixture(){
        if(destruido){
            return;
        }
        while (body.getFixtureList().size > 0){
            body.destroyFixture(body.getFixtureList().first());
        }
        bodyShape = new CircleShape();

        float w=Constantes.toWorldSize(sprite.getWidth()*sprite.getScaleX()/2f);

        bodyShape.setRadius(w);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density=density;
        fixtureDef.restitution=restitution;
        fixtureDef.shape=bodyShape;
        fixtureDef.friction = 0;
        fixtureDef.filter.categoryBits = CATEGORY;
        fixtureDef.filter.maskBits = MASK;
        body.createFixture(fixtureDef);
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

    public void destroyBody(){
        destruido = true;
    }
}
