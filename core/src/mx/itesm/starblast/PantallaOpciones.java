package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
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

public class PantallaOpciones implements Screen {
    private final StarBlast menu;

    //Camara, vista
    private OrthographicCamera camara;
    private Viewport vista;

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
    private GeneralSprite spriteAyuda;
    private GeneralSprite spriteCodigos;

    public PantallaOpciones(StarBlast menu) {
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
        escenaOpciones = new Stage(vista, batch);
        Image imgFondo = new Image(texturaFondo);
        escenaOpciones.addActor(imgFondo);
        texto = new Texto("Textos/Arcade50.fnt");
        crearBotonAtras();
        crearSprites();
        Gdx.input.setInputProcessor(escenaOpciones);
        Gdx.input.setCatchBackKey(false);

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

    private void crearSprites(){
        spriteAyuda = new GeneralSprite("PantallaOpciones/BotonAyuda.png",3*Constantes.ANCHO_PANTALLA/4,
                3*Constantes.ALTO_PANTALLA/4);
        spriteSonido = new GeneralSprite("PantallaOpciones/BotonSonido.png",3*Constantes.ANCHO_PANTALLA/4,
                Constantes.ALTO_PANTALLA/2);
        spriteCodigos = new GeneralSprite("PantallaOpciones/botonCodigos.png",3*Constantes.ANCHO_PANTALLA/4,
                1*Constantes.ALTO_PANTALLA/4);
    }

    private void crearBotonAtras() {
        textButtonStyle = texto.generarTexto(Color.RED,Color.GOLD,5);
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

    private void crearCamara() {
        camara = new OrthographicCamera(Constantes.ANCHO_PANTALLA, Constantes.ALTO_PANTALLA);
        camara.position.set(Constantes.ANCHO_PANTALLA/2, Constantes.ALTO_PANTALLA/2,0);
        camara.update();
        vista = new StretchViewport(Constantes.ANCHO_PANTALLA, Constantes.ALTO_PANTALLA, camara);
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

}
