package mx.itesm.starblast.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import mx.itesm.starblast.gameEntities.Bullet;
import mx.itesm.starblast.Constant;
import mx.itesm.starblast.gameEntities.Edge;
import mx.itesm.starblast.gameEntities.Explosion;
import mx.itesm.starblast.gameEntities.PowerUps.DamagePowerUp;
import mx.itesm.starblast.gameEntities.PowerUps.HealthPowerUp;
import mx.itesm.starblast.gameEntities.IExplotable;
import mx.itesm.starblast.gameEntities.IPausable;
import mx.itesm.starblast.gameEntities.IPlayableEntity;
import mx.itesm.starblast.PreferencesSB;
import mx.itesm.starblast.gameEntities.PowerUps.MissilePowerUp;
import mx.itesm.starblast.gameEntities.PowerUps.PowerUp;
import mx.itesm.starblast.gameEntities.ProgressBar;
import mx.itesm.starblast.gameEntities.PowerUps.ShieldPowerUp;
import mx.itesm.starblast.gameEntities.Ship;
import mx.itesm.starblast.gameEntities.ShipEnemy;
import mx.itesm.starblast.gameEntities.ShipEnemyBoss;
import mx.itesm.starblast.gameEntities.ShipPlayer;
import mx.itesm.starblast.gameEntities.PowerUps.SpeedPowerUp;
import mx.itesm.starblast.stages.StageLost;
import mx.itesm.starblast.stages.StagePause;
import mx.itesm.starblast.stages.StageWin;
import mx.itesm.starblast.StarBlast;

class LevelStory extends mx.itesm.starblast.screens.ScreenSB implements IPausable {

    //region background
    Texture[] plainBackgrounds = {
            Constant.MANAGER.get("GameScreen/FondoTile2.jpg", Texture.class),
            Constant.MANAGER.get("GameScreen/FondoTile3.jpg", Texture.class),
            Constant.MANAGER.get("GameScreen/FondoTile4.jpg", Texture.class)
    };
    Texture[] meteorBackground = {
            Constant.MANAGER.get("GameScreen/FondoTile6.jpg", Texture.class),
            Constant.MANAGER.get("GameScreen/FondoTile7.jpg", Texture.class),
            Constant.MANAGER.get("GameScreen/FondoTile8.jpg", Texture.class)
    };
    Texture[] transitionBackgrounds = {
            Constant.MANAGER.get("GameScreen/FondoTile5.jpg", Texture.class),
            Constant.MANAGER.get("GameScreen/FondoTileGrande.jpg", Texture.class),
            Constant.MANAGER.get("GameScreen/FondoTileGrande2.jpg", Texture.class),
            Constant.MANAGER.get("GameScreen/FondoTileGrande3.jpg", Texture.class)
    };
    Texture beginBackground = Constant.MANAGER.get("GameScreen/FondoBegin.jpg", Texture.class);
    //TODO usarla
    Texture endTexture = Constant.MANAGER.get("GameScreen/FondoEnd.jpg", Texture.class);
    Texture firstBackground;
    Texture secondBackground;
    private float posY = 0;
    private float backgroundSpeed = 70;
    //endregion

    //region estados del juego
    boolean isPaused;
    boolean switchingWaves;
    boolean youLost;
    boolean youWon;
    //endregion

    //region score
    int score;
    //endregion

    //region hud
    private final Stage HUDScene;
    private ProgressBar lifeBar;
    private Button pauseButton;
    //endregion

    //region escenas
    final StagePause pauseScene;
    private StageLost lostScene;
    private StageWin winningScene;
    //endregion

    // region world
    private final World world = new World(Vector2.Zero, true);
    private final HashSet<Body> toRemove = new HashSet<Body>();
    private float accumulator = 0;
    //hacer más grande este numero mejora el rendimiento pero hace más rapido el juego
    private final float timeBetweenFrames = 1 / 120f;
    //endregion

    //region enemies
    final int initialEnemies;
    int extraPerWave;
    final int numberOfWaves;
    float spawnTimeuot; //3 para juego normal
    float timeSinceLastSpawn = 0;
    float numberEnemiesForThisWave;
    float spawnedEnemiesForThisWave = 0;
    int waveNumber = 1;
    float timeoutBetweenWaves = 5;
    int level;
    boolean hasBoss = true;

    ArrayList<ShipEnemy> enemies = new ArrayList<ShipEnemy>();
    ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();


    ArrayList<Texture> EnemiesFiles;
    Texture enemyBossTexture;
    //endregion

    //region player
    private boolean playerShooting;
    private ShipPlayer player;
    //endregion

    //region animaciones
    private final LinkedList<Explosion> animations = new LinkedList<Explosion>();
    //endregion

    //region other
    final SpriteBatch batch;
    private Random random = new Random();
    private boolean playerSpecial = false;
//    private Box2DDebugRenderer debugRenderer;
//    private Matrix4 debugMatrix;
    //endregion

    LevelStory(StarBlast app, int initialEnemies, int extraPerWave, int numberOfWaves, int spawnTimeuot, int level) {
        super();
        batch = new SpriteBatch();
        HUDScene = new Stage(view, batch) {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    pauseIP();
                    pauseScene.draw();
                    return true;
                }
                return false;
            }
        };
        pauseScene = new StagePause(view, batch, app, this);
        lostScene = new StageLost(view, batch, app);
        winningScene = new StageWin(view, batch, app);
        this.initialEnemies = initialEnemies;
        this.extraPerWave = extraPerWave;
        this.numberOfWaves = numberOfWaves;
        this.spawnTimeuot = spawnTimeuot;
        numberEnemiesForThisWave = initialEnemies;
        this.level = level;

        EnemiesFiles = new ArrayList<Texture>();
        EnemiesFiles.add(Constant.MANAGER.get("GameScreen/Enemy1Sprite.png", Texture.class));
        EnemiesFiles.add(Constant.MANAGER.get("GameScreen/Enemy2Sprite.png", Texture.class));
        EnemiesFiles.add(Constant.MANAGER.get("GameScreen/Enemy3Sprite.png", Texture.class));

        firstBackground = beginBackground;
        secondBackground = plainBackgrounds[random.nextInt(plainBackgrounds.length)];
    }

    //region metodos ScreenSB

    @Override
    public void show() {
        createHUD();
        Gdx.input.setInputProcessor(HUDScene);
        createWorld();
        createEdges();

        player = new ShipPlayer(Constant.MANAGER.get("GameScreen/AvatarSprite.png", Texture.class), Constant.SCREEN_WIDTH / 2, Constant.SCREEN_WIDTH / 5, world, batch);
        player.scaling(Constant.SHIPS_SCALE);
//        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void render(float delta) {

//        debugMatrix = new Matrix4(camera.combined);
//        debugMatrix.scale(100,100,1);

        batch.begin();
        handleFondo(delta);
        handleGame(delta);
        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);
        Object obj;

        if (powerUps.size() > 0) {
            powerUps.get(0);
        }

        for (Body b : bodies) {
            obj = b.getUserData();
            if (obj instanceof IPlayableEntity) {
                ((IPlayableEntity) obj).draw(batch);
            }
        }
        Iterator<Explosion> it = animations.iterator();
        Explosion explosion;
        while (it.hasNext()) {
            explosion = it.next();
            if (!explosion.isCreated()) {
                explosion.createExplosion();
            }
            if (explosion.draw(batch)) {
                it.remove();
                toRemove.add(explosion.getBody());
            }
        }
        batch.end();

        HUDScene.draw();
        handleStates(delta);

//        debugRenderer.render(world,debugMatrix);
    }

    void handleStates(float delta) {
        if (youLost) {
            //pauseIP();
            Gdx.input.setInputProcessor(lostScene);
            lostScene.act(delta);
            lostScene.draw();
            //return;
        } else if (youWon) {
            PreferencesSB.saveLevelProgress(level + 1);
            Gdx.input.setInputProcessor(winningScene);
            winningScene.draw();
            //return;
        } else if (isPaused) {
            pauseScene.draw();
        }
    }

    @Override //pantalla
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    //endregion

    //region metodos IPausable

    @Override //IPausable
    public void pauseIP() {
        isPaused = true;
        pauseScene.addActor(pauseButton);
        Gdx.input.setInputProcessor(pauseScene);
    }

    @Override
    public void unPauseIP() {
        isPaused = false;
        HUDScene.addActor(pauseButton);
        Gdx.input.setInputProcessor(HUDScene);
    }

    //endregion

    //region metodos de render
    private void handleFondo(float dt) {
        batch.draw(firstBackground, 0, posY);
        batch.draw(secondBackground, 0, posY + firstBackground.getHeight());
        if (!isPaused) {
            posY -= backgroundSpeed * dt;
            if (posY <= -firstBackground.getHeight()) {
                posY = 0;
                firstBackground = secondBackground;
                secondBackground = getBackground();
            }
        }
    }

    private Texture getBackground() {
        double r = random.nextDouble();
        if (r <= 0.5) {
            return plainBackgrounds[random.nextInt(plainBackgrounds.length)];
        }
        if (r <= 0.8) {
            return meteorBackground[random.nextInt(meteorBackground.length)];
        }
        return transitionBackgrounds[random.nextInt(transitionBackgrounds.length)];
    }

    private void handleGame(float dt) {
        if (!isPaused) {
            handleWaves();
            spawnEnemies(dt);
            spawnPowerUps(dt);
            updateWorld(dt);
            movePlayer(dt);
            moveEnemies(dt);
        }
    }

    //endregion

    //region metodos hud

    private void createHUD() {
        createPad();
        createLifeBar();
        createPauseButton();
        createShotButton();
        createSpecialButton();
    }


    private void createPad() {
        Skin skin = new Skin();
        skin.add("PadBack", Constant.MANAGER.get("HUD/JoystickPad.png", Texture.class));
        skin.add("PadKnob", Constant.MANAGER.get("HUD/JoystickStick.png", Texture.class));

        Touchpad.TouchpadStyle style = new Touchpad.TouchpadStyle();
        style.background = skin.getDrawable("PadBack");
        style.knob = skin.getDrawable("PadKnob");

        Touchpad touchpad = new Touchpad(20, style);
        touchpad.setBounds(25, 50, 200, 200);

        touchpad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Touchpad pad = (Touchpad) actor;

                if (Math.abs(pad.getKnobPercentX()) > Constant.TOUCHPAD_DEADZONE) {
                    player.turn(pad.getKnobPercentX());
                } else {
                    player.turn(0);
                }
                if (Math.abs(pad.getKnobPercentY()) > Constant.TOUCHPAD_DEADZONE) {
                    player.accelerate(pad.getKnobPercentY());
                } else {
                    player.accelerate(0);
                }
            }
        });
        HUDScene.addActor(touchpad);
    }

    private void createLifeBar() {
        lifeBar = new ProgressBar(Constant.MANAGER.get("HUD/LifeBarBar.png", Texture.class), true, true);
        lifeBar.setFrame(Constant.MANAGER.get("HUD/LifeBarFrame.png", Texture.class));
        lifeBar.setPosition(9 * Constant.SCREEN_WIDTH / 10 + 40, 2 * Constant.SCREEN_HEIGTH / 8);
        HUDScene.addActor(lifeBar);
    }

    private void createPauseButton() {
        Skin skin = new Skin();
        skin.add("Pause", Constant.MANAGER.get("GameScreen/Pause.png", Texture.class));

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("Pause");

        pauseButton = new Button(style);
        pauseButton.setPosition(11 * Constant.SCREEN_WIDTH / 12,
                9 * Constant.SCREEN_HEIGTH / 10);
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                PreferencesSB.clickedSound();
                Button boton = (Button) actor;
                if (boton.isPressed()) {
                    if (!isPaused) {
                        pauseIP();
                    } else {
                        unPauseIP();
                    }
                }
            }
        });

        HUDScene.addActor(pauseButton);
    }

    private void createShotButton() {
        Skin skin = new Skin();
        skin.add("StandbyShot", Constant.MANAGER.get("HUD/ButtonAStandby.png", Texture.class));
        skin.add("PressedShot", Constant.MANAGER.get("HUD/ButtonAPress.png", Texture.class));
        Button.ButtonStyle style = new Button.ButtonStyle();
        style.down = skin.getDrawable("PressedShot");
        style.up = skin.getDrawable("StandbyShot");

        Button shotButton = new Button(style);
        shotButton.setPosition(13 * Constant.SCREEN_WIDTH / 16,
                1 * Constant.SCREEN_HEIGTH / 10);

        shotButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                playerShooting = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                playerShooting = false;
            }
        });
        HUDScene.addActor(shotButton);
    }

    private void createSpecialButton() {
        Skin skin = new Skin();
        skin.add("StandbySpecial", Constant.MANAGER.get("HUD/ButtonBStandby.png", Texture.class));
        skin.add("PressedSpecial", Constant.MANAGER.get("HUD/ButtonBPress.png", Texture.class));
        Button.ButtonStyle estilo = new Button.ButtonStyle();
        estilo.down = skin.getDrawable("PressedSpecial");
        estilo.up = skin.getDrawable("StandbySpecial");

        Button specialButton = new Button(estilo);
        specialButton.setPosition(12 * Constant.SCREEN_WIDTH / 16,
                1 * Constant.SCREEN_HEIGTH / 10);

        specialButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                playerSpecial = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                playerSpecial = false;
            }
        });
        HUDScene.addActor(specialButton);
    }

    //endregion

    //region metodos world
    private void createWorld() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                if (!(contact.getFixtureA().getBody().getUserData() instanceof IPlayableEntity) ||
                        !(contact.getFixtureB().getBody().getUserData() instanceof IPlayableEntity)) {
                    return;
                }
                IPlayableEntity objectA = (IPlayableEntity) contact.getFixtureA().getBody().getUserData();
                IPlayableEntity objectB = (IPlayableEntity) contact.getFixtureB().getBody().getUserData();
                boolean destroyA = handleCollision(objectA, objectB);
                boolean destroyB = handleCollision(objectB, objectA);
                if (destroyA) {
                    objectA.setDamage(0);
                }
                if (destroyB) {
                    objectB.setDamage(0);
                }
                lifeBar.setPorcentage(player.getHealthPercentage());
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });
    }

    private boolean handleCollision(IPlayableEntity a, IPlayableEntity b) {
        boolean isDestroyed = false;

        if (a instanceof PowerUp) {
            if (b instanceof ShipPlayer) {
                ((ShipPlayer) b).recievePowerUp(((PowerUp) a));
                toRemove.add(a.getBody());
            }
            if (b instanceof Edge) {
                toRemove.add(a.getBody());
            }
            return false;
        }

        if (a instanceof Bullet) {
            if (b instanceof Edge) {
                toRemove.add(a.getBody());
                return true;
            } else if (b instanceof Ship) {
                isDestroyed = a.doDamage(Integer.MAX_VALUE);
            }
        }
        if (a.doDamage(b.getDamage()) || isDestroyed) {
            toRemove.add(a.getBody());
            if (a instanceof IExplotable) {
                animations.add(((IExplotable) a).getExplosion());
            }
            if (a instanceof ShipEnemy) {
                enemies.remove(a);
                score += 100;
            }
            if (a instanceof ShipPlayer) {
                isPaused = true;
                youLost = true;
            }
            return true;
        }
        return false;
    }

    private void updateWorld(float dt) {
        accumulator += dt;
        while (accumulator >= timeBetweenFrames) {
            world.step(timeBetweenFrames, 8, 3);
            accumulator -= timeBetweenFrames;
        }
        for (Body b : toRemove) {
            while (b.getFixtureList().size > 0) {
                b.destroyFixture(b.getFixtureList().first());
            }
            b.setUserData(null);
            world.destroyBody(b);
        }
        toRemove.clear();
    }

    private void createEdges() {
        new Edge(world, -100, 0, 100, Constant.SCREEN_HEIGTH);
        new Edge(world, Constant.SCREEN_WIDTH + 100, 0, 100, Constant.SCREEN_HEIGTH);
        new Edge(world, -100, -100, Constant.SCREEN_WIDTH + 200, 100);
        new Edge(world, -100, Constant.SCREEN_HEIGTH + 100, Constant.SCREEN_WIDTH + 200, 100);
    }
    //endregion

    //region metodos player
    private void movePlayer(float delta) {
        player.move(null, delta);
        if (playerShooting) {
            player.shoot(TimeUtils.millis());
        }
        if (playerSpecial) {
            player.shootMissile(TimeUtils.millis());
        }
    }
    //endregion

    //region metodos enemies
    private void spawnEnemies(float dt) {
        timeSinceLastSpawn += dt;
        if (switchingWaves) {
            if (timeSinceLastSpawn < timeoutBetweenWaves) {
                return;
            }
            timeSinceLastSpawn = spawnTimeuot;
            switchingWaves = false;
        }
        if (spawnedEnemiesForThisWave < numberEnemiesForThisWave && timeSinceLastSpawn >= spawnTimeuot) {
            timeSinceLastSpawn = 0;
            spawnedEnemiesForThisWave++;
            ShipEnemy enemy = new ShipEnemy(EnemiesFiles.get(random.nextInt(3)), random.nextInt((int) Constant.SCREEN_WIDTH), Constant.SCREEN_HEIGTH + 50, world, batch);
            enemy.scale(Constant.SHIPS_SCALE);
            enemies.add(enemy);
        }
    }

    private void spawnPowerUps(float dt) {
        if (random.nextFloat() < 0.1f / 50f) {
            spawnRandomPowerUp();
        }
    }

    private void spawnRandomPowerUp() {
        float r = random.nextFloat();
        if (0 <= r && r < 0.30) {
            Gdx.app.log("PowerUp", "Spawned Health");
            new HealthPowerUp(Constant.MANAGER.get("GameScreen/PowerupHealthSprite.png", Texture.class), random.nextFloat() * (Constant.SCREEN_WIDTH - 200) + 100, Constant.SCREEN_HEIGTH - 100, world);
        } else if (r < 0.50) {
            Gdx.app.log("PowerUp", "Spawned Shield");
            new ShieldPowerUp(Constant.MANAGER.get("GameScreen/PowerupShieldSprite.png", Texture.class), random.nextFloat() * (Constant.SCREEN_WIDTH - 200) + 100, Constant.SCREEN_HEIGTH - 100, world);
        } else if (r < 0.70) {
            Gdx.app.log("PowerUp", "Spawned Speed");
            new SpeedPowerUp(Constant.MANAGER.get("GameScreen/PowerupSpeedSprite.png", Texture.class), random.nextFloat() * (Constant.SCREEN_WIDTH - 200) + 100, Constant.SCREEN_HEIGTH - 100, world);
        } else if (r < 0.90) {
            Gdx.app.log("PowerUp", "Spawned Damage");
            new DamagePowerUp(Constant.MANAGER.get("GameScreen/PowerupDamageSprite.png", Texture.class), random.nextFloat() * (Constant.SCREEN_WIDTH - 200) + 100, Constant.SCREEN_HEIGTH - 100, world);
        } else {
            Gdx.app.log("PowerUp", "Spawned Missile");
            new MissilePowerUp(Constant.MANAGER.get("GameScreen/PowerupMissileSprite.png", Texture.class), random.nextFloat() * (Constant.SCREEN_WIDTH - 200) + 100, Constant.SCREEN_HEIGTH - 100, world);
        }
    }

    private void moveEnemies(float delta) {
        Vector2 target = new Vector2(player.getX(), player.getY());
        for (ShipEnemy enemy : enemies) {
            enemy.move(target, delta);
            enemy.shoot(TimeUtils.millis());
        }
    }

    void handleWaves() {
        if (spawnedEnemiesForThisWave == numberEnemiesForThisWave && enemies.size() == 0) {
            if (waveNumber < numberOfWaves) {
                waveNumber++;
                timeSinceLastSpawn = 0;
                spawnedEnemiesForThisWave = 0;
                numberEnemiesForThisWave += extraPerWave;
                switchingWaves = true;
            } else {
                if (hasBoss) {
                    hasBoss = false;
                    ShipEnemy boss = new ShipEnemyBoss(enemyBossTexture, Constant.SCREEN_WIDTH / 2, Constant.SCREEN_HEIGTH + 100, world, (int) (3000 + 1000 * (level * (level + 1)) / 2), batch);
                    boss.scaling(Constant.SHIPS_SCALE);
                    enemies.add(boss);
                } else {
                    isPaused = true;
                    youWon = true;
                }
            }
        }
    }
    //endregion


}