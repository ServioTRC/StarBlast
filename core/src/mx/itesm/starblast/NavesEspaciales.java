package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.StringBuilder;

class NavesEspaciales implements INaveEspacial {

    BodyDef bodyDef;
    Body body;
    private CircleShape bodyShape;
    Sprite sprite;
    float porcentajeAceleracion;
    long COOLDOWN_DISPARO;
    private long disparoAnterior = 0;
    private final World world;
    short CATEGORY = -1;
    short MASK = -1;
    int vida;
    int damage;

    NavesEspaciales(String ubicacion,float x,float y,World world,float angulo,float density, float restitution) {
        this.world = world;
        vida = 100;
        damage = 10;
        sprite = new Sprite(new Texture(ubicacion));
        sprite.setRotation(angulo);
        this.bodyDef = new BodyDef();
        this.bodyDef.type = BodyDef.BodyType.DynamicBody;
        this.bodyDef.position.set(Constantes.toWorldSize(x),Constantes.toWorldSize(y));
        this.bodyDef.angle = angulo;

        body = world.createBody(bodyDef);
        body.setUserData(this);

        makeFixture(density,restitution);
    }

    @Override
    public Bullet disparar(long time, boolean enemy) {
        if (disparoAnterior + COOLDOWN_DISPARO < time) {
            disparoAnterior = time;
            return disparar(enemy);
        }
        return null;
    }

    private Bullet disparar(boolean enemy){
        return new Bullet(body.getPosition(),world, sprite.getRotation(), enemy,20);
    }

    @Override
    public void acelerar(float porcentaje) {
        this.porcentajeAceleracion = porcentaje;
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    @Override
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

    public void doDamage(int damage){
        vida-=damage;
    }

    Shape getShape() {
        return bodyShape;
    }

    void escalar(float escala) {
        bodyShape.dispose();

        this.sprite.scale(escala);
        bodyShape = new CircleShape();
        this.bodyShape.setRadius(sprite.getWidth()*sprite.getScaleX()/2);
        makeFixture(0.1f,0.1f);
    }

    void makeFixture(float density, float restitution){
        for(Fixture fix: body.getFixtureList()){
            body.destroyFixture(fix);
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
}
