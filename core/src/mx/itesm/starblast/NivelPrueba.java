package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
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

class NivelPrueba implements Screen, IPausable {

    private static final int ENEMIGOS_INICIALES = 1;
    private static long COOLDOWN_ENEMIGO = 3000;
    private final StarBlast menu;

    //Camara, view
    private OrthographicCamera camara;
    private Viewport vista;

    //Escenas
    private Stage escenaJuego;

    //Sprites
    private ArrayList<NaveEnemiga> enemigos;

    private Vector2 target;

    private OrthographicCamera camaraHUD;
    private Viewport vistaHUD;
    private Stage escenaHUD;

    private Touchpad touchpad;

    private Button botonPausa;
    private Button botonDisparo;
    private ProgressBar barraVida;

    private NaveJugador jugador;

    private World world;

    private float accumulator = 0;

    private ShapeRenderer shapeRenderer;

    private boolean isPaused = false;
    private StagePausa escenaPausa;
    private StageResultados escenaResultados;


    private boolean disparando = false;

    private HashSet<Body> toRemove;

    private LinkedList<AutoAnimation> animations = new LinkedList<AutoAnimation>();

    private boolean gameEnded = false;
    private boolean ganador = false;

    //Elementos para el fondo
    private Texture texturaFondo = Constant.MANAGER.get("PantallaJuego/FondoNivel2.jpg", Texture.class);
    private SpriteBatch batch;
    private Sprite spriteFondo;
    private int posY = 0;

    //Puntaje
    private Text textScore;
    private Text textShips;
    private int puntaje = 0;
    private int navesRestantes = 12;

    //Oleadas
    private long tiempoInicio;
    private boolean oleada1Realizada = false;
    private boolean oleada2Realizada = false;
    private boolean oleada3Realizada = false;
    private long enemigoAnterior = 0;

    NivelPrueba(StarBlast menu) {
        this.menu = menu;
    }

    @Override
    public void show() {
        crearCamara();
        crearObjetos();
        tiempoInicio = TimeUtils.millis();
    }

    //region metodos show

    private void crearCamara() {
        camara = new OrthographicCamera(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGTH);
        camara.position.set(Constant.SCREEN_WIDTH / 2, Constant.SCREEN_HEIGTH / 2, 0);
        camara.update();
        vista = new StretchViewport(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGTH, camara);
    }

    private void crearObjetos() {
        target = new Vector2(Constant.SCREEN_WIDTH / 2, Constant.SCREEN_HEIGTH / 2);
        batch = new SpriteBatch();
        escenaJuego = new Stage(vista, batch);
        //Image imgFondo = new Image(texturaFondo);
        enemigos = new ArrayList<NaveEnemiga>();
        //escenaJuego.addActor(imgFondo);

        crearWorld();
        crearBordes();
        crearSprites();
        crearHud();

        //Gdx.input.setInputProcessor(new Procesador());
        Gdx.input.setInputProcessor(escenaHUD);
        shapeRenderer = new ShapeRenderer();

        textScore = new Text(Constant.SOURCE_TEXT);
        textShips = new Text(Constant.SOURCE_TEXT);

        escenaPausa = new StagePausa(vista, batch, menu, this);
        escenaResultados = new StageResultados(vista, batch, menu, this);
    }

    //region metodos crearObjetos

    private void crearWorld() {
        world = new World(Vector2.Zero, true);
        accumulator = 0;
        toRemove = new HashSet<Body>();
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                IPlayableEntity objetoA = (IPlayableEntity) contact.getFixtureA().getBody().getUserData();
                IPlayableEntity objetoB = (IPlayableEntity) contact.getFixtureB().getBody().getUserData();
                if(objetoA == null || objetoB == null){
                    return;
                }
                if(objetoA.doDamage(objetoB.getDamage())){
                    objetoA.setDamage(0);
                    toRemove.add(contact.getFixtureA().getBody());
                    if(objetoA instanceof NaveJugador){
                        animations.add(new AutoAnimation(new Texture("Animaciones/ExplosionNaveFrames.png"),0.15f,jugador.getX(),jugador.getY(),100,100,batch));
                        gameEnded = true;
                    }else if(objetoA instanceof NaveEnemiga){
                        NaveEnemiga nve = (NaveEnemiga) objetoA;
                        animations.add(new AutoAnimation(new Texture("Animaciones/ExplosionNaveFrames.png"),0.15f,nve.getX(),nve.getY(),100,100,batch));
                        enemigos.remove(nve);
                        puntaje += 100;
                        navesRestantes--;
                        if(navesRestantes==0){
                            gameEnded = true;
                            ganador = true;
                        }
                    }
                }
                if(objetoB.doDamage(objetoA.getDamage())){
                    objetoB.setDamage(0);
                    toRemove.add(contact.getFixtureB().getBody());
                    if(objetoB instanceof NaveJugador){
                        animations.add(new AutoAnimation(new Texture("Animaciones/ExplosionNaveFrames.png"),0.15f,jugador.getX(),jugador.getY(),100,100,batch));
                        gameEnded = true;
                    }else if(objetoB instanceof NaveEnemiga){
                        NaveEnemiga nve = (NaveEnemiga) objetoB;
                        animations.add(new AutoAnimation(new Texture("Animaciones/ExplosionNaveFrames.png"),0.15f,nve.getX(),nve.getY(),100,100,batch));
                        enemigos.remove(nve);
                        puntaje += 100;
                        navesRestantes--;
                        if(navesRestantes == 0){
                            gameEnded = true;
                            ganador = true;
                        }
                    }
                }
                barraVida.setPorcentage(jugador.vida/100f);
//                Gdx.app.log("Vida",""+jugador.vida);
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

    private void crearSprites() {
        //Sprites Complejos
        jugador = new NaveJugador(Constant.MANAGER.get("PantallaJuego/AvatarSprite.png", Texture.class), Constant.SCREEN_WIDTH / 2, Constant.SCREEN_WIDTH / 5, world);
        jugador.escalar(Constant.SHIPS_SCALE);
        //Sprite del fondo
        spriteFondo = new Sprite(texturaFondo);
        spriteFondo.setPosition(0,0);
    }

    //region metodos crearSprites

    private void crearEnemigos() {
        NaveEnemiga enemigo;
        Random r = new Random();
        for (int i = 0; i < ENEMIGOS_INICIALES; i++) {
            enemigo = new NaveEnemiga(Constant.MANAGER.get("PantallaJuego/Enemigo" + (r.nextBoolean() ? "1" : "2") + "Sprite.png", Texture.class), r.nextInt((int) Constant.SCREEN_WIDTH), Constant.SCREEN_HEIGTH + 50, world);
//            enemigo = new NaveEnemiga("PantallaJuego/Enemigo1.png",3*Constant.SCREEN_WIDTH/4,Constant.SCREEN_HEIGTH/3,world);
            //enemigo = new JefeEnemigo("PantallaJuego/Enemigo" + (r.nextBoolean() ? "1" : "2") + "Sprite.png", r.nextInt((int) Constant.SCREEN_WIDTH), Constant.SCREEN_HEIGTH, world,300);

            enemigo.escalar(Constant.SHIPS_SCALE);
            enemigos.add(enemigo);
        }
    }

    private void crearJefeNivel() {
        Random r = new Random();
        NaveEnemiga jefe = new JefeEnemigo(Constant.MANAGER.get("PantallaJuego/NaveJefe.png", Texture.class), r.nextInt((int) Constant.SCREEN_WIDTH), Constant.SCREEN_HEIGTH, world,300);
        jefe.escalar(Constant.SHIPS_SCALE);
        enemigos.add(jefe);
    }
    //endregion

    private void crearHud() {
        camaraHUD = new OrthographicCamera(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGTH);
        camaraHUD.position.set(Constant.SCREEN_WIDTH / 2, Constant.SCREEN_HEIGTH / 2, 0);
        camaraHUD.update();
        vistaHUD = new StretchViewport(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGTH, camaraHUD);

        crearPad();
        crearBarraVida();
        crearBotonPausa();
        crearBotonDisparo();
    }

    //region metodos crearHud
    private void crearPad() {

        Skin skin = new Skin();
        skin.add("PadBack", Constant.MANAGER.get("HUD/JoystickPad.png",Texture.class));
        skin.add("PadKnob", Constant.MANAGER.get("HUD/JoystickStick.png",Texture.class));

        Touchpad.TouchpadStyle estilo = new Touchpad.TouchpadStyle();
        estilo.background = skin.getDrawable("PadBack");
        estilo.knob = skin.getDrawable("PadKnob");

        touchpad = new Touchpad(20, estilo);
        touchpad.setBounds(25, 50, 200, 200);

        touchpad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Touchpad pad = (Touchpad) actor;

                if (Math.abs(pad.getKnobPercentX()) > Constant.TOUCHPAD_DEADZONE) {
                    jugador.girar(pad.getKnobPercentX());
                } else {
                    jugador.girar(0);
                }
                if (Math.abs(pad.getKnobPercentY()) > Constant.TOUCHPAD_DEADZONE) {
                    jugador.acelerar(pad.getKnobPercentY());
                } else {
                    jugador.acelerar(0);
                }
            }
        });

        escenaHUD = new Stage(vistaHUD);
        escenaHUD.addActor(touchpad);
    }

    private void crearBotonPausa() {
        float escala = 0.3f;

        Skin skin = new Skin();
        skin.add("Pausa", Constant.MANAGER.get("PantallaJuego/Pausa.png", Texture.class));

        Button.ButtonStyle estilo = new Button.ButtonStyle();
        estilo.up = skin.getDrawable("Pausa");

        botonPausa = new Button(estilo);
        botonPausa.scaleBy(escala);
        botonPausa.setPosition(11 * Constant.SCREEN_WIDTH / 12,
                9 * Constant.SCREEN_HEIGTH / 10);
        botonPausa.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Button boton = (Button) actor;
                if (boton.isPressed()) {
                    if (!isPaused) {
                        pausa();
                    } else {
                        unPause();
                    }
                }
            }
        });

        escenaHUD.addActor(botonPausa);
    }

    private void crearBotonDisparo() {
        float escala = 0.3f;

        Skin skin = new Skin();
        skin.add("DisparoStandby", Constant.MANAGER.get("HUD/BotonAStandby.png",Texture.class));
        skin.add("DisparoPresionado", Constant.MANAGER.get("HUD/BotonAPresionado.png",Texture.class));
        Button.ButtonStyle estilo = new Button.ButtonStyle();
        estilo.down = skin.getDrawable("DisparoPresionado");
        estilo.up = skin.getDrawable("DisparoStandby");

        botonDisparo = new Button(estilo);
        botonDisparo.scaleBy(escala);
        botonDisparo.setPosition(13 * Constant.SCREEN_WIDTH / 16,
                1 * Constant.SCREEN_HEIGTH / 10);

        botonDisparo.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                disparando = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                disparando = false;
            }
        });
        escenaHUD.addActor(botonDisparo);
    }

    private void crearBarraVida() {
        barraVida = new ProgressBar(Constant.MANAGER.get("HUD/LifeBarBar.png",Texture.class),true);
        barraVida.setFrame(Constant.MANAGER.get("HUD/LifeBarFrame.png",Texture.class));
        barraVida.setPosition(9 * Constant.SCREEN_WIDTH / 10+40, 2* Constant.SCREEN_HEIGTH / 8);
        escenaHUD.addActor(barraVida);
    }


    //endregion

    //endregion

    //endregion


    //region metodos pausa
    @Override
    public void pausa() {
        isPaused = true;
        escenaPausa.addActor(botonPausa);
        if(!gameEnded)
            Gdx.input.setInputProcessor(escenaPausa);
        else {
            Gdx.input.setInputProcessor(escenaResultados);
            Gdx.app.log("Pausa","EscenaControl");
        }
        Gdx.app.log("Pausa","pausa");
    }

    @Override
    public void unPause() {
        isPaused = false;
        escenaHUD.addActor(botonPausa);
        Gdx.input.setInputProcessor(escenaHUD);
        Gdx.app.log("Despausa","despausa");
    }

    //endregion

    @Override
    public void render(float delta) {
        borrarPantalla();

        if(!gameEnded){
            procesarJuego(delta);
        }
        dibujarElementos();
//        debugearElementos();

        if(gameEnded && animations.size()==0){
            if(!isPaused){
                pausa();
                Gdx.app.log("PARA EL JUEGO", Boolean.toString(ganador));
                escenaResultados.setGanadorYPuntaje(ganador, puntaje);
            }
            escenaResultados.draw();
        }
        moverFondo();

        //Oleadas de enemigos
        //TODO mejorar la forma en la que salen
        if(enemigoAnterior + COOLDOWN_ENEMIGO < TimeUtils.nanosToMillis(TimeUtils.nanoTime()) && !gameEnded && !isPaused && enemigos.size() < navesRestantes){
            enemigoAnterior = TimeUtils.nanosToMillis(TimeUtils.nanoTime());
            if(navesRestantes-enemigos.size() != 1) {
                crearEnemigos();
            }
            else{
                crearJefeNivel();
            }

        }

    }


    //region metodos render

    private void moverFondo(){
        if(posY >= -3200 ){
            if(!isPaused) {
                posY--;
                spriteFondo.setPosition(0,posY);
            }
        }
    }

    private void borrarPantalla() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void procesarJuego(float delta) {
        if (!isPaused) {
            update(delta);
            moverEnemigos(delta);
            moverJugador(delta);
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

    private void moverEnemigos(float delta) {
        target = new Vector2(jugador.getX(), jugador.getY());
        //target = new Vector2(Constant.SCREEN_WIDTH/2,Constant.SCREEN_HEIGTH/2);
        for(NaveEnemiga enemigo:enemigos){
            enemigo.mover(target,delta);
            //se utiliza el metodo nanos en vez de millis porque millis
            //cuenta el tiempo desde 1970

            enemigo.disparar(TimeUtils.nanosToMillis(TimeUtils.nanoTime()));
        }
    }

    private void moverJugador(float delta) {
        jugador.mover(target,delta);
        if(disparando){
            //se utiliza el metodo nanos en vez de millis porque millis
            //cuenta el tiempo desde 1970
            jugador.disparar(TimeUtils.nanosToMillis(TimeUtils.nanoTime()));
        }
    }
    //endregion

    private void dibujarElementos() {

        batch.begin();
        spriteFondo.draw(batch);
        batch.end();

        escenaJuego.draw();

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

        textScore.showMessage(batch, "Puntaje: "+Integer.toString(puntaje),
                10, Constant.SCREEN_HEIGTH -20, Color.GOLD);
        textShips.showMessage(batch, "Enemigos: "+Integer.toString(navesRestantes),
                Constant.SCREEN_WIDTH /2, Constant.SCREEN_HEIGTH -20, Color.ORANGE);

        batch.end();

        batch.setProjectionMatrix(camaraHUD.combined);
        escenaHUD.draw();
        if (isPaused) {
            escenaPausa.draw();
        }
    }
//     region debug shapes
//    private void debugearElementos() {
//        if (enemigos.size() == 0) {
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
//        for (NaveEnemiga enemigo : enemigos) {
//            shapeRenderer.circle(enemigo.getX(), enemigo.getY(), Constant.toScreenSize(enemigo.getShape().getRadius()));
//        }
//        shapeRenderer.circle(jugador.getX(), jugador.getY(), Constant.toScreenSize(jugador.getShape().getRadius()));
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
//        shapeRenderer.line(enemigos.get(0).getX(), enemigos.get(0).getY(), target.x, target.y);
//
//        shapeRenderer.end();
//
//    }
//    endregion

    //endregion

    private void crearBordes() {
        new Borde(world,-120,0,100, Constant.SCREEN_HEIGTH);
        new Borde(world, Constant.SCREEN_WIDTH +120,0,100, Constant.SCREEN_HEIGTH);
        new Borde(world,-120,-120, Constant.SCREEN_WIDTH +200,100);
        new Borde(world,-120, Constant.SCREEN_HEIGTH +120, Constant.SCREEN_WIDTH +200,100);
    }

    @Override
    public void resize(int width, int height) {
        vista.update(width, height);
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