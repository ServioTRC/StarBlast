package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


class PantallaPuntajes extends ScreenSB {

    private final StarBlast menu;

    //Texturas
    private Texture texturaFondo;

    //Escenas
    private Stage escenaInicio;

    //Text
    private Text text;
    private TextButton.TextButtonStyle textButtonStyle;

    PantallaPuntajes(StarBlast menu) {
        this.menu=menu;
    }

    @Override
    public void show() {
        cargarTexturas();
        crearObjetos();
    }

    private void crearObjetos() {
        SpriteBatch batch = new SpriteBatch();
        escenaInicio = new Stage(view, batch)
        {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    // DEBUG
                    Gdx.app.log("ScreenSB Puntajes: ","Voy al Menu");
                    menu.setScreen(new ScreenMenu(menu));
                    return true;
                }
                return super.keyDown(keycode);
            }
        };
        Image imgFondo = new Image(texturaFondo);
        text = new Text(Constants.SOURCE_TEXT);
        escenaInicio.addActor(imgFondo);
        crearBotonAtras();
        crearTitulo();
        imprimirPuntajes();
        Gdx.input.setInputProcessor(escenaInicio);
    }

    private void crearBotonAtras() {
        textButtonStyle = text.generateText(Color.RED,Color.GOLD,2);
        TextButton btnPlay = new TextButton("X", textButtonStyle);
        btnPlay.setPosition(7* Constants.SCREEN_WIDTH /8-btnPlay.getWidth()/2+100, Constants.SCREEN_HEIGTH /8-btnPlay.getHeight()/2);

        escenaInicio.addActor(btnPlay);

        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ScreenSB Creditos: ","Voy a pantalla menu");
                menu.setScreen(new ScreenMenu(menu));
            }
        });
    }

    private void crearTitulo() {
        textButtonStyle = text.generateText(Color.GOLD, Color.GOLD, 2);
        TextButton btnPlay = new TextButton("PUNTAJES MAS ALTOS", textButtonStyle);
        btnPlay.setPosition(Constants.SCREEN_WIDTH / 2 - btnPlay.getWidth() / 2, 9 * Constants.SCREEN_HEIGTH / 10 - btnPlay.getHeight() / 2);
        escenaInicio.addActor(btnPlay);
    }

    private void imprimirPuntajes(){
        Preferences prefs = Gdx.app.getPreferences("High Scores");
        String punt1 = "1. "+ prefs.getString("punt1", "----- 00000");
        crearMarcadores(punt1, Constants.SCREEN_WIDTH / 2, 3* Constants.SCREEN_HEIGTH / 4);
        String punt2 = "2. " + prefs.getString("punt2", "----- 00000");
        crearMarcadores(punt2, Constants.SCREEN_WIDTH / 2, 3* Constants.SCREEN_HEIGTH / 4-100);
        String punt3 = "3. " + prefs.getString("punt3", "----- 00000");
        crearMarcadores(punt3, Constants.SCREEN_WIDTH / 2, 3* Constants.SCREEN_HEIGTH / 4-200);
        String punt4 = "4. " + prefs.getString("punt4", "----- 00000");
        crearMarcadores(punt4, Constants.SCREEN_WIDTH / 2, 3* Constants.SCREEN_HEIGTH / 4-300);
        String punt5 = "5. " + prefs.getString("punt5", "----- 00000");
        crearMarcadores(punt5, Constants.SCREEN_WIDTH / 2, 3* Constants.SCREEN_HEIGTH / 4-400);
    }

    private void crearMarcadores(String nombre, float x, float y){
        textButtonStyle = text.generateText(Color.GOLD, Color.GOLD, 2);
        TextButton marcador = new TextButton(nombre, textButtonStyle);
        marcador.setPosition(x - marcador.getWidth() / 2, y - marcador.getHeight() / 2);
        escenaInicio.addActor(marcador);
    }

    private void cargarTexturas() {
        texturaFondo = new Texture("PantallaPuntajes/FondoSimple.jpg");
    }

    @Override
    public void render(float delta) {
        clearScreen();
        escenaInicio.draw();
    }

    @Override
    public void resize(int width, int height) {
        view.update(width, height);
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
