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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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

public class  NivelPrueba implements Screen{

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
    private GeneralSprite botonPausa;
    private GeneralSprite controles;

    private Vector2 target;

    private OrthographicCamera camaraHUD;
    private Viewport vistaHUD;
    private Stage escenaHUD;

    private Touchpad touchpad;

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
        texto = new Texto("Textos/Arcade50.fnt");
        enemigos = new ArrayList<NaveEnemiga>();
        escenaJuego.addActor(imgFondo);
        crearSprites();
        Gdx.input.setInputProcessor(new mx.itesm.starblast.NivelPrueba.Procesador());

        //crearPad();

    }

    private void crearPad() {

        camaraHUD = new OrthographicCamera(Constantes.ANCHO_PANTALLA,Constantes.ALTO_PANTALLA);
        camaraHUD.position.set(Constantes.ANCHO_PANTALLA/2,Constantes.ALTO_PANTALLA/2,0);
        camaraHUD.update();
        vistaHUD = new StretchViewport(Constantes.ANCHO_PANTALLA,Constantes.ALTO_PANTALLA,camaraHUD);

        Skin skin = new Skin();
        skin.add("padBack", new Texture("padBack.png"));
        skin.add("padKnob", new Texture("padKnob.png"));

        Touchpad.TouchpadStyle estilo = new Touchpad.TouchpadStyle();
        estilo.background = skin.getDrawable("padBack");
        estilo.knob = skin.getDrawable("padKnob");

        touchpad = new Touchpad(20, estilo);
        touchpad.setBounds(0, 0, 200, 200);

        touchpad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Touchpad pad = (Touchpad) actor;
                /*if()
                if (pad.getKnobPercentX()>0.20) {
                    mario.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA);
                } else if (pad.getKnobPercentX()<-0.20){
                    mario.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_IZQUIERDA);
                } else {
                    mario.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
                }
                if(pad.getKnobPercentY() > 0.20){
                    mario.saltar();
                }*/
            }
        });

        escenaHUD = new Stage(vistaHUD);
        escenaHUD.addActor(touchpad);
    }

    private void crearSprites() {
        float escala = 1.5f;
        avatar = new GeneralSprite("PantallaJuego/avatar.png",Constantes.ANCHO_PANTALLA/2,
                Constantes.ALTO_PANTALLA/5);
        avatar.rotar(90);
        avatar.escalar(Constantes.ESCALA_NAVES);

        crearEnemigos();
        /*enemigo1 = new GeneralSprite("PantallaJuego/enemigo1.png",Constantes.ANCHO_PANTALLA/4,
                2*Constantes.ALTO_PANTALLA/3);
        enemigo1.rotar(40);
        enemigo1.escalar(escala);
        enemigo2 = new GeneralSprite("PantallaJuego/enemigo2.png",3*Constantes.ANCHO_PANTALLA/4,
                2*Constantes.ALTO_PANTALLA/3);
        enemigo2.rotar(330);
        enemigo2.escalar(escala);*/
        botonPausa = new GeneralSprite("PantallaJuego/Pausa.png",11*Constantes.ANCHO_PANTALLA/12,
                9*Constantes.ALTO_PANTALLA/10);
        botonPausa.escalar(escala);
        controles = new GeneralSprite("PantallaJuego/Controles.png",Constantes.ANCHO_PANTALLA/2,
                Constantes.ALTO_PANTALLA/2);
        controles.escalar(escala);
        controles.setAlpha(0.5f);
    }

    private void crearEnemigos() {
        NaveEnemiga enemigo;
        Random r = new Random();
        for(int i = 0; i< ENEMIGOS_INICIALES;i++){
            //enemigo = new NaveEnemiga("PantallaJuego/enemigo"+(r.nextBoolean()?"1.png":"2.png"),r.nextInt((int)Constantes.ANCHO_PANTALLA),Constantes.ALTO_PANTALLA);
            enemigo = new NaveEnemiga("PantallaJuego/enemigo1.png",3*Constantes.ANCHO_PANTALLA/4,Constantes.ALTO_PANTALLA/3);
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
    }

    private void moverEnemigos(float delta) {
        for(NaveEnemiga enemigo:enemigos){
            //enemigo.mover(new Vector2((int)avatar.getSprite().getX(),(int)avatar.getSprite().getY()),delta);
            //enemigo.mover(new Vector2((int)Constantes.ANCHO_PANTALLA/2,(int)Constantes.ALTO_PANTALLA/2),delta);
            enemigo.mover(target,delta);
            boolean bool = true;
        }
    }

    private void dibujarElementos() {
        escenaJuego.draw();
        batch.begin();
        avatar.draw(batch);
        for (NaveEnemiga enemigo:enemigos){
            enemigo.draw(batch);
        }
        botonPausa.draw(batch);
        controles.draw(batch);
        batch.end();
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
            return true;
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
            if(botonPausa.isTouched(screenX, screenY, camara)) {
                Gdx.app.log("Pantalla Juego: ","Voy a Opciones");
                menu.setScreen(new PantallaOpcionesTemporal(menu));
            }
            return true;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            target = new Vector2(screenX,Constantes.ALTO_PANTALLA-screenY);
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

