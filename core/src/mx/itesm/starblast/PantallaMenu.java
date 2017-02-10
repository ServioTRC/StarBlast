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
 * Created by Servio T on 05/02/2017.
 */

public class PantallaMenu implements Screen {

    private final StarBlast menu;

    //Camara, vista
    private OrthographicCamera camara;
    private Viewport vista;

    //Texturas
    private Texture texturaFondo;

    //SpriteBatch
    private SpriteBatch batch;

    //Escenas
    private Stage escenaMenu;

    //Texto
    Texto texto;
    private TextButton.TextButtonStyle textButtonStyle;

    public PantallaMenu(StarBlast menu) {
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
        escenaMenu = new Stage(vista, batch);
        Image imgFondo = new Image(texturaFondo);
        escenaMenu.addActor(imgFondo);
        texto = new Texto("Textos/Arcade50.fnt");
        crearBotonCreditos();
        crearBotonEndless();
        crearBotonHistoria();
        crearBotonOpciones();

        Gdx.input.setInputProcessor(escenaMenu);
        Gdx.input.setCatchBackKey(false);

    }

    private void crearBotonHistoria(){
        textButtonStyle = texto.generarTexto(Color.BLUE,Color.GOLD,5);
        TextButton btnPlay = new TextButton("MODO HISTORIA", textButtonStyle);
        btnPlay.setPosition(Constantes.ANCHO_PANTALLA/2-btnPlay.getWidth()/2, Constantes.ALTO_PANTALLA/2-btnPlay.getHeight()/2);

        escenaMenu.addActor(btnPlay);

        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Pantalla Menu: ","Voy a Modo Historia");
                //menu.setScreen(new PantallaAcercaDe(menu));
            }
        });
    }

    private void crearBotonEndless(){
        textButtonStyle = texto.generarTexto(Color.BLUE,Color.GOLD,5);
        TextButton btnPlay = new TextButton("MODO ENDLESS", textButtonStyle);
        btnPlay.setPosition(Constantes.ANCHO_PANTALLA/2-btnPlay.getWidth()/2, 3* Constantes.ALTO_PANTALLA/8-btnPlay.getHeight()/2);

        escenaMenu.addActor(btnPlay);

        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Pantalla Menu: ","Me voy a Modo Endless");
                //menu.setScreen(new PantallaAcercaDe(menu));
            }
        });
    }

    private void crearBotonOpciones(){
        textButtonStyle = texto.generarTexto(Color.BLUE,Color.GOLD,5);
        TextButton btnPlay = new TextButton("OPCIONES", textButtonStyle);
        btnPlay.setPosition(Constantes.ANCHO_PANTALLA/2-btnPlay.getWidth()/2, 2* Constantes.ALTO_PANTALLA/8-btnPlay.getHeight()/2);

        escenaMenu.addActor(btnPlay);

        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Pantalla Menu: ","Me voy a Opciones");
                //menu.setScreen(new PantallaAcercaDe(menu));
            }
        });
    }

    private void crearBotonCreditos(){
        textButtonStyle = texto.generarTexto(Color.BLUE,Color.GOLD,5);
        TextButton btnPlay = new TextButton("CREDITOS", textButtonStyle);
        btnPlay.setPosition(Constantes.ANCHO_PANTALLA/2-btnPlay.getWidth()/2, Constantes.ALTO_PANTALLA/8-btnPlay.getHeight()/2);
        escenaMenu.addActor(btnPlay);

        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Pantalla Menu: "," Me voy a Créditos");
                menu.setScreen(new PantallaCreditos(menu));
            }
        });
    }

    private void cargarTexturas() {
        texturaFondo = new Texture("PantallaMenu/pantallaMenu.jpg");
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
        escenaMenu.draw();
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
