package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
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
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Random;

class NivelPrueba implements Screen, IPausable {

    private static final int ENEMIGOS_INICIALES = 1;
    private final StarBlast menu;

    //Camara, vista
    private OrthographicCamera camara;
    private Viewport vista;

    //Texturas
    private Texture texturaFondo;

    //SpriteBatch
    private SpriteBatch batch;

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
    private HealthBar barraVida;

    private NaveJugador jugador;

    private World world;

    private float accumulator;

    private ShapeRenderer shapeRenderer;

    private boolean isPaused = false;
    private StageOpciones escenaPausa;

    private boolean disparando = false;

    private Array<Body> toRemove;

    private ArrayList<Body> bordes = new ArrayList<Body>();

    NivelPrueba(StarBlast menu) {
        this.menu = menu;
    }

    @Override
    public void show() {
        crearCamara();
        cargarTexturas();
        crearObjetos();
    }

    //region metodos show

    private void crearCamara() {
        camara = new OrthographicCamera(Constantes.ANCHO_PANTALLA, Constantes.ALTO_PANTALLA);
        camara.position.set(Constantes.ANCHO_PANTALLA / 2, Constantes.ALTO_PANTALLA / 2, 0);
        camara.update();
        vista = new StretchViewport(Constantes.ANCHO_PANTALLA, Constantes.ALTO_PANTALLA, camara);
    }

    private void cargarTexturas() {
        texturaFondo = new Texture("PantallaJuego/FondoSimple.jpg");
    }

    private void crearObjetos() {
        target = new Vector2(Constantes.ANCHO_PANTALLA / 2, Constantes.ALTO_PANTALLA / 2);
        batch = new SpriteBatch();
        escenaJuego = new Stage(vista, batch);
        Image imgFondo = new Image(texturaFondo);
        enemigos = new ArrayList<NaveEnemiga>();
        escenaJuego.addActor(imgFondo);

        crearWorld();
        crearBordes();
        crearSprites();
        crearHud();

        //Gdx.input.setInputProcessor(new Procesador());
        Gdx.input.setInputProcessor(escenaHUD);
        shapeRenderer = new ShapeRenderer();


        escenaPausa = new StageOpciones(vista, batch, menu, this);
    }

    //region metodos crearObjetos

    private void crearWorld() {
        world = new World(Vector2.Zero, true);
        accumulator = 0;
        toRemove = new Array<Body>();
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Object objetoA = contact.getFixtureA().getBody().getUserData();
                Object objetoB = contact.getFixtureB().getBody().getUserData();
                int damageA,damageB;
                damageA = obtenerDamage(objetoA);
                damageB = obtenerDamage(objetoB);
                if(objetoA instanceof Bullet){
                    ((Bullet)objetoA).damage = 0;
                    toRemove.add(contact.getFixtureA().getBody());
                }
                if(objetoB instanceof Bullet){
                    ((Bullet)objetoB).damage = 0;
                    toRemove.add(contact.getFixtureB().getBody());
                }
                if(objetoA instanceof NavesEspaciales){
                    ((NavesEspaciales)objetoA).doDamage(damageB);
                }
                if(objetoB instanceof NavesEspaciales){
                    ((NavesEspaciales)objetoB).doDamage(damageA);
                }
                barraVida.setHealthPorcentage(jugador.vida/100);
                Gdx.app.log("Vida",""+jugador.vida);
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

    //region metodos crearWorld
    private int obtenerDamage(Object objeto){
        if(objeto instanceof Bullet){
            Bullet balaA = (Bullet)objeto;
            return balaA.damage;
        }
        if(objeto instanceof NavesEspaciales){
            NavesEspaciales naveA = (NavesEspaciales)objeto;
            return naveA.damage;
        }
        return 0;
    }
    //endregion

    private void crearSprites() {
        crearEnemigos();
        jugador = new NaveJugador("PantallaJuego/AvatarSprite.png", Constantes.ANCHO_PANTALLA / 2, Constantes.ANCHO_PANTALLA / 5, world);
        jugador.escalar(Constantes.ESCALA_NAVES);
    }

    //region metodos crearSprites
    private void crearEnemigos() {
        NaveEnemiga enemigo;
        Random r = new Random();
        for (int i = 0; i < ENEMIGOS_INICIALES; i++) {
            enemigo = new NaveEnemiga("PantallaJuego/Enemigo" + (r.nextBoolean() ? "1" : "2") + "Sprite.png", r.nextInt((int) Constantes.ANCHO_PANTALLA), Constantes.ALTO_PANTALLA, world);
//            enemigo = new NaveEnemiga("PantallaJuego/Enemigo1.png",3*Constantes.ANCHO_PANTALLA/4,Constantes.ALTO_PANTALLA/3,world);
            enemigo.escalar(Constantes.ESCALA_NAVES);
            enemigos.add(enemigo);
        }
    }
    //endregion

    private void crearHud() {
        camaraHUD = new OrthographicCamera(Constantes.ANCHO_PANTALLA, Constantes.ALTO_PANTALLA);
        camaraHUD.position.set(Constantes.ANCHO_PANTALLA / 2, Constantes.ALTO_PANTALLA / 2, 0);
        camaraHUD.update();
        vistaHUD = new StretchViewport(Constantes.ANCHO_PANTALLA, Constantes.ALTO_PANTALLA, camaraHUD);

        crearPad();
        crearBotonPausa();
        crearBotonDisparo();
        crearBarraVida();
    }

    //region metodos crearHud
    private void crearPad() {

        Skin skin = new Skin();
        skin.add("PadBack", new Texture("HUD/JoystickPad.png"));
        skin.add("PadKnob", new Texture("HUD/JoystickStick.png"));

        Touchpad.TouchpadStyle estilo = new Touchpad.TouchpadStyle();
        estilo.background = skin.getDrawable("PadBack");
        estilo.knob = skin.getDrawable("PadKnob");

        touchpad = new Touchpad(20, estilo);
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

        escenaHUD = new Stage(vistaHUD);
        escenaHUD.addActor(touchpad);
    }

    private void crearBotonPausa() {
        float escala = 0.3f;

        Skin skin = new Skin();
        skin.add("Pausa", new Texture("PantallaJuego/Pausa.png"));

        Button.ButtonStyle estilo = new Button.ButtonStyle();
        estilo.down = skin.getDrawable("Pausa");
        estilo.up = skin.getDrawable("Pausa");

        botonPausa = new Button(estilo);
        botonPausa.scaleBy(escala);
        botonPausa.setPosition(11 * Constantes.ANCHO_PANTALLA / 12,
                9 * Constantes.ALTO_PANTALLA / 10);
        botonPausa.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Button boton = (Button) actor;
                if (boton.isPressed()) {
                    if (!isPaused) {
                        pause();
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
        skin.add("DisparoStandby", new Texture("HUD/BotonAStandby.png"));
        skin.add("DisparoPresionado", new Texture("HUD/BotonAPresionado.png"));
        Button.ButtonStyle estilo = new Button.ButtonStyle();
        estilo.down = skin.getDrawable("DisparoPresionado");
        estilo.up = skin.getDrawable("DisparoStandby");

        botonDisparo = new Button(estilo);
        botonDisparo.scaleBy(escala);
        botonDisparo.setPosition(13 * Constantes.ANCHO_PANTALLA / 16,
                1 * Constantes.ALTO_PANTALLA / 10);

        botonDisparo.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                disparando = true;
                jugador.disparar(TimeUtils.nanosToMillis(TimeUtils.nanoTime()),false);
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
        barraVida = new HealthBar(new Texture("HUD/LifeBarBar.png"));
        barraVida.setFrame(new Texture("HUD/LifeBarFrame.png"));
        barraVida.setPosition(9 * Constantes.ANCHO_PANTALLA / 10, 3 * Constantes.ALTO_PANTALLA / 8);
        escenaHUD.addActor(barraVida);
    }
    //endregion

    //endregion

    //endregion


    //region metodos pausa
    @Override
    public void pause() {
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

    @Override
    public void render(float delta) {
        borrarPantalla();
        procesarJuego(delta);
        dibujarElementos();
        debugearElementos();
    }
    //endregion


    //region metodos render
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
            for(Fixture fix: b.getFixtureList()){
                b.destroyFixture(fix);
            }
            world.destroyBody(b);
        }
        toRemove.clear();
    }

    private void moverEnemigos(float delta) {
        target = new Vector2(jugador.getX(), jugador.getY());
        //target = new Vector2(Constantes.ANCHO_PANTALLA/2,Constantes.ALTO_PANTALLA/2);
        for(NaveEnemiga enemigo:enemigos){
            enemigo.mover(target,delta);
            enemigo.disparar(TimeUtils.nanosToMillis(TimeUtils.nanoTime()),true);
        }
    }

    private void moverJugador(float delta) {
        jugador.mover(target,delta);
        if(disparando){
            jugador.disparar(TimeUtils.nanosToMillis(TimeUtils.nanoTime()),false);
        }
    }
    //endregion

    private void dibujarElementos() {
        escenaJuego.draw();
        batch.begin();
        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);
        Object object;
        for(Body b:bodies){
            object = b.getUserData();
            if(object instanceof NavesEspaciales){
                ((NavesEspaciales)object).draw(batch);
            }
            if(object instanceof Bullet){
                ((Bullet)object).draw(batch);
            }
        }
        /*jugador.draw(batch);
        for (NaveEnemiga enemigo : enemigos) {
            enemigo.draw(batch);
        }*/
        //botonPausa.draw(batch);
        //comentado para probar el touch pad
        //controles.draw(batch);

        batch.end();

        batch.setProjectionMatrix(camaraHUD.combined);
        escenaHUD.draw();
        if (isPaused) {
            escenaPausa.draw();
        }
    }

    private void debugearElementos() {
        if (enemigos.size() == 0) {
            return;
        }
        camara.update();


        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setProjectionMatrix(camara.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);


        shapeRenderer.setColor(new Color(0, 1, 0, 0.5f));
        for (NaveEnemiga enemigo : enemigos) {
            shapeRenderer.circle(enemigo.getX(), enemigo.getY(), Constantes.toScreenSize(enemigo.getShape().getRadius()));
        }
        shapeRenderer.circle(jugador.getX(), jugador.getY(), Constantes.toScreenSize(jugador.getShape().getRadius()));

        shapeRenderer.setColor(new Color(1, 0, 0, 0.7f));
        /*for (Bullet bala : balas) {
            shapeRenderer.circle(bala.getX(), bala.getY(), Constantes.toScreenSize(bala.getShape().getRadius()));
        }*/


        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(new Color(0, 1, 0, 0.5f));
        shapeRenderer.line(enemigos.get(0).getX(), enemigos.get(0).getY(), target.x, target.y);

        shapeRenderer.end();

    }

    //endregion

    private void crearBordes() {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(Constantes.toWorldSize(-120),0);
        bordes.add(world.createBody(bdef));
        crearFixtureBordes(bordes.get(0),100,Constantes.ALTO_PANTALLA);
        bdef.position.set(Constantes.toWorldSize(Constantes.ANCHO_PANTALLA+120),0);
        bordes.add(world.createBody(bdef));
        crearFixtureBordes(bordes.get(1),100,Constantes.ALTO_PANTALLA);
        bdef.position.set(Constantes.toWorldSize(-120),Constantes.toWorldSize(-120));
        bordes.add(world.createBody(bdef));
        crearFixtureBordes(bordes.get(2),Constantes.ANCHO_PANTALLA+200,100);
        bdef.position.set(Constantes.toWorldSize(-120),
                Constantes.toWorldSize(Constantes.ALTO_PANTALLA+120));
        bordes.add(world.createBody(bdef));
        crearFixtureBordes(bordes.get(3),Constantes.ANCHO_PANTALLA+200,100);
    }

    private void crearFixtureBordes(Body body, float x, float y){
        for (Fixture fix : body.getFixtureList()) {
            body.destroyFixture(fix);
        }
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constantes.toWorldSize(x),Constantes.toWorldSize(y));
        FixtureDef fix = new FixtureDef();
        fix.shape = shape;
        fix.filter.categoryBits = Constantes.CATEGORY_BORDERS;
        fix.filter.maskBits = Constantes.MASK_BORDERS;
        body.createFixture(fix);
    }

    @Override
    public void resize(int width, int height) {
        vista.update(width, height);
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        Constantes.ASSET_GENERAL.dispose();
    }


}