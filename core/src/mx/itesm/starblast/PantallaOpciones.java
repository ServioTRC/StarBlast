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

class PantallaOpciones extends ScreenSB {
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
        escenaOpciones = new Stage(view, batch){
            @Override
            public boolean keyDown(int keycode) {
                if(keycode == Input.Keys.BACK){
                    Gdx.app.log("ScreenSB Creditos: ","Voy al Menu");
                    menu.setScreen(new ScreenMenu(menu));
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
                menu.setScreen(new ScreenMenu(menu));
            }
        });
        btn.setPosition(12* Constants.SCREEN_WIDTH /13,
                Constants.SCREEN_HEIGTH /8, Align.center);
        escenaOpciones.addActor(btn);
    }

    private void crearBotonReiniciar() {
        Skin skin = new Skin();
        skin.add("Up", new Texture("PantallaOpciones/BotonReset.png"));
        skin.add("Down", new Texture("PantallaOpciones/BotonResetYellow.png"));

        Button.ButtonStyle estilo = new Button.ButtonStyle();
        estilo.up = skin.getDrawable("Up");
        estilo.down = skin.getDrawable("Down");

        Button btn = new Button(estilo);
        btn.setPosition(3* Constants.SCREEN_WIDTH /4,
                Constants.SCREEN_HEIGTH /6+20, Align.center);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PreferencesSB.erasingGameInfo();
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
                        Constants.CODES.add(input);
                    }

                    @Override
                    public void canceled() {
                        Gdx.app.log("Codigo Ingresado: ", "Salida del teclado");
                    }
                };

                Gdx.input.getTextInput(textListener, "Ingresar Código: ", "", "");
            }
        });
        btn.setPosition(3* Constants.SCREEN_WIDTH /4,
                Constants.SCREEN_HEIGTH /3+10, Align.center);
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
                PreferencesSB.SOUNDS_ENABLE = !btn.isChecked();
                PreferencesSB.savingSoundPreferences();
            }
        });
        btn.setChecked(!PreferencesSB.SOUNDS_ENABLE);
        btn.setPosition(3 * Constants.SCREEN_WIDTH / 4,
                2 * Constants.SCREEN_HEIGTH / 3 + 40, Align.center);
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
                PreferencesSB.MUSIC_ENABLE = !btn.isChecked();
                PreferencesSB.savingSoundPreferences();
                if (btn.isChecked()) {
                    menu.pauseMusic();
                } else {
                    menu.playMusic();
                }
            }
        });
        btn.setChecked(!PreferencesSB.MUSIC_ENABLE);
        btn.setPosition(3 * Constants.SCREEN_WIDTH / 4,
                Constants.SCREEN_HEIGTH / 2 + 75, Align.center);
        escenaOpciones.addActor(btn);
    }

    private void crearBotonAyuda(){
        Skin skin = new Skin();
        skin.add("Up", new Texture("PantallaOpciones/BotonAyuda.png"));

        Button.ButtonStyle estilo = new Button.ButtonStyle();
        estilo.up = skin.getDrawable("Up");

        Button btn = new Button(estilo);
        btn.setPosition(3* Constants.SCREEN_WIDTH /4,
                Constants.SCREEN_HEIGTH /2-25, Align.center);
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
        clearScreen();
        batch.setProjectionMatrix(camera.combined);
        escenaOpciones.draw();
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
        Constants.MANAGER.dispose();
    }

}

