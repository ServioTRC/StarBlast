package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by delag on 3/14/2017.
 */

public class PantallaSeleccionMinijuegos extends Pantalla {

    private final StarBlast menu;

    //Texturas
    private Texture texturaFondo;

    //SpriteBatch
    private SpriteBatch batch;

    //Escenas
    private Stage escenaSeleccion;

    public PantallaSeleccionMinijuegos(StarBlast menu) {
        this.menu = menu;
    }

    @Override
    public void show() {
        cargarTexturas();
        crearObjetos();
    }

    private void crearObjetos() {
        batch = new SpriteBatch();
        escenaSeleccion = new Stage(vista, batch);
        Image imgFondo = new Image(texturaFondo);
        escenaSeleccion.addActor(imgFondo);
        crearBotonMinijuego1();
        crearBotonMinijuego2();
        crearBotonMinijuego3();

        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(escenaSeleccion);
    }

    private void crearBotonMinijuego1() {
        Texture textureUp = new Texture("PantallaSeleccionMinijuego/BotonMinijuego1.png");
        Texture textureDown = new Texture("PantallaSeleccionMinijuego/BotonMinijuego1.png");
        Button btn = new Button(new TextureRegionDrawable(new TextureRegion(textureUp)),
                                new TextureRegionDrawable(new TextureRegion(textureDown)));
        btn.setPosition(Constantes.ANCHO_PANTALLA / 5 - btn.getWidth() / 2,
                        Constantes.ALTO_PANTALLA / 4 - btn.getHeight() / 2);
        escenaSeleccion.addActor(btn);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("PantallaSeleccionMinijuegos: ", "Voy a pantalla minijuego1");
                //menu.setScreen(new PantallaJuego(menu));
            }
        });
    }

    private void crearBotonMinijuego2() {
        Texture textureUp = new Texture("PantallaSeleccionMinijuego/BotonMinijuego2.png");
        Texture textureDown = new Texture("PantallaSeleccionMinijuego/BotonMinijuego2.png");
        Button btn = new Button(new TextureRegionDrawable(new TextureRegion(textureUp)),
                new TextureRegionDrawable(new TextureRegion(textureDown)));
        btn.setPosition(Constantes.ANCHO_PANTALLA / 2 - btn.getWidth() / 2,
                Constantes.ALTO_PANTALLA / 2 - btn.getHeight() / 2);
        escenaSeleccion.addActor(btn);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("PantallaSeleccionMinijuegos: ", "Voy a pantalla minijuego2");
                //menu.setScreen(new PantallaJuego(menu));
            }
        });
    }

    private void crearBotonMinijuego3() {
        Texture textureUp = new Texture("PantallaSeleccionMinijuego/BotonMinijuego3.png");
        Texture textureDown = new Texture("PantallaSeleccionMinijuego/BotonMinijuego3.png");
        Button btn = new Button(new TextureRegionDrawable(new TextureRegion(textureUp)),
                new TextureRegionDrawable(new TextureRegion(textureDown)));
        btn.setPosition(Constantes.ANCHO_PANTALLA - Constantes.ANCHO_PANTALLA / 5 - btn.getWidth() / 2,
                Constantes.ALTO_PANTALLA / 4 - btn.getHeight() / 2);
        escenaSeleccion.addActor(btn);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("PantallaSeleccionMinijuegos: ", "Voy a pantalla minijuego3");
                //menu.setScreen(new PantallaJuego(menu));
            }
        });
    }

    private void cargarTexturas() {
        texturaFondo = new Texture("PantallaSeleccionMinijuego/PantallaSeleccionMinijuego.png");
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)){
            menu.setScreen(new PantallaMenu(menu));
        }
        borrarPantalla();
        escenaSeleccion.draw();
        batch.begin();

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

    }
}
