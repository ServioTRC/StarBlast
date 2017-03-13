package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Array;
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
    private ArrayList<GeneralSprite> proyectiles;
    private GeneralSprite enemigo1;
    private GeneralSprite enemigo2;
    private GeneralSprite controles;

    private Vector2 target;

    private OrthographicCamera camaraHUD;
    private Viewport vistaHUD;
    private Stage escenaHUD;

    private Touchpad touchpad;

    private Button botonPausa;

    private NaveJugador jugador;


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

        crearSprites();
        crearHud();

        //Gdx.input.setInputProcessor(new Procesador());
        Gdx.input.setInputProcessor(escenaHUD);
    }

    private void crearHud() {
        camaraHUD = new OrthographicCamera(Constantes.ANCHO_PANTALLA,Constantes.ALTO_PANTALLA);
        camaraHUD.position.set(Constantes.ANCHO_PANTALLA/2,Constantes.ALTO_PANTALLA/2,0);
        camaraHUD.update();
        vistaHUD = new StretchViewport(Constantes.ANCHO_PANTALLA,Constantes.ALTO_PANTALLA,camaraHUD);

        crearPad();
        crearBotonPausa();
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
                    Gdx.app.log("Nivel Prueba:", "Voy a pantalla opciones");
                    menu.setScreen(new PantallaOpciones(menu));
                }
            }
        });

        escenaHUD.addActor(botonPausa);
    }

    private void crearPad() {



        Skin skin = new Skin();
        skin.add("PadBack", new Texture("PadBack.png"));
        skin.add("PadKnob", new Texture("PadKnob.png"));

        Touchpad.TouchpadStyle estilo = new Touchpad.TouchpadStyle();
        estilo.background = skin.getDrawable("PadBack");
        estilo.knob = skin.getDrawable("PadKnob");

        touchpad = new Touchpad(20, estilo);
        touchpad.setBounds(0, 0, 200, 200);

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

        jugador = new NaveJugador("PantallaJuego/Avatar.png",Constantes.ANCHO_PANTALLA/2,Constantes.ANCHO_PANTALLA/5);
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
            //enemigo = new NaveEnemiga("PantallaJuego/enemigo"+(r.nextBoolean()?"1.png":"2.png"),r.nextInt((int)Constantes.ANCHO_PANTALLA),Constantes.ALTO_PANTALLA);
            enemigo = new NaveEnemiga("PantallaJuego/Enemigo1.png",3*Constantes.ANCHO_PANTALLA/4,Constantes.ALTO_PANTALLA/3);
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
    }

    private void procesarJuego(float delta) {

        moverEnemigos(delta);
        moverJugador(delta);
    }

    private void moverJugador(float delta) {
        jugador.mover(target,delta);
    }

    private void moverEnemigos(float delta) {
        target = new Vector2(jugador.getX(),jugador.getY());
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

    //Procesar entrada
    class Procesador implements InputProcessor {

        private Vector3 v = new Vector3();

        @Override
        public boolean keyDown(int keycode) {
            if (keycode == Input.Keys.BACK) {
                // DEBUG
                Gdx.app.log("Pantalla Juego: ", "Voy al Menu");
                menu.setScreen(new PantallaMenu(menu));
                return true;
            }
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {

            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            //return false;
            v.set(screenX,screenY,0);
            camara.unproject(v);
            target = new Vector2(v.x,v.y);
            return true;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }
    }


}

