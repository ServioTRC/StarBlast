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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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

class NivelHistoria extends Pantalla implements IPausable {

    //region background
    Texture loopingBackground;
    private float posY = 0;
    private float backgroundSpeed = 50;
    //endregion

    //region estados del juego
    private boolean isPaused;
    private boolean switchingWaves;
    private boolean perdiste;
    private boolean ganaste;
    //endregion

    //region score
    private int puntaje;
    private Texto textoPuntaje = new Texto(Constantes.TEXTO_FUENTE);
    //endregion

    //region hud
    private final Stage escenaHUD;
    private ProgressBar barraVida;
    private Button botonPausa;
    //endregion

    //region escenas
    private final StagePausa escenaPausa;
    private StagePerder escenaPerdiste;
    //endregion

    // region world
    private final World world = new World(Vector2.Zero, true);
    private final HashSet<Body> toRemove = new HashSet<Body>();
    private float accumulator = 0;
    //hacer más grande este numero mejora el rendimiento pero hace más rapido el juego
    private final float timeBetweenFrames = 1 / 120f;
    //endregion

    //region enemigos
    final int enemigosIniciales;
    final int extraPerWave;
    final int numberOfWaves;
    final float spawnTimeuot = 0.1f;
    float timeSinceLastSpawn = 0;
    float numberEnemiesForThisWave;
    float spawnedEnemiesForThisWave = 0;
    int waveNumber = 1;
    float timeoutBetweenWaves = 5;

    ArrayList<NaveEnemiga> enemigos = new ArrayList<NaveEnemiga>();
    //endregion

    //region jugador
    private boolean jugadorDisparando;
    private NaveJugador jugador;
    //endregion

    //region animaciones
    private final LinkedList<AutoAnimation> animations = new LinkedList<AutoAnimation>();
    //endregion

    //region other
    private final StarBlast app;
    private final SpriteBatch batch = new SpriteBatch();
    Random random = new Random();
    //endregion

    NivelHistoria(StarBlast app, int enemigosIniciales, int extraPerWave, int numberOfWaves) {
        super();
        this.app = app;
        escenaHUD = new Stage(vista, batch);
        escenaPausa = new StagePausa(vista, batch, app, this);
        escenaPerdiste = new StagePerder(vista, batch, app);
        this.enemigosIniciales = enemigosIniciales;
        this.extraPerWave = extraPerWave;
        this.numberOfWaves = numberOfWaves;
        numberEnemiesForThisWave = enemigosIniciales;
    }

    //region metodos Pantalla

    @Override
    public void show() {
        crearHUD();
        Gdx.input.setInputProcessor(escenaHUD);
        crearWorld();
        crearBordes();

        jugador = new NaveJugador(Constantes.MANAGER.get("PantallaJuego/AvatarSprite.png", Texture.class), Constantes.ANCHO_PANTALLA / 2, Constantes.ANCHO_PANTALLA / 5, world);
        jugador.escalar(Constantes.ESCALA_NAVES);
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
//        textoPuntaje.mostrarMensaje(batch, "Puntaje: " + puntaje, 20, Constantes.ALTO_PANTALLA - 20, Color.GOLD);
        batch.end();

        escenaHUD.draw();
        if (perdiste) {
            escenaPerdiste.act(Gdx.graphics.getDeltaTime());
            escenaPerdiste.draw();
            return;
        }
        if (ganaste) {
            return;
        }
        if (isPaused) {
            escenaPausa.draw();
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
    public void pausa() {
        isPaused = true;
        escenaPausa.addActor(botonPausa);
        Gdx.input.setInputProcessor(escenaPausa);
    }

    @Override
    public void unPause() {
        isPaused = false;
        escenaHUD.addActor(botonPausa);
        Gdx.input.setInputProcessor(escenaHUD);
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
            moverJugador(dt);
            moverEnemigos(dt);
        }
    }

    //endregion

    //region metodos hud

    private void crearHUD() {
        crearPad();
        crearBarraVida();
        crearBotonPausa();
        crearBotonDisparo();
        crearBotonEspecial();
    }


    private void crearPad() {
        Skin skin = new Skin();
        skin.add("PadBack", Constantes.MANAGER.get("HUD/JoystickPad.png", Texture.class));
        skin.add("PadKnob", Constantes.MANAGER.get("HUD/JoystickStick.png", Texture.class));

        Touchpad.TouchpadStyle estilo = new Touchpad.TouchpadStyle();
        estilo.background = skin.getDrawable("PadBack");
        estilo.knob = skin.getDrawable("PadKnob");

        Touchpad touchpad = new Touchpad(20, estilo);
        touchpad.setBounds(25, 50, 200, 200);

        touchpad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Touchpad pad = (Touchpad) actor;

                if (Math.abs(pad.getKnobPercentX()) > Constantes.TOUCHPAD_DEADZONE) {
                    jugador.girar(pad.getKnobPercentX());
                } else {
                    jugador.girar(0);
                }
                if (Math.abs(pad.getKnobPercentY()) > Constantes.TOUCHPAD_DEADZONE) {
                    jugador.acelerar(pad.getKnobPercentY());
                } else {
                    jugador.acelerar(0);
                }
            }
        });
        escenaHUD.addActor(touchpad);
    }

    private void crearBarraVida() {
        barraVida = new ProgressBar(Constantes.MANAGER.get("HUD/LifeBarBar.png", Texture.class), true);
        barraVida.setFrame(Constantes.MANAGER.get("HUD/LifeBarFrame.png", Texture.class));
        barraVida.setPosition(9 * Constantes.ANCHO_PANTALLA / 10 + 40, 2 * Constantes.ALTO_PANTALLA / 8);
        escenaHUD.addActor(barraVida);
    }

    private void crearBotonPausa() {
        Skin skin = new Skin();
        skin.add("Pausa", Constantes.MANAGER.get("PantallaJuego/Pausa.png", Texture.class));

        Button.ButtonStyle estilo = new Button.ButtonStyle();
        estilo.up = skin.getDrawable("Pausa");

        botonPausa = new Button(estilo);
        botonPausa.setPosition(11 * Constantes.ANCHO_PANTALLA / 12,
                9 * Constantes.ALTO_PANTALLA / 10);
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
        Skin skin = new Skin();
        skin.add("DisparoStandby", Constantes.MANAGER.get("HUD/BotonAStandby.png", Texture.class));
        skin.add("DisparoPresionado", Constantes.MANAGER.get("HUD/BotonAPresionado.png", Texture.class));
        Button.ButtonStyle estilo = new Button.ButtonStyle();
        estilo.down = skin.getDrawable("DisparoPresionado");
        estilo.up = skin.getDrawable("DisparoStandby");

        Button botonDisparo = new Button(estilo);
        botonDisparo.setPosition(13 * Constantes.ANCHO_PANTALLA / 16,
                1 * Constantes.ALTO_PANTALLA / 10);

        botonDisparo.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                jugadorDisparando = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                jugadorDisparando = false;
            }
        });
        escenaHUD.addActor(botonDisparo);
    }

    private void crearBotonEspecial() {
        Skin skin = new Skin();
        skin.add("EspecialStandby", Constantes.MANAGER.get("HUD/BotonBStandby.png", Texture.class));
        skin.add("EspecialPresionado", Constantes.MANAGER.get("HUD/BotonBPresionado.png", Texture.class));
        Button.ButtonStyle estilo = new Button.ButtonStyle();
        estilo.down = skin.getDrawable("EspecialPresionado");
        estilo.up = skin.getDrawable("EspecialStandby");

        Button botonEspecial = new Button(estilo);
        botonEspecial.setPosition(12 * Constantes.ANCHO_PANTALLA / 16,
                1 * Constantes.ALTO_PANTALLA / 10);

        botonEspecial.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                jugadorDisparando = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                jugadorDisparando = false;
            }
        });
        escenaHUD.addActor(botonEspecial);
    }

    //endregion

    //region metodos world
    private void crearWorld() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                if (!(contact.getFixtureA().getBody().getUserData() instanceof IPlayableEntity) ||
                        !(contact.getFixtureB().getBody().getUserData() instanceof IPlayableEntity)) {
                    return;
                }
                IPlayableEntity objetoA = (IPlayableEntity) contact.getFixtureA().getBody().getUserData();
                IPlayableEntity objetoB = (IPlayableEntity) contact.getFixtureB().getBody().getUserData();
                handleCollision(objetoA, objetoB);
                handleCollision(objetoB, objetoA);

                barraVida.setPorcentage(jugador.vida / jugador.vidaTotal);
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
            if (a instanceof NavesEspaciales) {
                animations.add(new AutoAnimation(Constantes.MANAGER.get("Animaciones/ExplosionNaveFrames.png", Texture.class), 0.15f, a.getX(), a.getY(), 100, 100, batch));
            }
            if (a instanceof NaveEnemiga) {
                enemigos.remove(a);
            }
            if (a instanceof NaveJugador) {
                isPaused = true;
                perdiste = true;
                Gdx.input.setInputProcessor(escenaPerdiste);
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

    private void crearBordes() {
        new Borde(world, -100, 0, 100, Constantes.ALTO_PANTALLA);
        new Borde(world, Constantes.ANCHO_PANTALLA + 100, 0, 100, Constantes.ALTO_PANTALLA);
        new Borde(world, -100, -100, Constantes.ANCHO_PANTALLA + 200, 100);
        new Borde(world, -100, Constantes.ALTO_PANTALLA + 100, Constantes.ANCHO_PANTALLA + 200, 100);
    }
    //endregion

    //region metodos jugador
    private void moverJugador(float delta) {
        jugador.mover(null, delta);
        if (jugadorDisparando) {
            jugador.disparar(TimeUtils.millis());
        }
    }
    //endregion

    //region metodos enemigos
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
            NaveEnemiga enemigo = new NaveEnemiga(Constantes.MANAGER.get("PantallaJuego/Enemigo" + (random.nextInt(3) + 1) + "Sprite.png", Texture.class), random.nextInt((int) Constantes.ANCHO_PANTALLA), Constantes.ALTO_PANTALLA + 50, world);
            enemigo.escalar(Constantes.ESCALA_NAVES);
            enemigos.add(enemigo);
        }
    }

    private void moverEnemigos(float delta) {
        Vector2 target = new Vector2(jugador.getX(), jugador.getY());
        for (NaveEnemiga enemigo : enemigos) {
            enemigo.mover(target, delta);
            enemigo.disparar(TimeUtils.millis());
        }
    }

    private void handleWaves() {
        if (spawnedEnemiesForThisWave == numberEnemiesForThisWave && enemigos.size() == 0) {
            if (waveNumber < numberOfWaves) {
                waveNumber++;
                timeSinceLastSpawn = 0;
                spawnedEnemiesForThisWave = 0;
                numberEnemiesForThisWave += extraPerWave;
                switchingWaves = true;
            } else {
                //TODO se acabo el juego (ganaste)
                isPaused = true;
                ganaste = true;
            }
        }
    }
    //endregion
}