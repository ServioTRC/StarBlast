package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Servio T on 05/02/2017.
 */

public class PantallaMenu implements Screen {

    private final Menu menu;

    //Camara, vista
    private OrthographicCamera camara;
    private Viewport vista;

    //Texturas
    private Texture texturaFondo;
    private Texture texturaHistoria;
    private Texture texturaEndless;
    private Texture texturaOpciones;
    private Texture texturaCreditos;

    //SpriteBatch
    private SpriteBatch batch;

    //Escenas
    private Stage escenaMenu;


    public PantallaMenu(Menu menu) {
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

        crearBotonCreditos();
        crearBotonEndless();
        crearBotonHistoria();
        crearBotonOpciones();

        Gdx.input.setInputProcessor(escenaMenu);
        Gdx.input.setCatchBackKey(false);

    }

    private void crearBotonHistoria(){
        TextureRegionDrawable trdBtnPlay = new TextureRegionDrawable(new TextureRegion(texturaHistoria));
        ImageButton btnPlay = new ImageButton(trdBtnPlay);
        btnPlay.setPosition(StarBlast.ANCHO_PANTALLA/2-btnPlay.getWidth()/2, StarBlast.ALTO_PANTALLA/2-btnPlay.getHeight()/2);

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
        TextureRegionDrawable trdBtnPlay = new TextureRegionDrawable(new TextureRegion(texturaEndless));
        ImageButton btnPlay = new ImageButton(trdBtnPlay);
        btnPlay.setPosition(StarBlast.ANCHO_PANTALLA/2-btnPlay.getWidth()/2, 3*StarBlast.ALTO_PANTALLA/8-btnPlay.getHeight()/2);

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
        TextureRegionDrawable trdBtnPlay = new TextureRegionDrawable(new TextureRegion(texturaOpciones));
        ImageButton btnPlay = new ImageButton(trdBtnPlay);
        btnPlay.setPosition(StarBlast.ANCHO_PANTALLA/2-btnPlay.getWidth()/2, 2*StarBlast.ALTO_PANTALLA/8-btnPlay.getHeight()/2);

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
        TextureRegionDrawable trdBtnPlay = new TextureRegionDrawable(new TextureRegion(texturaCreditos));
        ImageButton btnPlay = new ImageButton(trdBtnPlay);
        btnPlay.setPosition(StarBlast.ANCHO_PANTALLA/2-btnPlay.getWidth()/2, StarBlast.ALTO_PANTALLA/8-btnPlay.getHeight()/2);

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
        texturaHistoria = new Texture("PantallaMenu/historia.png");
        texturaEndless = new Texture("PantallaMenu/endless.png");
        texturaOpciones = new Texture("PantallaMenu/opciones.png");
        texturaCreditos = new Texture("PantallaMenu/creditos.png");

    }

    private void crearCamara() {
        camara = new OrthographicCamera(StarBlast.ANCHO_PANTALLA, StarBlast.ALTO_PANTALLA);
        camara.position.set(StarBlast.ANCHO_PANTALLA/2, StarBlast.ALTO_PANTALLA/2,0);
        camara.update();
        vista = new StretchViewport(StarBlast.ANCHO_PANTALLA, StarBlast.ALTO_PANTALLA, camara);
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
