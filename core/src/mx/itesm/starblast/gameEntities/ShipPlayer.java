package mx.itesm.starblast.gameEntities;import com.badlogic.gdx.Gdx;import com.badlogic.gdx.Preferences;import com.badlogic.gdx.audio.Sound;import com.badlogic.gdx.graphics.Texture;import com.badlogic.gdx.graphics.g2d.Sprite;import com.badlogic.gdx.graphics.g2d.SpriteBatch;import com.badlogic.gdx.math.Vector2;import com.badlogic.gdx.physics.box2d.World;import com.badlogic.gdx.math.MathUtils;import com.badlogic.gdx.utils.Array;import mx.itesm.starblast.Constant;import mx.itesm.starblast.PreferencesSB;import mx.itesm.starblast.gameEntities.PowerUps.*;import static java.lang.Math.*;public class ShipPlayer extends Ship {    private enum movementState {        TURNING,        STOPPED    }    private final float TURN_RANGE = 80;    private final float BRAKE_CONSTANT = 0.97f;    private final float MAX_TURN_RANGE = 2;    private final int MISSILE_DAMAGE;    private final int COOLDOWN_MISSILE = 200;    private long previousMissile = 0;    private int missileCount = 5;    private movementState state = movementState.STOPPED;    private float turnPercentage;    private float shield;    private Sound missileSound;    public float MAX_HEALTH;    public final float MAX_SHIELD = 50;    private boolean infHealth;    private boolean infMissiles;    private float speedMultiplier = 1;    private int damageMultiplier;    private Texture missileTexture;    private Sprite shieldSprite;    private DroidHelper droidHelper;    private Array<PowerUp> powerUps;    public ShipPlayer(Texture texture, float x, float y, World world, SpriteBatch batch) {        super(texture, x, y, world, 90, 0.1f, 0.7f, false, batch);        CATEGORY = Constant.CATEGORY_PLAYER;        MASK = Constant.MASK_PLAYER;        COOLDOWN_SHOT = 100;        BULLET_DAMAGE = 10;        MISSILE_DAMAGE = 500;        damage = 20;        damageMultiplier = 1;        MAX_HEALTH = health;        shield = 0;        powerUps = new Array<PowerUp>();        fireSound = Constant.MANAGER.get("SoundEffects/ShootingSound1.mp3", Sound.class);        missileSound = Constant.MANAGER.get("SoundEffects/MissileSound.wav", Sound.class);        explosionSound = Constant.MANAGER.get("SoundEffects/Explosion1.mp3", Sound.class);        bulletTexture = Constant.MANAGER.get("GameScreen/BulletSprite.png", Texture.class);        missileTexture = Constant.MANAGER.get("GameScreen/MissileSprite.png", Texture.class);        shieldSprite = new Sprite(Constant.MANAGER.get("GameScreen/ShieldSprite.png", Texture.class));        Preferences pref = Gdx.app.getPreferences("Codes");        infHealth = pref.getBoolean("InfHealth", false);        infMissiles = pref.getBoolean("InfMissiles", false);        if (pref.getBoolean("speed", false)) {            speedMultiplier = 2;        }        if (pref.getBoolean("konami", false)) {            missileCount *= 2;            MAX_HEALTH *= 2;            health = MAX_HEALTH;        }        if (pref.getBoolean("nails", false)) {            damageMultiplier = 4;        }        int minigame = PreferencesSB.lastMinigameWon();        switch (minigame){            case 1:                droidHelper = new DroidHelper(Constant.MANAGER.get("GameScreen/DroidHelperSprite.png", Texture.class), getX() - 2 * sprite.getWidth(), getY(), world, 90, 0.1f, 0.7f, batch);                break;            case 2:                MAX_HEALTH *= 2;                health = MAX_HEALTH;                break;            case 3:                BULLET_DAMAGE *= 2;                bulletTexture = Constant.MANAGER.get("GameScreen/BulletSpritePowered.png", Texture.class);                break;            default:                break;        }    }    private void shootMissile() {        new Missile(body.getPosition().x, body.getPosition().y, world, sprite.getRotation(), enemy, MISSILE_DAMAGE, batch, missileTexture);    }    public void shootMissile(long time) {        if (previousMissile + COOLDOWN_MISSILE < time) {            previousMissile = time;            if (!infMissiles && missileCount <= 0) {                return;            }            missileCount--;            shootMissile();            if (PreferencesSB.SOUNDS_ENABLE) {                missileSound.play(0.5f);            }        }    }    @Override    public void playSound() {        fireSound.play(0.5f);    }    @Override    public void move(Vector2 nothing, float delta) {        if (state == movementState.TURNING) {            turn();        }        body.applyForceToCenter(turnPercentage * -1 * speedMultiplier, accelerationPercentage * speedMultiplier, true);        body.setLinearVelocity(body.getLinearVelocity().scl(BRAKE_CONSTANT));        sprite.setCenter(getX(), getY());        if(droidHelper != null && !droidHelper.isDead()) {            droidHelper.move(new Vector2(body.getPosition().x - 2, body.getPosition().y), delta);            droidHelper.setRotation(sprite.getRotation());        }        handlePowerups(delta);    }    private void handlePowerups(float delta) {        for(int i = powerUps.size-1; i>= 0;i--){            powerUps.get(i).addTime(delta);            if(powerUps.get(i).isDisabled()){                disablePowerUp(powerUps.get(i));                powerUps.removeIndex(i);            }        }    }    private void disablePowerUp(PowerUp powerUp) {        Gdx.app.log("PowerUp","Eliminar Powerup");        switch (powerUp.type()){            case health:                break;            case damage:                disableDamage(powerUp.getBonus());                break;            case speed:                disableSpeed(powerUp.getBonus());                break;            case shield:                break;            case missile:                break;        }    }    private void disableDamage(float bonus) {        BULLET_DAMAGE -= bonus;        BULLET_DAMAGE = max(10,BULLET_DAMAGE);        if(BULLET_DAMAGE == 10){            bulletTexture = Constant.MANAGER.get("GameScreen/BulletSprite.png");        }    }    private void disableSpeed(float bonus){        speedMultiplier -= bonus;        speedMultiplier = max(1,speedMultiplier);    }    public void turn(float percentage) {        this.turnPercentage = percentage * -1;        this.state = percentage == 0 ? movementState.STOPPED : movementState.TURNING;    }    private void turn() {        sprite.setRotation(sprite.getRotation() + MAX_TURN_RANGE * turnPercentage);        sprite.setRotation(min(sprite.getRotation(), 90 + TURN_RANGE / 2));        sprite.setRotation(max(sprite.getRotation(), 90 - TURN_RANGE / 2));    }    @Override    public boolean draw(SpriteBatch batch) {        if (shield > 0) {            shieldSprite.setCenter(getX(), getY());            shieldSprite.setAlpha(getShieldPercentage());            shieldSprite.draw(batch);        }        return super.draw(batch);    }    @Override    public int getDamage() {        return damageMultiplier * super.getDamage();    }    @Override    public boolean doDamage(int damage) {        if (infHealth) {            return false;        }        if (shield > 0) {            shield -= damage;            if (shield >= 0) {                return false;            }            damage = MathUtils.floor(-shield);            shield = 0;        }        return super.doDamage(damage);    }    public void recievePowerUp(PowerUp powerUp) {        switch (powerUp.type()) {            case health:                recieveHealth(powerUp.getBonus());                break;            case damage:                recieveBulletDamage(powerUp.getBonus());                powerUps.add(powerUp);                break;            case speed:                recieveSpeedBoost(powerUp.getBonus());                powerUps.add(powerUp);                break;            case shield:                recieveShield(powerUp.getBonus());                break;            case missile:                recieveMissile(powerUp.getBonus());                break;        }    }    private void recieveMissile(float bonus) {        missileCount += bonus;    }    private void recieveShield(float bonus) {        shield += bonus;        shield = min(shield, MAX_SHIELD);    }    private void recieveSpeedBoost(float bonus) {        speedMultiplier += bonus;    }    private void recieveHealth(float bonus) {        health += bonus;        health = min(health, MAX_HEALTH);    }    private void recieveBulletDamage(float bonus) {        bulletTexture = Constant.MANAGER.get("GameScreen/BulletSpritePowered.png", Texture.class);        BULLET_DAMAGE += bonus;    }    public float getHealthPercentage() {        return health / MAX_HEALTH;    }    public float getShieldPercentage() {        return shield / MAX_SHIELD;    }    @Override    public void scale(float scale) {        if (!(droidHelper == null) && !droidHelper.isDead()) {            droidHelper.scale(scale);        }        super.scale(scale);    }    @Override    protected void shoot() {        if (!(droidHelper == null) && !droidHelper.isDead()) {            droidHelper.shoot();        }        super.shoot();    }    public int getMissileCount() {        return missileCount;    }}