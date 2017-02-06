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

public class PantallaInicio implements Screen {

    private final Menu menu;

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


    public PantallaInicio(Menu menu) {
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


        TextureRegionDrawable trdBtnPlay = new TextureRegionDrawable(new TextureRegion(texturaBtn));
        ImageButton btnPlay = new ImageButton(trdBtnPlay);
        btnPlay.setPosition(StarBlast.ANCHO_PANTALLA/2-btnPlay.getWidth()/2, StarBlast.ALTO_PANTALLA/4-btnPlay.getHeight()/2);

        escenaInicio.addActor(btnPlay);

        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Pantalla de Inicio: ","Voy a PantallaMenu");
                menu.setScreen(new PantallaMenu(menu));
            }
        });

        Gdx.input.setInputProcessor(escenaInicio);
        Gdx.input.setCatchBackKey(false);
    }

    private void cargarTexturas() {
        texturaFondo = new Texture("PantallaInicio/FondoInicio.jpg");
        texturaBtn = new Texture("PantallaInicio/TAP.png");
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
        escenaInicio.draw();
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
