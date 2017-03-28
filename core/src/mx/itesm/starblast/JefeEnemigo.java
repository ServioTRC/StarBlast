package mx.itesm.starblast;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;


public class JefeEnemigo extends NaveEnemiga {

    private final float limiteIzquierdo;
    private final float limiteDerecho;
    private final float limiteSuperior;
    private final float VELOCIDAD_MAXIMA = 3;

    private Vector2 objetivo;
    private Random random;

    JefeEnemigo(Texture textura, float x, float y, World world, int vida) {
        super(textura, x, y, world, -90, 0.7f, 0.1f);

        damage = 20;
        this.vida = vida;
        COOLDOWN_DISPARO = 400;

        limiteDerecho = Constantes.toWorldSize(Constantes.ANCHO_PANTALLA*0.9f);
        limiteIzquierdo = Constantes.toWorldSize(Constantes.ANCHO_PANTALLA*0.1f);
        limiteSuperior = Constantes.toWorldSize(Constantes.ALTO_PANTALLA*0.9f);

        random = new Random();
    }

    @Override
    public void mover(Vector2 target,float delta){
        target = new Vector2(Constantes.toWorldSize(target.x),Constantes.toWorldSize(target.y));
        objetivo = new Vector2(target);
        target.y = limiteSuperior;
        target.x = MathUtils.clamp(target.x,limiteIzquierdo,limiteDerecho);
        float radianes = MathUtils.atan2(target.y-body.getPosition().y,target.x-body.getPosition().x);
        body.applyForceToCenter(new Vector2(MathUtils.cos(radianes),MathUtils.sin(radianes)),true);
        float hip = body.getLinearVelocity().len();
        if(hip > VELOCIDAD_MAXIMA){
            body.setLinearVelocity(body.getLinearVelocity().scl(VELOCIDAD_MAXIMA/hip));
        }
        sprite.setCenter(Constantes.toScreenSize(body.getPosition().x),Constantes.toScreenSize(body.getPosition().y));
        sprite.setRotation(MathUtils.radiansToDegrees*MathUtils.atan2(objetivo.y-body.getPosition().y,objetivo.x-body.getPosition().x));
    }

    @Override
    protected void disparar(boolean enemy){
        Vector2 gunPosition = new Vector2(body.getPosition().x+bodyShape.getRadius()*1.5f*MathUtils.cosDeg(sprite.getRotation()),
                body.getPosition().y+bodyShape.getRadius()*1.5f*MathUtils.sinDeg(sprite.getRotation()));

        float angulo = MathUtils.radiansToDegrees*MathUtils.atan2(objetivo.y-body.getPosition().y,objetivo.x-body.getPosition().x);
        switch (random.nextInt(5)){
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                new Bullet(gunPosition,world,angulo,enemy,5);
                break;
            case 6:
            case 7:
            case 8:
                new Bullet(gunPosition,world,angulo-20,enemy,5);
                new Bullet(gunPosition,world,angulo,enemy,5);
                new Bullet(gunPosition,world,angulo+20,enemy,5);
                break;
            case 9:
                new Bullet(gunPosition,world,angulo-50,enemy,5);
                new Bullet(gunPosition,world,angulo-25,enemy,5);
                new Bullet(gunPosition,world,angulo,enemy,5);
                new Bullet(gunPosition,world,angulo+25,enemy,5);
                new Bullet(gunPosition,world,angulo+50,enemy,5);
                break;

        }
    }

    @Override
    public void disparar(long time, boolean enemy) {
        if (disparoAnterior + COOLDOWN_DISPARO < time) {
            disparoAnterior = time;
            disparar(enemy);
        }
    }
}
