package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Servio T on 15/02/2017.
 */

public class PantallaJuego implements Screen {

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
    private GeneralSprite enemigo1;
    private GeneralSprite enemigo2;
    private GeneralSprite botonPausa;
    private GeneralSprite controles;

    public PantallaJuego(StarBlast menu) {
        this.menu = menu;
    }

    @Override
    public void show() {
        crearCamara();
        cargarTexturas();
        crearObjetos();
    }

    private void crearObjetos() {
        batch = new SpriteBatch();
        escenaJuego = new Stage(vista, batch);
        Image imgFondo = new Image(texturaFondo);
        texto = new Texto("Textos/Arcade50.fnt");
        escenaJuego.addActor(imgFondo);
        crearSprites();
        Gdx.input.setInputProcessor(new Procesador());
    }

    private void crearSprites() {
        float escala = 1.5f;
        avatar = new GeneralSprite("PantallaJuego/avatar.png",Constantes.ANCHO_PANTALLA/2,
                Constantes.ALTO_PANTALLA/3);
        avatar.rotar(20);
        avatar.escalar(escala);
        enemigo1 = new GeneralSprite("PantallaJuego/enemigo1.png",Constantes.ANCHO_PANTALLA/4,
                2*Constantes.ALTO_PANTALLA/3);
        enemigo1.rotar(40);
        enemigo1.escalar(escala);
        enemigo2 = new GeneralSprite("PantallaJuego/enemigo2.png",3*Constantes.ANCHO_PANTALLA/4,
                2*Constantes.ALTO_PANTALLA/3);
        enemigo2.rotar(330);
        enemigo2.escalar(escala);
        botonPausa = new GeneralSprite("PantallaJuego/Pausa.png",11*Constantes.ANCHO_PANTALLA/12,
                9*Constantes.ALTO_PANTALLA/10);
        botonPausa.escalar(escala);
        controles = new GeneralSprite("PantallaJuego/Controles.png",Constantes.ANCHO_PANTALLA/2,
                Constantes.ALTO_PANTALLA/2);
        controles.escalar(escala);
        controles.setAlpha(0.5f);
    }

    private void cargarTexturas() {
        texturaFondo = new Texture("PantallaJuego/fondoSimple.jpg");
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
        escenaJuego.draw();
        batch.begin();
        avatar.draw(batch);
        enemigo1.draw(batch);
        enemigo2.draw(batch);
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
            if(botonPausa.isTouched(screenX, screenY, camara, vista)) {
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
            return false;
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