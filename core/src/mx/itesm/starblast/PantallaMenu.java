package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

class PantallaMenu extends Pantalla {

    private final StarBlast menu;

    //Texturas
    private Texture texturaFondo;

    //Escenas
    private Stage escenaMenu;

    //Texto
    private Texto texto;
    private TextButton.TextButtonStyle textButtonStyle;

    PantallaMenu(StarBlast menu) {
        this.menu=menu;
    }

    @Override
    public void show() {
        cargarTexturas();
        crearObjetos();
    }

    private void crearObjetos() {
        SpriteBatch batch = new SpriteBatch();
        escenaMenu = new Stage(vista, batch)
        {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    // DEBUG
                    Gdx.app.log("Pantalla Menu: ","Voy al Inicio");
                    menu.setScreen(new PantallaInicio(menu));
                    return true;
                }
                return super.keyDown(keycode);
            }
        };
        Image imgFondo = new Image(texturaFondo);
        escenaMenu.addActor(imgFondo);
        texto = new Texto(Constantes.TEXTO_FUENTE);
        crearBotones();

        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(escenaMenu);

    }

    private void crearBotones() {
        crearBotonCreditos();
        crearBotonEndless();
        crearBotonHistoria();
        crearBotonOpciones();
        crearBotonPuntajes();
        crearBotonMinijuegos();
    }

    private void crearBotonHistoria(){
        textButtonStyle = texto.generarTexto(Color.BLUE,Color.GOLD,1);
        TextButton btnPlay = new TextButton("MODO HISTORIA", textButtonStyle);
        btnPlay.setPosition(Constantes.ANCHO_PANTALLA/2-btnPlay.getWidth()/2, Constantes.ALTO_PANTALLA/2-btnPlay.getHeight()/2+40);

        escenaMenu.addActor(btnPlay);

        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Pantalla Menu: ","Voy a Modo Historia");
                menu.setScreen(new NivelPrueba(menu));
            }
        });
    }

    private void crearBotonEndless(){
        textButtonStyle = texto.generarTexto(Color.GRAY,Color.GRAY,1);
        TextButton btnPlay = new TextButton("MODO ENDLESS", textButtonStyle);
        btnPlay.setPosition(Constantes.ANCHO_PANTALLA/2-btnPlay.getWidth()/2, 3* Constantes.ALTO_PANTALLA/8-btnPlay.getHeight()/2+80);

        escenaMenu.addActor(btnPlay);

        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Pantalla Menu: ","Me voy a Modo Endless");
                //menu.setScreen(new PantallaJuego(menu));
            }
        });
    }

    private void crearBotonPuntajes(){
        textButtonStyle = texto.generarTexto(Color.BLUE,Color.GOLD,1);
        TextButton btnPlay = new TextButton("PUNTAJES MAS ALTOS", textButtonStyle);
        btnPlay.setPosition(Constantes.ANCHO_PANTALLA/2-btnPlay.getWidth()/2, Constantes.ALTO_PANTALLA/3-btnPlay.getHeight()/2-25);

        escenaMenu.addActor(btnPlay);

        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Pantalla Menu: ","Me voy a Puntajes");
                menu.setScreen(new PantallaPuntajes(menu));
            }
        });
    }

    private void crearBotonOpciones(){
        textButtonStyle = texto.generarTexto(Color.BLUE,Color.GOLD,1);
        TextButton btnPlay = new TextButton("OPCIONES", textButtonStyle);
        btnPlay.setPosition(Constantes.ANCHO_PANTALLA/2-btnPlay.getWidth()/2, 2* Constantes.ALTO_PANTALLA/8-btnPlay.getHeight()/2-30);

        escenaMenu.addActor(btnPlay);

        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Pantalla Menu: ","Me voy a Opciones");
                menu.setScreen(new PantallaOpciones(menu));
            }
        });
    }

    private void crearBotonCreditos(){
        textButtonStyle = texto.generarTexto(Color.BLUE,Color.GOLD,1);
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

    private void crearBotonMinijuegos(){
        textButtonStyle = texto.generarTexto(Color.BLUE,Color.GOLD,1);
        TextButton btnPlay = new TextButton("MINIJUEGOS", textButtonStyle);
        btnPlay.setPosition(Constantes.ANCHO_PANTALLA/2-btnPlay.getWidth()/2, Constantes.ALTO_PANTALLA/3-btnPlay.getHeight()/2+40);
        escenaMenu.addActor(btnPlay);

        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Pantalla Menu: "," Me voy a Minijuegos");
                menu.setScreen(new PantallaSeleccionMinijuegos(menu));
            }
        });
    }

    private void cargarTexturas() {
        texturaFondo = new Texture("PantallaMenu/PantallaMenu.jpg");
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        escenaMenu.draw();
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
