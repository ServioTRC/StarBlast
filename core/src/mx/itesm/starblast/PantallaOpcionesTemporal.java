package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
 * Created by Servio T on 16/02/2017.
 */

public class PantallaOpcionesTemporal extends Pantalla {
    private final StarBlast menu;

    //Texturas
    private Texture texturaFondo;

    //SpriteBatch
    private SpriteBatch batch;

    //Escenas
    private Stage escenaOpciones2;

    //Texto
    private Texto texto;
    private TextButton.TextButtonStyle textButtonStyle;

    //Sprite
    private GeneralSprite spriteSonido;
    private GeneralSprite spriteAyuda;
    private GeneralSprite spriteCodigos;

    public PantallaOpcionesTemporal(StarBlast menu) {
        this.menu = menu;
    }

    @Override
    public void show() {
        cargarTexturas();
        crearObjetos();
    }

    private void crearObjetos() {
        batch = new SpriteBatch();
        escenaOpciones2 = new Stage(vista, batch) {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    // DEBUG
                    Gdx.app.log("Pantalla Opciones: ", "Voy al Juego");
                    menu.setScreen(new PantallaJuego(menu));
                    return true;
                }
                return super.keyDown(keycode);
            }
        };
        Image imgFondo = new Image(texturaFondo);
        escenaOpciones2.addActor(imgFondo);
        texto = new Texto("Textos/ArcadeFont.fnt");
        crearBotonMenu();
        crearBotonJuego();
        crearSprites();
        Gdx.input.setInputProcessor(escenaOpciones2);
    }

    /*private void crearTextos(){
        textButtonStyle = texto.generarTexto(Color.BLUE,Color.BLUE,1);
        TextButton btnPlay = new TextButton("EFECTOS DE SONIDO", textButtonStyle);
        btnPlay.setPosition(Constantes.ANCHO_PANTALLA/3-btnPlay.getWidth()/2, 3*Constantes.ALTO_PANTALLA/4-btnPlay.getHeight()/2);
        escenaOpciones.addActor(btnPlay);
        btnPlay = new TextButton("AYUDA Y CONSEJOS", textButtonStyle);
        btnPlay.setPosition(Constantes.ANCHO_PANTALLA/3-btnPlay.getWidth()/2, Constantes.ALTO_PANTALLA/2-btnPlay.getHeight()/2);
        escenaOpciones.addActor(btnPlay);
        btnPlay = new TextButton("INGRESAR CODIGO", textButtonStyle);
        btnPlay.setPosition(Constantes.ANCHO_PANTALLA/3-btnPlay.getWidth()/2, Constantes.ALTO_PANTALLA/4-btnPlay.getHeight()/2);
        escenaOpciones.addActor(btnPlay);
    }*/

    private void crearSprites() {
        spriteAyuda = new GeneralSprite("PantallaOpciones/BotonAyuda.png", 3 * Constantes.ANCHO_PANTALLA / 4,
                3 * Constantes.ALTO_PANTALLA / 4-50);
        spriteSonido = new GeneralSprite("PantallaOpciones/BotonSonido.png", 3 * Constantes.ANCHO_PANTALLA / 4,
                Constantes.ALTO_PANTALLA / 2);
        spriteCodigos = new GeneralSprite("PantallaOpciones/BotonCodigos.png", 3 * Constantes.ANCHO_PANTALLA / 4,
                1 * Constantes.ALTO_PANTALLA / 4+100);
    }

    private void crearBotonMenu() {
        textButtonStyle = texto.generarTexto(Color.ORANGE, Color.GOLD, 3);
        TextButton btnPlay = new TextButton("MENU", textButtonStyle);
        btnPlay.setPosition(Constantes.ANCHO_PANTALLA / 3 - btnPlay.getWidth() / 2 -100, Constantes.ALTO_PANTALLA / 4 - btnPlay.getHeight() / 2-100);

        escenaOpciones2.addActor(btnPlay);

        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Pantalla Creditos: ", "Voy a pantalla menu");
                menu.setScreen(new PantallaMenu(menu));
            }
        });
    }

    private void crearBotonJuego() {
        textButtonStyle = texto.generarTexto(Color.ORANGE, Color.GOLD, 3);
        TextButton btnPlay = new TextButton("CONTINUAR", textButtonStyle);
        btnPlay.setPosition(2 * Constantes.ANCHO_PANTALLA / 3 - btnPlay.getWidth() / 2+50, Constantes.ALTO_PANTALLA / 4 - btnPlay.getHeight() / 2-100);

        escenaOpciones2.addActor(btnPlay);

        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Pantalla Creditos: ", "Voy a pantalla menu");
                menu.setScreen(new PantallaJuego(menu));
            }
        });
    }

    private void cargarTexturas() {
        texturaFondo = new Texture("PantallaOpciones/PantallaOpciones.jpg");
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
        escenaOpciones2.draw();
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        spriteAyuda.draw(batch);
        spriteCodigos.draw(batch);
        spriteSonido.draw(batch);
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
}