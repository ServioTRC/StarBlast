package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

class PantallaOpciones extends Pantalla {
    private final StarBlast menu;

    //Texturas
    private Texture texturaFondo;

    //SpriteBatch
    private SpriteBatch batch;

    //Escenas
    private Stage escenaOpciones;

    PantallaOpciones(StarBlast menu) {
        this.menu=menu;
    }

    @Override
    public void show() {
        cargarTexturas();
        crearObjetos();
    }

    private void crearObjetos() {
        batch = new SpriteBatch();
        escenaOpciones = new Stage(vista, batch){
            @Override
            public boolean keyDown(int keycode) {
                if(keycode == Input.Keys.BACK){
                    Gdx.app.log("Pantalla Creditos: ","Voy al Menu");
                    menu.setScreen(new PantallaMenu(menu));
                    return true;
                }
                return super.keyDown(keycode);
            }
        };
        Image imgFondo = new Image(texturaFondo);
        escenaOpciones.addActor(imgFondo);
        crearBotones();
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(escenaOpciones);
    }

    private void crearBotones(){
        crearBotonAyuda();
        crearBotonMusica();
        crearBotonSonido();
        crearBotonCodigos();
        crearBotonReiniciar();
        crearBotonBack();
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
        btn.setPosition(12*Constantes.ANCHO_PANTALLA/13,
                Constantes.ALTO_PANTALLA/8, Align.center);
        escenaOpciones.addActor(btn);
    }

    private void crearBotonReiniciar() {
        Skin skin = new Skin();
        skin.add("Up", new Texture("PantallaOpciones/BotonReset.png"));

        Button.ButtonStyle estilo = new Button.ButtonStyle();
        estilo.up = skin.getDrawable("Up");

        Button btn = new Button(estilo);
        btn.setPosition(3*Constantes.ANCHO_PANTALLA/4,
                Constantes.ALTO_PANTALLA/6+20, Align.center);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO implementar
            }
        });

        escenaOpciones.addActor(btn);
    }

    private void crearBotonCodigos() {
        Skin skin = new Skin();
        skin.add("Up", new Texture("PantallaOpciones/BotonCodigos.png"));

        Button.ButtonStyle estilo = new Button.ButtonStyle();
        estilo.up = skin.getDrawable("Up");

        final Button btn = new Button(estilo);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Input.TextInputListener textListener = new Input.TextInputListener() {
                    @Override
                    public void input(String input) {
                        Gdx.app.log("Codigo Ingresado: ", input);
                        Constantes.CODIGOS.add(input);
                    }

                    @Override
                    public void canceled() {
                        Gdx.app.log("Codigo Ingresado: ", "Salida del teclado");
                    }
                };

                Gdx.input.getTextInput(textListener, "Ingresar Código: ", "", "");
            }
        });
        btn.setPosition(3*Constantes.ANCHO_PANTALLA/4,
                Constantes.ALTO_PANTALLA/3+10, Align.center);
        escenaOpciones.addActor(btn);
    }

    private void crearBotonSonido() {
        Skin skin = new Skin();
        skin.add("Si", new Texture("PantallaOpciones/BotonSonido.png"));
        skin.add("No", new Texture("PantallaOpciones/BotonNoSonido.png"));

        Button.ButtonStyle estilo = new Button.ButtonStyle();
        estilo.up = skin.getDrawable("Si");
        estilo.checked = skin.getDrawable("No");

        final Button btn = new Button(estilo);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Preferencias.SONIDO_HABILITADO = !btn.isChecked();
                Preferencias.escribirPreferenciasSonidos();
            }
        });
        btn.setChecked(!Preferencias.SONIDO_HABILITADO);
        btn.setPosition(3 * Constantes.ANCHO_PANTALLA / 4,
                2 * Constantes.ALTO_PANTALLA / 3 + 40, Align.center);
        escenaOpciones.addActor(btn);
    }

    private void crearBotonMusica() {
        Skin skin = new Skin();
        skin.add("Si", new Texture("PantallaOpciones/BotonMusica.png"));
        skin.add("No", new Texture("PantallaOpciones/BotonNoMusica.png"));

        Button.ButtonStyle estilo = new Button.ButtonStyle();
        estilo.up = skin.getDrawable("Si");
        estilo.checked = skin.getDrawable("No");

        final Button btn = new Button(estilo);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Preferencias.MUSICA_HABILITADA = !btn.isChecked();
                Preferencias.escribirPreferenciasSonidos();
                if (btn.isChecked()) {
                    menu.pauseMusica();
                } else {
                    menu.playMusica();
                }
            }
        });
        btn.setChecked(!Preferencias.MUSICA_HABILITADA);
        btn.setPosition(3 * Constantes.ANCHO_PANTALLA / 4,
                Constantes.ALTO_PANTALLA / 2 + 75, Align.center);
        escenaOpciones.addActor(btn);
    }

    private void crearBotonAyuda(){
        Skin skin = new Skin();
        skin.add("Up", new Texture("PantallaOpciones/BotonAyuda.png"));

        Button.ButtonStyle estilo = new Button.ButtonStyle();
        estilo.up = skin.getDrawable("Up");

        Button btn = new Button(estilo);
        btn.setPosition(3*Constantes.ANCHO_PANTALLA/4,
                Constantes.ALTO_PANTALLA/2-25, Align.center);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO implementar
            }
        });

        escenaOpciones.addActor(btn);
    }

    private void cargarTexturas() {
        texturaFondo = new Texture("PantallaOpciones/PantallaOpciones.jpg");
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);
        escenaOpciones.draw();
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
        Constantes.ASSET_GENERAL.dispose();
    }

}

