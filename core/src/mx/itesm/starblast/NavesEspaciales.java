package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.utils.Queue;

import java.util.ArrayList;

public class NavesEspaciales implements INaveEspacial {

    protected BodyDef bodyDef;
    protected Body body;
    protected CircleShape bodyShape;
    protected Sprite sprite;
    protected float porcentajeAceleracion;
    protected long COOLDOWN_DISPARO;
    protected long disparoAnterior = 0;
    protected World world;

    protected NavesEspaciales(World world) {
        this.world = world;
    }

    @Override
    public Bullet disparar(long time) {
        if (disparoAnterior + COOLDOWN_DISPARO < time) {
            disparoAnterior = time;
            return disparar();
        }
        return null;
    }

    protected Bullet disparar(){
        return new Bullet(body.getPosition(),world, sprite.getRotation());
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

    public Shape getShape() {
        return bodyShape;
    }

    public void escalar(float escala) {
        bodyShape.dispose();

        this.sprite.scale(escala);
        bodyShape = new CircleShape();
        this.bodyShape.setRadius(sprite.getWidth()*sprite.getScaleX()/2);
        makeFixture(0.1f,0.1f);
    }

    protected void makeFixture(float density,float restitution){
        makeFixture(density,restitution,(short) 0);
    }

    protected void makeFixture(float density,float restitution, short group){
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
        if(group!=0) {
            fixtureDef.filter.groupIndex = group;
        }
        body.createFixture(fixtureDef);
    }
}
