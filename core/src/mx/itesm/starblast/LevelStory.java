package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
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

class LevelStory extends ScreenSB implements IPausable {

    //region background
    Texture loopingBackground;
    private float posY = 0;
    private float backgroundSpeed = 50;
    //endregion

    //region estados del juego
    private boolean isPaused;
    private boolean switchingWaves;
    private boolean youLost;
    private boolean youWon;
    //endregion

    //region score
    private int score;
    private Text textScore = new Text(Constant.SOURCE_TEXT);
    //endregion

    //region hud
    private final Stage HUDScene;
    private ProgressBar lifeBar;
    private Button pauseButton;
    //endregion

    //region escenas
    private final StagePause pauseScene;
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
    final  int extraPerWave;
    final int numberOfWaves;
    final float spawnTimeuot = 0.1f;
    float timeSinceLastSpawn = 0;
    float numberEnemiesForThisWave;
    float spawnedEnemiesForThisWave = 0;
    int waveNumber = 1;
    float timeoutBetweenWaves = 5;
    int level;

    ArrayList<ShipEnemy> enemies = new ArrayList<ShipEnemy>();
    //endregion

    //region player
    private boolean playerShooting;
    private ShipPlayer player;
    //endregion

    //region animaciones
    private final LinkedList<AutoAnimation> animations = new LinkedList<AutoAnimation>();
    //endregion

    //region other
    private final StarBlast app;
    private final SpriteBatch batch = new SpriteBatch();
    Random random = new Random();
    //endregion

    LevelStory(StarBlast app, int initialEnemies, int extraPerWave, int numberOfWaves, int level) {
        super();
        this.app = app;
        HUDScene = new Stage(view, batch);
        pauseScene = new StagePause(view, batch, app, this);
        lostScene = new StageLost(view, batch, app);
        winningScene = new StageWin(view, batch, app);
        this.initialEnemies = initialEnemies;
        this.extraPerWave = extraPerWave;
        this.numberOfWaves = numberOfWaves;
        numberEnemiesForThisWave = initialEnemies;
        this.level = level;
    }

    //region metodos ScreenSB

    @Override
    public void show() {
        createHUD();
        Gdx.input.setInputProcessor(HUDScene);
        createWorld();
        createEdges();

        player = new ShipPlayer(Constant.MANAGER.get("GameScreen/AvatarSprite.png", Texture.class), Constant.SCREEN_WIDTH / 2, Constant.SCREEN_WIDTH / 5, world);
        player.scaling(Constant.SHIPS_SCALE);
    }

    @Override
    public void render(float delta) {
        batch.begin();
        handleFondo(delta);
        handleGame(delta);
        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);
        Object obj;
        for (Body b : bodies) {
            obj = b.getUserData();
            if (obj instanceof IPlayableEntity) {
                ((IPlayableEntity) obj).draw(batch);
            }
        }
        Iterator<AutoAnimation> it = animations.iterator();
        AutoAnimation anim;
        while (it.hasNext()) {
            anim = it.next();
            if (anim.draw(batch, Gdx.graphics.getDeltaTime())) {
                it.remove();
            }
        }
//        textScore.showMessage(batch, "Puntaje: " + score, 20, Constant.SCREEN_HEIGTH - 20, Color.GOLD);
        batch.end();

        HUDScene.draw();
        if (youLost) {
            pauseIP();
            Gdx.input.setInputProcessor(lostScene);
            lostScene.act(delta);
            lostScene.draw();
            //return;
        }
        else if (youWon) {
            PreferencesSB.savingLevelProgress(level+1);
            Gdx.input.setInputProcessor(winningScene);
            winningScene.draw();
            //return;
        }
        else if (isPaused) {
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
    //TODO manejar transiciones y demás cosas
    private void handleFondo(float dt) {
        batch.draw(loopingBackground, 0, posY);
        batch.draw(loopingBackground, 0, posY + loopingBackground.getHeight());
        if (!isPaused) {
            posY -= backgroundSpeed * dt;
            if (posY <= 0 - loopingBackground.getHeight()) {
                posY = 0;
            }
        }
    }

    private void handleGame(float dt) {
        if (!isPaused) {
            handleWaves();
            spawnEnemies(dt);
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
        lifeBar = new ProgressBar(Constant.MANAGER.get("HUD/LifeBarBar.png", Texture.class), true);
        lifeBar.setFrame(Constant.MANAGER.get("HUD/LifeBarFrame.png", Texture.class));
        lifeBar.setPosition(9 * Constant.SCREEN_WIDTH / 10 + 40, 2 * Constant.SCREEN_HEIGTH / 8);
        HUDScene.addActor(lifeBar);
    }

    private void createPauseButton() {
        Skin skin = new Skin();
        skin.add("Pause", Constant.MANAGER.get("GameScreen/Pause.png", Texture.class));

        Button.ButtonStyle estilo = new Button.ButtonStyle();
        estilo.up = skin.getDrawable("Pause");

        pauseButton = new Button(estilo);
        pauseButton.setPosition(11 * Constant.SCREEN_WIDTH / 12,
                9 * Constant.SCREEN_HEIGTH / 10);
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
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
                playerShooting = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                playerShooting = false;
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
                handleCollision(objectA, objectB);
                handleCollision(objectB, objectA);

                lifeBar.setPorcentage(player.life / player.totalLife);
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

    private void handleCollision(IPlayableEntity a, IPlayableEntity b) {
        if (a.doDamage(b.getDamage())) {
            a.setDamage(0);
            toRemove.add(a.getBody());
            if (a instanceof Ship) {
                animations.add(new AutoAnimation(Constant.MANAGER.get("Animations/ExplosionFrames.png", Texture.class), 0.15f, a.getX(), a.getY(), 100, 100, batch));
            }
            if (a instanceof ShipEnemy) {
                enemies.remove(a);
            }
            if (a instanceof ShipPlayer) {
                animations.add(new AutoAnimation(Constant.MANAGER.get("Animations/ExplosionFrames.png", Texture.class), 0.15f, a.getX(), a.getY(), 100, 100, batch));
                isPaused = true;
                youLost = true;
            }
        }
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
            //TODO hacerlo más generico si es necesario
            ShipEnemy enemy = new ShipEnemy(Constant.MANAGER.get("GameScreen/Enemy" + (random.nextInt(3) + 1) + "Sprite.png", Texture.class), random.nextInt((int) Constant.SCREEN_WIDTH), Constant.SCREEN_HEIGTH + 50, world);
            enemy.scaling(Constant.SHIPS_SCALE);
            enemies.add(enemy);
        }
    }

    private void moveEnemies(float delta) {
        Vector2 target = new Vector2(player.getX(), player.getY());
        for (ShipEnemy enemy : enemies) {
            enemy.move(target, delta);
            enemy.shoot(TimeUtils.millis());
        }
    }

    private void handleWaves() {
        if (spawnedEnemiesForThisWave == numberEnemiesForThisWave && enemies.size() == 0) {
            if (waveNumber < numberOfWaves) {
                waveNumber++;
                timeSinceLastSpawn = 0;
                spawnedEnemiesForThisWave = 0;
                numberEnemiesForThisWave += extraPerWave;
                switchingWaves = true;
            } else {
                //TODO se acabo el juego (ganaste)
                isPaused = true;
                youWon = true;
            }
        }
    }
    //endregion


}