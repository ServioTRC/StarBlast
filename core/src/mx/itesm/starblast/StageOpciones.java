package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

class StageOpciones extends Stage {

    private final StarBlast menu;
    private final IPausable parent;

    StageOpciones(Viewport viewport, Batch batch, StarBlast menu, IPausable parent) {
        super(viewport, batch);
        this.menu = menu;
        this.parent = parent;
        init();
    }

    private void init() {
        Image backgroud = new Image(new Texture("PantallaOpciones/CuadroOpciones.png"));
        backgroud.setPosition(Constantes.ANCHO_PANTALLA / 2 - backgroud.getWidth() / 2,
                Constantes.ALTO_PANTALLA / 2 - backgroud.getHeight() / 2);
        addActor(backgroud);
        crearBotonSonido();
        crearBotonMusica();
        crearBotonBack();
        crearBotonCodigos();
        crearBotonRegresar();
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
                parent.unPause();
            }
        });
        btn.setPosition(3 * Constantes.ANCHO_PANTALLA / 4 - 15,
                Constantes.ALTO_PANTALLA / 6 + 50, Align.center);
        addActor(btn);
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
        btn.setPosition(3 * Constantes.ANCHO_PANTALLA / 4,
                Constantes.ALTO_PANTALLA / 2 - 25, Align.center);
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
        addActor(btn);
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
        addActor(btn);
    }

    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.BACK) {
            parent.unPause();
            return true;
        }
        return super.keyDown(keyCode);
    }
}
