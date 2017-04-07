package mx.itesm.starblast;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;


class JefeEnemigo extends NaveEnemiga {

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

        limiteDerecho = Constants.toWorldSize(Constants.SCREEN_WIDTH *0.9f);
        limiteIzquierdo = Constants.toWorldSize(Constants.SCREEN_WIDTH *0.1f);
        limiteSuperior = Constants.toWorldSize(Constants.SCREEN_HEIGTH *0.9f);

        random = new Random();
    }

    @Override
    public void mover(Vector2 target,float delta){
        target = new Vector2(Constants.toWorldSize(target.x), Constants.toWorldSize(target.y));
        objetivo = new Vector2(target);
        target.y = limiteSuperior;
        target.x = MathUtils.clamp(target.x,limiteIzquierdo,limiteDerecho);
        float radianes = MathUtils.atan2(target.y-body.getPosition().y,target.x-body.getPosition().x);
        body.applyForceToCenter(new Vector2(MathUtils.cos(radianes),MathUtils.sin(radianes)),true);
        float hip = body.getLinearVelocity().len();
        if(hip > VELOCIDAD_MAXIMA){
            body.setLinearVelocity(body.getLinearVelocity().scl(VELOCIDAD_MAXIMA/hip));
        }
        sprite.setCenter(Constants.toScreenSize(body.getPosition().x), Constants.toScreenSize(body.getPosition().y));
        sprite.setRotation(MathUtils.radiansToDegrees*MathUtils.atan2(objetivo.y-body.getPosition().y,objetivo.x-body.getPosition().x));
    }

    @Override
    protected void disparar(){
        Vector2 gunPosition = new Vector2(body.getPosition().x,body.getPosition().y);

        float angulo = MathUtils.radiansToDegrees*MathUtils.atan2(objetivo.y-body.getPosition().y,objetivo.x-body.getPosition().x);
        switch (random.nextInt(10)){
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
    public void disparar(long time) {
        if (disparoAnterior + COOLDOWN_DISPARO < time) {
            disparoAnterior = time;
            disparar();
        }
    }
}
