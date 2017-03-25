package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

class StageResultados extends Stage {

    private final StarBlast menu;
    private final IPausable parent;
    private boolean ganador;
    private int puntaje;

    StageResultados(Viewport viewport, Batch batch, StarBlast menu, IPausable parent) {
        super(viewport, batch);
        this.menu = menu;
        this.parent = parent;
        this.ganador = false;
        this.puntaje = 0;
        init();
    }

    public void setGanadorYPuntaje(boolean ganador, int puntaje){
        this.ganador = ganador;
        this.puntaje = puntaje;
        pedirNombre();
        mostrarPuntaje();
    }

    private void init() {
        Image backgroud = new Image(new Texture("PantallaPuntajes/CuadroResultados.png"));
        backgroud.setPosition(Constantes.ANCHO_PANTALLA / 2 - backgroud.getWidth() / 2,
                Constantes.ALTO_PANTALLA / 2 - backgroud.getHeight() / 2);
        addActor(backgroud);
        crearBotonBack();
        crearBotonRegresar();
        mostrarLeyenda();
    }

    private void crearBotonRegresar() {Skin skin = new Skin();
        skin.add("Up", new Texture("PantallaOpciones/BotonReset.png"));
        skin.add("Down", new Texture("PantallaOpciones/BotonResetYellow.png"));

        Button.ButtonStyle estilo = new Button.ButtonStyle();
        estilo.up = skin.getDrawable("Up");
        estilo.down = skin.getDrawable("Down");

        final Button btn = new Button(estilo);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menu.setScreen(new NivelPrueba(menu));
            }
        });
        btn.setPosition(3 * Constantes.ANCHO_PANTALLA / 4 - 15,
                Constantes.ALTO_PANTALLA / 6 + 50, Align.center);
        addActor(btn);
    }

    private void crearBotonBack() {
        Skin skin = new Skin();
        skin.add("Up", new Texture("PantallaOpciones/Back.png"));
        skin.add("Down", new Texture("PantallaOpciones/BackYellow.png"));

        Button.ButtonStyle estilo = new Button.ButtonStyle();
        estilo.up = skin.getDrawable("Up");
        estilo.down = skin.getDrawable("Down");

        final Button btn = new Button(estilo);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menu.setScreen(new PantallaMenu(menu));
            }
        });
        btn.setPosition(Constantes.ANCHO_PANTALLA / 4 - 10,
                Constantes.ALTO_PANTALLA / 6 + 40, Align.center);
        addActor(btn);
    }

    private void mostrarLeyenda(){
        Texture textura;
        if(ganador){
            textura = new Texture("PantallaPuntajes/Ganador.png");
        }else{
            textura = new Texture("PantallaPuntajes/Perdedor.png");
        }
        Image leyenda = new Image(textura);
        leyenda.setPosition(Constantes.ANCHO_PANTALLA / 2 - leyenda.getWidth() / 2,
                3*Constantes.ALTO_PANTALLA / 4 - 50 - leyenda.getHeight() / 2);
        addActor(leyenda);
    }

    private void mostrarPuntaje(){
        Gdx.app.log("FINAL", Integer.toString(this.puntaje));
        Texto texto = new Texto(Constantes.TEXTO_FUENTE);
        TextButton.TextButtonStyle textButtonStyle = texto.generarTexto(Color.GOLD, Color.GOLD, 2);
        TextButton marcador = new TextButton(Integer.toString(this.puntaje), textButtonStyle);
        marcador.setPosition(Constantes.ANCHO_PANTALLA/2+100 - marcador.getWidth() / 2,
                Constantes.ALTO_PANTALLA/2-90 - marcador.getHeight() / 2);
        addActor(marcador);
    }

    private void pedirNombre(){
        Input.TextInputListener textListener = new Input.TextInputListener() {
            @Override
            public void input(String input) {
                Gdx.app.log("Nombre Ingresado: ", input);
                Preferencias.guardarPuntaje(input, puntaje);
            }

            @Override
            public void canceled() {
                Gdx.app.log("Nombre Ingresado: ", "Salida del teclado");
            }
        };

        Gdx.input.getTextInput(textListener, "Ingrese su Nombre: ", "", "");
    }

    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.BACK) {
            menu.setScreen(new PantallaMenu(menu));
            return true;
        }
        return super.keyDown(keyCode);
    }

}
