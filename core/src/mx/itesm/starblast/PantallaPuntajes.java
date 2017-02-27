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
 * Created by Servio T on 15/02/2017.
 */

public class PantallaPuntajes extends Pantalla {

    private final StarBlast menu;

    //Texturas
    private Texture texturaFondo;

    //SpriteBatch
    private SpriteBatch batch;

    //Escenas
    private Stage escenaInicio;

    //Texto
    Texto texto;
    private TextButton.TextButtonStyle textButtonStyle;

    public PantallaPuntajes(StarBlast menu) {
        this.menu=menu;
    }

    @Override
    public void show() {
        cargarTexturas();
        crearObjetos();
    }

    private void crearObjetos() {
        batch = new SpriteBatch();
        escenaInicio = new Stage(vista, batch)
        {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    // DEBUG
                    Gdx.app.log("Pantalla Puntajes: ","Voy al Menu");
                    menu.setScreen(new PantallaMenu(menu));
                    return true;
                }
                return super.keyDown(keycode);
            }
        };
        Image imgFondo = new Image(texturaFondo);
        texto = new Texto("Textos/Arcade50.fnt");
        escenaInicio.addActor(imgFondo);
        crearBotonAtras();
        crearTitulo();
        Gdx.input.setInputProcessor(escenaInicio);
    }

    private void crearBotonAtras() {
        textButtonStyle = texto.generarTexto(Color.RED,Color.GOLD,2);
        TextButton btnPlay = new TextButton("X", textButtonStyle);
        btnPlay.setPosition(7* Constantes.ANCHO_PANTALLA/8-btnPlay.getWidth()/2+100, Constantes.ALTO_PANTALLA/8-btnPlay.getHeight()/2);

        escenaInicio.addActor(btnPlay);

        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Pantalla Creditos: ","Voy a pantalla menu");
                menu.setScreen(new PantallaMenu(menu));
            }
        });
    }

    private void crearTitulo() {
        textButtonStyle = texto.generarTexto(Color.GOLD, Color.GOLD, 2);
        TextButton btnPlay = new TextButton("PUNTAJES MAS ALTOS", textButtonStyle);
        btnPlay.setPosition(Constantes.ANCHO_PANTALLA / 2 - btnPlay.getWidth() / 2, 9 * Constantes.ALTO_PANTALLA / 10 - btnPlay.getHeight() / 2);

        escenaInicio.addActor(btnPlay);
    }

    private void cargarTexturas() {
        texturaFondo = new Texture("PantallaPuntajes/fondoSimple.jpg");
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        escenaInicio.draw();
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
