package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

class LevelProof implements Screen, IPausable {

    private static final int INITIAL_ENEMIES = 1;
    private static long ENEMIES_COOLDOWN = 3000;
    private final StarBlast menu;

    //Camara, view
    private OrthographicCamera camera;
    private Viewport view;

    //Escenas
    private Stage gameScene;

    //Sprites
    private ArrayList<ShipEnemy> enemies;

    private Vector2 target;

    private OrthographicCamera HUDcamera;
    private Viewport HUDview;
    private Stage HUDstage;

    private Touchpad touchpad;

    private Button pauseButton;
    private Button shootingButton;
    private ProgressBar lifeBar;

    private ShipPlayer player;

    private World world;

    private float accumulator = 0;

    private ShapeRenderer shapeRenderer;

    private boolean isPaused = false;
    private StagePause pauseScene;
    private StageResults resultsScene;


    private boolean shooting = false;

    private HashSet<Body> toRemove;

    private LinkedList<AutoAnimation> animations = new LinkedList<AutoAnimation>();

    private boolean gameEnded = false;
    private boolean winner = false;

    //Elementos para el fondo
    private Texture backgroundTexture = Constant.MANAGER.get("PantallaJuego/FondoNivel2.jpg", Texture.class);
    private SpriteBatch batch;
    private Sprite spriteFondo;
    private int posY = 0;

    //Puntaje
    private Text textScore;
    private Text textShips;
    private int score = 0;
    private int leftShips = 12;

    //Oleadas
    private long previousEnemy = 0;

    LevelProof(StarBlast menu) {
        this.menu = menu;
    }

    @Override
    public void show() {
        createCamera();
        createObjects();
    }

    //region metodos show

    private void createCamera() {
        camera = new OrthographicCamera(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGTH);
        camera.position.set(Constant.SCREEN_WIDTH / 2, Constant.SCREEN_HEIGTH / 2, 0);
        camera.update();
        view = new StretchViewport(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGTH, camera);
    }

    private void createObjects() {
        target = new Vector2(Constant.SCREEN_WIDTH / 2, Constant.SCREEN_HEIGTH / 2);
        batch = new SpriteBatch();
        gameScene = new Stage(view, batch);
        //Image imgFondo = new Image(backgroundTexture);
        enemies = new ArrayList<ShipEnemy>();
        //gameScene.addActor(imgFondo);

        createWorld();
        createEdges();
        createSprites();
        createHUD();

        //Gdx.input.setInputProcessor(new Procesador());
        Gdx.input.setInputProcessor(HUDstage);
        shapeRenderer = new ShapeRenderer();

        textScore = new Text(Constant.SOURCE_TEXT);
        textShips = new Text(Constant.SOURCE_TEXT);

        pauseScene = new StagePause(view, batch, menu, this);
        resultsScene = new StageResults(view, batch, menu, this);
    }

    //region metodos crearObjetos

    private void createWorld() {
        world = new World(Vector2.Zero, true);
        accumulator = 0;
        toRemove = new HashSet<Body>();
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                IPlayableEntity objectA = (IPlayableEntity) contact.getFixtureA().getBody().getUserData();
                IPlayableEntity objectB = (IPlayableEntity) contact.getFixtureB().getBody().getUserData();
                if(objectA == null || objectB == null){
                    return;
                }
                if(objectA.doDamage(objectB.getDamage())){
                    objectA.setDamage(0);
                    toRemove.add(contact.getFixtureA().getBody());
                    if(objectA instanceof ShipPlayer){
                        animations.add(new AutoAnimation(new Texture("Animaciones/ExplosionNaveFrames.png"),0.15f, player.getX(), player.getY(),100,100,batch));
                        gameEnded = true;
                    }else if(objectA instanceof ShipEnemy){
                        ShipEnemy ship = (ShipEnemy) objectA;
                        animations.add(new AutoAnimation(new Texture("Animaciones/ExplosionNaveFrames.png"),0.15f,ship.getX(),ship.getY(),100,100,batch));
                        enemies.remove(ship);
                        score += 100;
                        leftShips--;
                        if(leftShips ==0){
                            gameEnded = true;
                            winner = true;
                        }
                    }
                }
                if(objectB.doDamage(objectA.getDamage())){
                    objectB.setDamage(0);
                    toRemove.add(contact.getFixtureB().getBody());
                    if(objectB instanceof ShipPlayer){
                        animations.add(new AutoAnimation(new Texture("Animaciones/ExplosionNaveFrames.png"),0.15f, player.getX(), player.getY(),100,100,batch));
                        gameEnded = true;
                    }else if(objectB instanceof ShipEnemy){
                        ShipEnemy ship = (ShipEnemy) objectB;
                        animations.add(new AutoAnimation(new Texture("Animaciones/ExplosionNaveFrames.png"),0.15f,ship.getX(),ship.getY(),100,100,batch));
                        enemies.remove(ship);
                        score += 100;
                        leftShips--;
                        if(leftShips == 0){
                            gameEnded = true;
                            winner = true;
                        }
                    }
                }
                lifeBar.setPorcentage(player.life /100f);
//                Gdx.app.log("Vida",""+player.life);
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

    private void createSprites() {
        //Sprites Complejos
        player = new ShipPlayer(Constant.MANAGER.get("PantallaJuego/AvatarSprite.png", Texture.class), Constant.SCREEN_WIDTH / 2, Constant.SCREEN_WIDTH / 5, world);
        player.scaling(Constant.SHIPS_SCALE);
        //Sprite del fondo
        spriteFondo = new Sprite(backgroundTexture);
        spriteFondo.setPosition(0,0);
    }

    //region metodos crearSprites

    private void createEnemies() {
        ShipEnemy enemy;
        Random r = new Random();
        for (int i = 0; i < INITIAL_ENEMIES; i++) {
            enemy = new ShipEnemy(Constant.MANAGER.get("PantallaJuego/Enemigo" + (r.nextBoolean() ? "1" : "2") + "Sprite.png", Texture.class), r.nextInt((int) Constant.SCREEN_WIDTH), Constant.SCREEN_HEIGTH + 50, world);
//            enemigo = new ShipEnemy("PantallaJuego/Enemigo1.png",3*Constant.SCREEN_WIDTH/4,Constant.SCREEN_HEIGTH/3,world);
            //enemigo = new ShipEnemyBoss("PantallaJuego/Enemigo" + (r.nextBoolean() ? "1" : "2") + "Sprite.png", r.nextInt((int) Constant.SCREEN_WIDTH), Constant.SCREEN_HEIGTH, world,300);

            enemy.scaling(Constant.SHIPS_SCALE);
            enemies.add(enemy);
        }
    }

    private void createBossLevel() {
        Random r = new Random();
        ShipEnemy boss = new ShipEnemyBoss(Constant.MANAGER.get("PantallaJuego/NaveJefe.png", Texture.class), r.nextInt((int) Constant.SCREEN_WIDTH), Constant.SCREEN_HEIGTH, world,300);
        boss.scaling(Constant.SHIPS_SCALE);
        enemies.add(boss);
    }
    //endregion

    private void createHUD() {
        HUDcamera = new OrthographicCamera(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGTH);
        HUDcamera.position.set(Constant.SCREEN_WIDTH / 2, Constant.SCREEN_HEIGTH / 2, 0);
        HUDcamera.update();
        HUDview = new StretchViewport(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGTH, HUDcamera);

        createPad();
        createLifeBar();
        createPauseButton();
        createShootingButton();
    }

    //region metodos crearHud
    private void createPad() {

        Skin skin = new Skin();
        skin.add("PadBack", Constant.MANAGER.get("HUD/JoystickPad.png",Texture.class));
        skin.add("PadKnob", Constant.MANAGER.get("HUD/JoystickStick.png",Texture.class));

        Touchpad.TouchpadStyle style = new Touchpad.TouchpadStyle();
        style.background = skin.getDrawable("PadBack");
        style.knob = skin.getDrawable("PadKnob");

        touchpad = new Touchpad(20, style);
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

        HUDstage = new Stage(HUDview);
        HUDstage.addActor(touchpad);
    }

    private void createPauseButton() {
        float scale = 0.3f;

        Skin skin = new Skin();
        skin.add("Pausa", Constant.MANAGER.get("PantallaJuego/Pausa.png", Texture.class));

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("Pausa");

        pauseButton = new Button(style);
        pauseButton.scaleBy(scale);
        pauseButton.setPosition(11 * Constant.SCREEN_WIDTH / 12,
                9 * Constant.SCREEN_HEIGTH / 10);
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Button button = (Button) actor;
                if (button.isPressed()) {
                    if (!isPaused) {
                        pauseIP();
                    } else {
                        unPauseIP();
                    }
                }
            }
        });

        HUDstage.addActor(pauseButton);
    }

    private void createShootingButton() {
        float scale = 0.3f;

        Skin skin = new Skin();
        skin.add("StandbyShot", Constant.MANAGER.get("HUD/BotonAStandby.png",Texture.class));
        skin.add("PressedShot", Constant.MANAGER.get("HUD/BotonAPresionado.png",Texture.class));
        Button.ButtonStyle style = new Button.ButtonStyle();
        style.down = skin.getDrawable("PressedShot");
        style.up = skin.getDrawable("StandbyShot");

        shootingButton = new Button(style);
        shootingButton.scaleBy(scale);
        shootingButton.setPosition(13 * Constant.SCREEN_WIDTH / 16,
                1 * Constant.SCREEN_HEIGTH / 10);

        shootingButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                shooting = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                shooting = false;
            }
        });
        HUDstage.addActor(shootingButton);
    }

    private void createLifeBar() {
        lifeBar = new ProgressBar(Constant.MANAGER.get("HUD/LifeBarBar.png",Texture.class),true);
        lifeBar.setFrame(Constant.MANAGER.get("HUD/LifeBarFrame.png",Texture.class));
        lifeBar.setPosition(9 * Constant.SCREEN_WIDTH / 10+40, 2* Constant.SCREEN_HEIGTH / 8);
        HUDstage.addActor(lifeBar);
    }


    //endregion

    //endregion

    //endregion


    //region metodos pausa
    @Override
    public void pauseIP() {
        isPaused = true;
        pauseScene.addActor(pauseButton);
        if(!gameEnded)
            Gdx.input.setInputProcessor(pauseScene);
        else {
            Gdx.input.setInputProcessor(resultsScene);
            Gdx.app.log("Pausa","EscenaControl");
        }
        Gdx.app.log("Pausa","pausa");
    }

    @Override
    public void unPauseIP() {
        isPaused = false;
        HUDstage.addActor(pauseButton);
        Gdx.input.setInputProcessor(HUDstage);
        Gdx.app.log("Despausa","despausa");
    }

    //endregion

    @Override
    public void render(float delta) {
        clearScreen();

        if(!gameEnded){
            processGame(delta);
        }
        drawElements();
//        debugearElementos();

        if(gameEnded && animations.size()==0){
            if(!isPaused){
                pauseIP();
                resultsScene.setWinnnerAndScore(winner, score);
            }
            resultsScene.draw();
        }
        backgroundMove();

        //Oleadas de enemies
        //TODO mejorar la forma en la que salen
        if(previousEnemy + ENEMIES_COOLDOWN < TimeUtils.nanosToMillis(TimeUtils.nanoTime()) && !gameEnded && !isPaused && enemies.size() < leftShips){
            previousEnemy = TimeUtils.nanosToMillis(TimeUtils.nanoTime());
            if(leftShips - enemies.size() != 1) {
                createEnemies();
            }
            else{
                createBossLevel();
            }

        }

    }


    //region metodos render

    private void backgroundMove(){
        if(posY >= -3200 ){
            if(!isPaused) {
                posY--;
                spriteFondo.setPosition(0,posY);
            }
        }
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void processGame(float delta) {
        if (!isPaused) {
            update(delta);
            moveEnemies(delta);
            movePlayer(delta);
        }
    }

    //region metodos procesarJuego
    private void update(float dt) {
        accumulator += dt;
        //TODO checar porque al final de todos los steps
        while (accumulator > 1 / 120f) {
            world.step(1 / 120f, 8, 3);
            accumulator -= 1 / 120f;
        }
        for(Body b: toRemove){
            while (b.getFixtureList().size > 0){
                b.destroyFixture(b.getFixtureList().first());
            }
            world.destroyBody(b);
        }
        toRemove.clear();
    }

    private void moveEnemies(float delta) {
        target = new Vector2(player.getX(), player.getY());
        //target = new Vector2(Constant.SCREEN_WIDTH/2,Constant.SCREEN_HEIGTH/2);
        for(ShipEnemy enemy: enemies){
            enemy.move(target,delta);
            //se utiliza el metodo nanos en vez de millis porque millis
            //cuenta el tiempo desde 1970

            enemy.shoot(TimeUtils.nanosToMillis(TimeUtils.nanoTime()));
        }
    }

    private void movePlayer(float delta) {
        player.move(target,delta);
        if(shooting){
            //se utiliza el metodo nanos en vez de millis porque millis
            //cuenta el tiempo desde 1970
            player.shoot(TimeUtils.nanosToMillis(TimeUtils.nanoTime()));
        }
    }
    //endregion

    private void drawElements() {

        batch.begin();
        spriteFondo.draw(batch);
        batch.end();

        gameScene.draw();

        batch.begin();

        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);
        Object object;
        for(Body b:bodies) {
            object = b.getUserData();
            if (object instanceof IPlayableEntity) {
                ((IPlayableEntity) object).draw(batch);
            }
        }
        Iterator<AutoAnimation> it = animations.iterator();
        AutoAnimation anim;
        while (it.hasNext()){
            anim = it.next();
            if(anim.draw(batch,Gdx.graphics.getDeltaTime())){
                it.remove();
            }
        }

        textScore.showMessage(batch, "Puntaje: "+Integer.toString(score),
                10, Constant.SCREEN_HEIGTH -20, Color.GOLD);
        textShips.showMessage(batch, "Enemigos: "+Integer.toString(leftShips),
                Constant.SCREEN_WIDTH /2, Constant.SCREEN_HEIGTH -20, Color.ORANGE);

        batch.end();

        batch.setProjectionMatrix(HUDcamera.combined);
        HUDstage.draw();
        if (isPaused) {
            pauseScene.draw();
        }
    }
//     region debug shapes
//    private void debugearElementos() {
//        if (enemies.size() == 0) {
//            return;
//        }
//        camera.update();
//
//
//        Gdx.gl.glEnable(GL20.GL_BLEND);
//        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//
//        shapeRenderer.setProjectionMatrix(camera.combined);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//
//
//        shapeRenderer.setColor(new Color(0, 1, 0, 0.5f));
//        for (ShipEnemy enemigo : enemies) {
//            shapeRenderer.circle(enemigo.getX(), enemigo.getY(), Constant.toScreenSize(enemigo.getShape().getRadius()));
//        }
//        shapeRenderer.circle(player.getX(), player.getY(), Constant.toScreenSize(player.getShape().getRadius()));
//
//        shapeRenderer.setColor(new Color(1, 0, 0, 0.7f));
//        /*for (Bullet bala : balas) {
//            shapeRenderer.circle(bala.getX(), bala.getY(), Constant.toScreenSize(bala.getShape().getRadius()));
//        }*/
//
//
//        shapeRenderer.end();
//
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//
//        shapeRenderer.setColor(new Color(0, 1, 0, 0.5f));
//        shapeRenderer.line(enemies.get(0).getX(), enemies.get(0).getY(), target.x, target.y);
//
//        shapeRenderer.end();
//
//    }
//    endregion

    //endregion

    private void createEdges() {
        new Edge(world,-120,0,100, Constant.SCREEN_HEIGTH);
        new Edge(world, Constant.SCREEN_WIDTH +120,0,100, Constant.SCREEN_HEIGTH);
        new Edge(world,-120,-120, Constant.SCREEN_WIDTH +200,100);
        new Edge(world,-120, Constant.SCREEN_HEIGTH +120, Constant.SCREEN_WIDTH +200,100);
    }

    @Override
    public void resize(int width, int height) {
        view.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        Constant.MANAGER.dispose();
    }

}