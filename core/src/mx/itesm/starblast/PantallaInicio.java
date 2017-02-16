package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Servio T on 05/02/2017.
 */

public class PantallaInicio implements Screen {

    private final StarBlast menu;

    //Camara, vista
    private OrthographicCamera camara;
    private Viewport vista;

    //Texturas
    private Texture texturaFondo;
    private Texture texturaBtn;

    //SpriteBatch
    private SpriteBatch batch;

    //Escenas
    private Stage escenaInicio;

    //Texto
    private Texto tap;

    //Sprite
    private GeneralSprite sprite;

    private float cambioAlpha = (float)1/120;
    private float alpha = (float) 1;
    private int cuenta = 0;

    public PantallaInicio(StarBlast menu) {
        this.menu=menu;
    }

    @Override
    public void show() {
        crearCamara();
        cargarTexturas();
        crearObjetos();
    }

    private void crearObjetos() {
        batch = new SpriteBatch();
        escenaInicio = new Stage(vista, batch);
        Image imgFondo = new Image(texturaFondo);
        escenaInicio.addActor(imgFondo);
        tap = new Texto("Textos/Arcade50.fnt");
        sprite = new GeneralSprite("PantallaInicio/TAP.png", Constantes.ANCHO_PANTALLA/2,1* Constantes.ALTO_PANTALLA/4);
        Gdx.input.setInputProcessor(new ProcesadorEntrada());
        Gdx.input.setCatchBackKey(true);
    }

    private void cargarTexturas() {
        texturaFondo = new Texture("PantallaInicio/FondoInicio.jpg");
        texturaBtn = new Texture("PantallaInicio/TAP.png");
    }

    private void crearCamara() {
        camara = new OrthographicCamera(Constantes.ANCHO_PANTALLA, Constantes.ALTO_PANTALLA);
        camara.position.set(Constantes.ANCHO_PANTALLA/2, Constantes.ALTO_PANTALLA/2,0);
        camara.update();
        vista = new StretchViewport(Constantes.ANCHO_PANTALLA, Constantes.ALTO_PANTALLA, camara);
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        escenaInicio.draw();
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        sprite.draw(batch);
        sprite.setAlpha(alpha);
        if(cuenta >= 120){
            cambioAlpha *= -1;
            cuenta = 0;
        }
        alpha -= cambioAlpha;
        cuenta++;
        batch.end();
    }

    private void borrarPantalla() {
        Gdx.gl.glClearColor(0,0,0,1);
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

    }

    //Una clase para capturar los eventos del touch (teclado y mouse también)
    class ProcesadorEntrada implements InputProcessor {

        @Override
        public boolean keyDown(int keycode) {
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
            //Revisar si hace tap para reiniciar el juego
            Gdx.app.log("Pantalla de Inicio: ","Voy a PantallaMenu");
            menu.setScreen(new PantallaMenu(menu));
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