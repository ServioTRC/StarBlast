package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;
import java.math.*;

/**
 * Created by Ian Neumann on 16/02/2017.
 */

public class NivelPrueba implements Screen{

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

    //Texto
    private Texto texto;
    private TextButton.TextButtonStyle textButtonStyle;

    //Sprites
    private GeneralSprite avatar;
    private ArrayList<NaveEnemiga> enemigos;

    private GeneralSprite controles;

    private Vector2 target;

    private OrthographicCamera camaraHUD;
    private Viewport vistaHUD;
    private Stage escenaHUD;

    private Touchpad touchpad;

    private Button botonPausa;
    private Button botonDisparo;

    private NaveJugador jugador;

    private World world;

    private float accumulator;

    ShapeRenderer shapeRenderer;

    private boolean isPaused = false;
    private StageOpciones escenaPausa;

    public NivelPrueba(StarBlast menu) {
        this.menu = menu;
    }

    @Override
    public void show() {
        crearCamara();
        cargarTexturas();
        crearObjetos();
    }

    private void crearObjetos() {
        target = new Vector2(Constantes.ANCHO_PANTALLA/2,Constantes.ALTO_PANTALLA/2);
        batch = new SpriteBatch();
        escenaJuego = new Stage(vista, batch);
        Image imgFondo = new Image(texturaFondo);
        texto = new Texto(Constantes.TEXTO_FUENTE);
        enemigos = new ArrayList<NaveEnemiga>();
        escenaJuego.addActor(imgFondo);

        crearWorld();
        crearSprites();
        crearHud();

        //Gdx.input.setInputProcessor(new Procesador());
        Gdx.input.setInputProcessor(escenaHUD);
        shapeRenderer = new ShapeRenderer();


        escenaPausa = new StageOpciones(vista,batch,menu){
            @Override
            public boolean keyDown(int keyCode) {
                if(keyCode == Input.Keys.BACK){
                    isPaused = false;
                    handlePause();
                    return true;
                }
                return super.keyDown(keyCode);
            }
        };
    }

    private void crearWorld(){
        world = new World(new Vector2(0, 0), true);
        accumulator = 0;
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                /*
                Gdx.app.log("Choque: ","chocaron");
                Body bodyA = contact.getFixtureA().getBody();
                Body bodyB = contact.getFixtureB().getBody();
                Vector2 positionA = bodyA.getPosition();
                Vector2 positionB = bodyB.getPosition();
                Vector2 fuerza = new Vector2((positionA.x-positionB.x)*1000,(positionA.y-positionB.y)*1000);
                bodyA.setLinearVelocity(fuerza);
                fuerza = new Vector2(-fuerza.x,-fuerza.y);
                bodyB.setLinearVelocity(fuerza);*/
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

    private void crearHud() {
        camaraHUD = new OrthographicCamera(Constantes.ANCHO_PANTALLA,Constantes.ALTO_PANTALLA);
        camaraHUD.position.set(Constantes.ANCHO_PANTALLA/2,Constantes.ALTO_PANTALLA/2,0);
        camaraHUD.update();
        vistaHUD = new StretchViewport(Constantes.ANCHO_PANTALLA,Constantes.ALTO_PANTALLA,camaraHUD);

        crearPad();
        crearBotonPausa();
        crearBotonDisparo();
    }

    private void crearBotonDisparo() {
        float escala = 0.3f;

        Skin skin = new Skin();
        skin.add("DisparoStandby",new Texture("HUD/BotonAStandby.png"));
        skin.add("DisparoPresionado",new Texture("HUD/BotonAPresionado.png"));
        Button.ButtonStyle estilo = new Button.ButtonStyle();
        estilo.down = skin.getDrawable("DisparoPresionado");
        estilo.up = skin.getDrawable("DisparoStandby");

        botonDisparo = new Button(estilo);
        botonDisparo.scaleBy(escala);
        botonDisparo.setPosition(13*Constantes.ANCHO_PANTALLA/16,
                1*Constantes.ALTO_PANTALLA/10);

        botonDisparo.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Button boton = (Button) actor;
                if(boton.isPressed()){
                    jugador.disparar(TimeUtils.millisToNanos(TimeUtils.nanoTime()));
                }
            }
        });
        escenaHUD.addActor(botonDisparo);
    }

    private void crearBotonPausa() {
        float escala = 0.3f;

        Skin skin = new Skin();
        skin.add("Pausa",new Texture("PantallaJuego/Pausa.png"));

        Button.ButtonStyle estilo = new Button.ButtonStyle();
        estilo.down = skin.getDrawable("Pausa");
        estilo.up = skin.getDrawable("Pausa");

        botonPausa = new Button(estilo);
        botonPausa.scaleBy(escala);
        botonPausa.setPosition(11*Constantes.ANCHO_PANTALLA/12,
                9*Constantes.ALTO_PANTALLA/10);
        botonPausa.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Button boton = (Button) actor;
                if(boton.isPressed()){
                    isPaused = !isPaused;
                    handlePause();
                }
            }
        });

        escenaHUD.addActor(botonPausa);
    }

    private void handlePause() {
        if(isPaused){
            escenaPausa.addActor(botonPausa);
            Gdx.input.setInputProcessor(escenaPausa);
        }else{
            escenaHUD.addActor(botonPausa);
            Gdx.input.setInputProcessor(escenaHUD);
        }
    }

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

                if (Math.abs(pad.getKnobPercentX())> Constantes.TOUCHPAD_DEADZONE) {
                    jugador.girar(pad.getKnobPercentX());
                }
                else {
                    jugador.girar(0);
                }
                if(Math.abs(pad.getKnobPercentY()) > Constantes.TOUCHPAD_DEADZONE){
                    jugador.acelerar(pad.getKnobPercentY());
                }
                else{
                    jugador.acelerar(0);
                }
            }
        });

        escenaHUD = new Stage(vistaHUD);
        escenaHUD.addActor(touchpad);
    }

    private void crearSprites() {
        float escala = 0.3f;

        crearEnemigos();

        jugador = new NaveJugador("PantallaJuego/AvatarSprite.png",Constantes.ANCHO_PANTALLA/2,Constantes.ANCHO_PANTALLA/5,world);
        jugador.escalar(Constantes.ESCALA_NAVES);

        /*enemigo1 = new GeneralSprite("PantallaJuego/enemigo1.png",Constantes.ANCHO_PANTALLA/4,
                2*Constantes.ALTO_PANTALLA/3);
        enemigo1.rotar(40);
        enemigo1.escalar(escala);
        enemigo2 = new GeneralSprite("PantallaJuego/enemigo2.png",3*Constantes.ANCHO_PANTALLA/4,
                2*Constantes.ALTO_PANTALLA/3);
        enemigo2.rotar(330);
        enemigo2.escalar(escala);*/

        controles = new GeneralSprite("PantallaJuego/Controles.png",Constantes.ANCHO_PANTALLA/2,
                Constantes.ALTO_PANTALLA/2);
        controles.escalar(escala);
        controles.setAlpha(1);
    }

    private void crearEnemigos() {
        NaveEnemiga enemigo;
        Random r = new Random();
        for(int i = 0; i< ENEMIGOS_INICIALES;i++){
            //enemigo = new NaveEnemiga("PantallaJuego/Enemigo"+(r.nextBoolean()?"1":"2")+"Sprite.png",r.nextInt((int)Constantes.ANCHO_PANTALLA),Constantes.ALTO_PANTALLA,world);
            enemigo = new NaveEnemiga("PantallaJuego/Enemigo1.png",3*Constantes.ANCHO_PANTALLA/4,Constantes.ALTO_PANTALLA/3,world);
            enemigo.escalar(Constantes.ESCALA_NAVES);
            enemigos.add(enemigo);
        }
    }

    private void cargarTexturas() {
        texturaFondo = new Texture("PantallaJuego/FondoSimple.jpg");
    }

    private void crearCamara() {
        camara = new OrthographicCamera(Constantes.ANCHO_PANTALLA, Constantes.ALTO_PANTALLA);
        camara.position.set(Constantes.ANCHO_PANTALLA / 2, Constantes.ALTO_PANTALLA / 2, 0);
        camara.update();
        vista = new StretchViewport(Constantes.ANCHO_PANTALLA, Constantes.ALTO_PANTALLA, camara);
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        procesarJuego(delta);
        dibujarElementos();
        debugearElementos();
    }

    private void debugearElementos() {
        if(enemigos.size() == 0) {
            return;
        }
        camara.update();


        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setProjectionMatrix(camara.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);



        shapeRenderer.setColor(new Color(0,1,0,0.5f));
        for (NaveEnemiga enemigo: enemigos) {
            shapeRenderer.circle(enemigo.getX(),enemigo.getY(),Constantes.toScreenSize(enemigo.getShape().getRadius()));
        }
        shapeRenderer.circle(jugador.getX(),jugador.getY(),Constantes.toScreenSize(jugador.getShape().getRadius()));



        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.line(enemigos.get(0).getX(),enemigos.get(0).getY(),target.x,target.y);

        shapeRenderer.end();

    }

    private void update(float dt){
        accumulator+=dt;
        while(accumulator>1/120f){
            world.step(1/120f,8,3);
            accumulator-=1/120f;
        }
    }

    private void procesarJuego(float delta) {
        if(!isPaused) {
            update(delta);
            moverEnemigos(delta);
            moverJugador(delta);
        }
    }


    private void moverJugador(float delta) {
        jugador.mover(target,delta);
    }

    private void moverEnemigos(float delta) {
        target = new Vector2(jugador.getX(),jugador.getY());
        //target = new Vector2(Constantes.ANCHO_PANTALLA/2,Constantes.ALTO_PANTALLA/2);
        for(NaveEnemiga enemigo:enemigos){
            enemigo.mover(target,delta);
        }
    }

    private void dibujarElementos() {
        escenaJuego.draw();
        batch.begin();
        jugador.draw(batch);
        for (NaveEnemiga enemigo:enemigos){
            enemigo.draw(batch);
        }
        //botonPausa.draw(batch);
        //comentado para probar el touch pad
        //controles.draw(batch);

        batch.end();

        batch.setProjectionMatrix(camaraHUD.combined);
        escenaHUD.draw();
        if(isPaused){
            escenaPausa.draw();
        }
    }

    private void borrarPantalla() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
        Constantes.ASSET_GENERAL.dispose();
    }


}

