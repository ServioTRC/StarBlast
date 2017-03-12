package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
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

    //Preferencias (archivos)
    Preferences prefs;

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
        texto = new Texto(Constantes.TEXTO_FUENTE);
        escenaInicio.addActor(imgFondo);
        crearBotonAtras();
        crearTitulo();
        imprimirPuntajes();
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

    private void imprimirPuntajes(){
        prefs = Gdx.app.getPreferences("Puntajes Mas Altos");
        String punt1 = prefs.getString("punt1", "1. ----- 00000");
        crearMarcadores(punt1,Constantes.ANCHO_PANTALLA / 2, 3*Constantes.ALTO_PANTALLA / 4);
        String punt2 = prefs.getString("punt2", "2. ----- 00000");
        crearMarcadores(punt2,Constantes.ANCHO_PANTALLA / 2, 3*Constantes.ALTO_PANTALLA / 4-100);
        String punt3 = prefs.getString("punt3", "3. ----- 00000");
        crearMarcadores(punt3,Constantes.ANCHO_PANTALLA / 2, 3*Constantes.ALTO_PANTALLA / 4-200);
        String punt4 = prefs.getString("punt4", "4. ----- 00000");
        crearMarcadores(punt4,Constantes.ANCHO_PANTALLA / 2, 3*Constantes.ALTO_PANTALLA / 4-300);
        String punt5 = prefs.getString("punt5", "5. ----- 00000");
        crearMarcadores(punt5,Constantes.ANCHO_PANTALLA / 2, 3*Constantes.ALTO_PANTALLA / 4-400);
    }

    private void crearMarcadores(String nombre, float x, float y){
        textButtonStyle = texto.generarTexto(Color.GOLD, Color.GOLD, 2);
        TextButton btnPlay = new TextButton(nombre, textButtonStyle);
        btnPlay.setPosition(x - btnPlay.getWidth() / 2, y - btnPlay.getHeight() / 2);
        escenaInicio.addActor(btnPlay);
    }

    private void cargarTexturas() {
        texturaFondo = new Texture("PantallaPuntajes/FondoSimple.jpg");
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
