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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Servio T on 10/02/2017.
 */

public class PantallaOpciones extends Pantalla {
    private final StarBlast menu;

    //Texturas
    private Texture texturaFondo;

    //SpriteBatch
    private SpriteBatch batch;

    //Escenas
    private Stage escenaOpciones;

    //Texto
    private Texto texto;
    private TextButton.TextButtonStyle textButtonStyle;

    //Sprite
    private GeneralSprite spriteSonido;
    private GeneralSprite spriteMusica;
    private GeneralSprite spriteAyuda;
    private GeneralSprite spriteCodigos;
    private GeneralSprite spriteReiniciar;
    private GeneralSprite spriteBack;


    public PantallaOpciones(StarBlast menu) {
        this.menu=menu;
    }

    @Override
    public void show() {
        cargarTexturas();
        crearObjetos();
    }

    private void crearObjetos() {
        batch = new SpriteBatch();
        escenaOpciones = new Stage(vista, batch);
        Image imgFondo = new Image(texturaFondo);
        escenaOpciones.addActor(imgFondo);
        texto = new Texto(Constantes.TEXTO_FUENTE);
        //crearBotonAtras();
        crearSprites();
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(new ProcesadorEntrada());
    }

    private void crearSprites(){
        spriteAyuda = new GeneralSprite("PantallaOpciones/BotonAyuda.png",3*Constantes.ANCHO_PANTALLA/4,
                Constantes.ALTO_PANTALLA/2-25);
        spriteMusica = new GeneralSprite("PantallaOpciones/BotonMusica.png",3*Constantes.ANCHO_PANTALLA/4,
                Constantes.ALTO_PANTALLA/2+80);
        spriteSonido = new GeneralSprite("PantallaOpciones/BotonSonido.png",3*Constantes.ANCHO_PANTALLA/4,
                2*Constantes.ALTO_PANTALLA/3+40);
        spriteCodigos = new GeneralSprite("PantallaOpciones/BotonCodigos.png",3*Constantes.ANCHO_PANTALLA/4,
                Constantes.ALTO_PANTALLA/3+10);
        spriteReiniciar = new GeneralSprite("PantallaOpciones/BotonReset.png",3*Constantes.ANCHO_PANTALLA/4,
                Constantes.ALTO_PANTALLA/6+20);
        spriteBack = new GeneralSprite("PantallaOpciones/back.png",12*Constantes.ANCHO_PANTALLA/13,
                Constantes.ALTO_PANTALLA/8);
    }

    private void crearBotonAtras() {
        textButtonStyle = texto.generarTexto(Color.RED,Color.GOLD,2);
        TextButton btnPlay = new TextButton("X", textButtonStyle);
        btnPlay.setPosition(12*Constantes.ANCHO_PANTALLA/13-btnPlay.getWidth()/2, Constantes.ALTO_PANTALLA/8-btnPlay.getHeight()/2);

        escenaOpciones.addActor(btnPlay);

        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Pantalla Creditos: ","Voy a pantalla menu");
                menu.setScreen(new PantallaMenu(menu));
            }
        });
    }

    private void cargarTexturas() {
        texturaFondo = new Texture("PantallaOpciones/PantallaOpciones.jpg");
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        escenaOpciones.draw();
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        spriteAyuda.draw(batch);
        spriteCodigos.draw(batch);
        spriteSonido.draw(batch);
        spriteReiniciar.draw(batch);
        spriteMusica.draw(batch);
        spriteBack.draw(batch);
        batch.end();
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

    private class ProcesadorEntrada implements InputProcessor {
        @Override
        public boolean keyDown(int keycode) {
            Gdx.app.log("Pantalla Creditos: ","Voy al Menu");
            menu.setScreen(new PantallaMenu(menu));
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
            if(spriteBack.isTouched(screenX, screenY, camara)){
                spriteBack.setTexture("PantallaOpciones/backYellow.png");
            }
            return true;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            if(spriteSonido.isTouched(screenX, screenY, camara)){
                spriteSonido.setTexture("PantallaOpciones/BotonNoSonido.png");
            } else if(spriteMusica.isTouched(screenX, screenY, camara)){
                spriteMusica.setTexture("PantallaOpciones/BotonNoMusica.png");
            } else if(spriteAyuda.isTouched(screenX, screenY, camara)){

            } else if(spriteCodigos.isTouched(screenX, screenY, camara)){

            } else if(spriteReiniciar.isTouched(screenX, screenY, camara)){

            } else if(spriteBack.isTouched(screenX, screenY, camara)){
                Gdx.app.log("Pantalla Creditos: ","Voy a pantalla menu");
                menu.setScreen(new PantallaMenu(menu));
            } else {
                spriteBack.setTexture("PantallaOpciones/back.png");
            }
            return true;
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

