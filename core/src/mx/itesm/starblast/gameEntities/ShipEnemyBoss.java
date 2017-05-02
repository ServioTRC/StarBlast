package mx.itesm.starblast.gameEntities;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

import mx.itesm.starblast.Constant;


public class ShipEnemyBoss extends ShipEnemy {

    private final float leftLimit;
    private final float rightLimit;
    private final float upperLimit;
    private final float TOP_SPEED = 3;

    private Vector2 objective;
    private Random random;

    public ShipEnemyBoss(Texture texture, float x, float y, World world, int life, SpriteBatch batch) {
        super(texture, x, y, world, -90, 50f, 0.1f,batch);

        damage = 20;
        this.health = life;
        COOLDOWN_SHOT = 400;

        rightLimit = Constant.toWorldSize(Constant.SCREEN_WIDTH *0.7f);
        leftLimit = Constant.toWorldSize(Constant.SCREEN_WIDTH *0.1f);
        upperLimit = Constant.toWorldSize(Constant.SCREEN_HEIGTH *0.7f);

        random = new Random();

        fireSound = Constant.MANAGER.get("SoundEffects/ShootingSound1.mp3", Sound.class);
        explosionSound = Constant.MANAGER.get("SoundEffects/Explosion2.mp3",Sound.class);

    }

    void makeFixture(float density, float restitution){

        PolygonShape bodyShape = new PolygonShape();

        float w= Constant.toWorldSize(sprite.getWidth()*sprite.getScaleX()/2f);
        float h= Constant.toWorldSize(sprite.getHeight()*sprite.getScaleY()/2f);

        bodyShape.setAsBox(w,h);

        super.makeFixture(density,restitution,body,bodyShape,CATEGORY,MASK,false);
    }

    @Override
    public void move(Vector2 target, float delta){
        target = new Vector2(Constant.toWorldSize(target.x), Constant.toWorldSize(target.y));
        objective = new Vector2(target);
        target.y = upperLimit;
        target.x = MathUtils.clamp(target.x, leftLimit, rightLimit);
        float radians = MathUtils.atan2(target.y-body.getPosition().y,target.x-body.getPosition().x);
        body.applyForceToCenter(new Vector2(MathUtils.cos(radians),MathUtils.sin(radians)),true);
        float hip = body.getLinearVelocity().len();
        if(hip > TOP_SPEED){
            body.setLinearVelocity(body.getLinearVelocity().scl(TOP_SPEED /hip));
        }
        sprite.setCenter(Constant.toScreenSize(body.getPosition().x), Constant.toScreenSize(body.getPosition().y));
        sprite.setRotation(MathUtils.radiansToDegrees*MathUtils.atan2(objective.y-body.getPosition().y, objective.x-body.getPosition().x));

        body.setTransform(body.getPosition(), MathUtils.degreesToRadians*sprite.getRotation());
    }

    @Override
    protected void shoot(){
        Vector2 gunPosition = new Vector2(body.getPosition().x,body.getPosition().y);

        float angle = MathUtils.radiansToDegrees*MathUtils.atan2(objective.y-body.getPosition().y, objective.x-body.getPosition().x);
        switch (random.nextInt(10)){
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                new Bullet(gunPosition,world,angle,enemy,5,bulletTexture);
                break;
            case 6:
            case 7:
            case 8:
                new Bullet(gunPosition,world,angle-20,enemy,5,bulletTexture);
                new Bullet(gunPosition,world,angle,enemy,5,bulletTexture);
                new Bullet(gunPosition,world,angle+20,enemy,5,bulletTexture);
                break;
            case 9:
                new Bullet(gunPosition,world,angle-50,enemy,5,bulletTexture);
                new Bullet(gunPosition,world,angle-25,enemy,5,bulletTexture);
                new Bullet(gunPosition,world,angle,enemy,5,bulletTexture);
                new Bullet(gunPosition,world,angle+25,enemy,5,bulletTexture);
                new Bullet(gunPosition,world,angle+50,enemy,5,bulletTexture);
                break;

        }
    }

    @Override
    public void shoot(long time) {
        if (previousShot + COOLDOWN_SHOT < time) {
            previousShot = time;
            shoot();
        }
    }
}
