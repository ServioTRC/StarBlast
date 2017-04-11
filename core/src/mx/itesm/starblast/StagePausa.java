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

class StagePausa extends Stage {

    private final StarBlast menu;
    private final IPausable parent;

    StagePausa(Viewport viewport, Batch batch, StarBlast menu, IPausable parent) {
        super(viewport, batch);
        this.menu = menu;
        this.parent = parent;
        init();
    }

    private void init() {
        Image backgroud = new Image(Constant.MANAGER.get("PantallaOpciones/CuadroOpciones.png", Texture.class));
        backgroud.setPosition(Constant.SCREEN_WIDTH / 2 - backgroud.getWidth() / 2,
                Constant.SCREEN_HEIGTH / 2 - backgroud.getHeight() / 2);
        addActor(backgroud);
        crearBotonSonido();
        crearBotonMusica();
        crearBotonBack();
        crearBotonCodigos();
        crearBotonRegresar();
    }

    private void crearBotonRegresar() {
        Skin skin = new Skin();
        skin.add("Up", Constant.MANAGER.get("PantallaOpciones/BotonReset.png", Texture.class));
        skin.add("Down", Constant.MANAGER.get("PantallaOpciones/BotonResetYellow.png", Texture.class));

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
        btn.setPosition(3 * Constant.SCREEN_WIDTH / 4 - 15,
                Constant.SCREEN_HEIGTH / 6 + 50, Align.center);
        addActor(btn);
    }

    private void crearBotonCodigos() {
        Skin skin = new Skin();
        skin.add("Up", Constant.MANAGER.get("PantallaOpciones/BotonCodigos.png", Texture.class));

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
                        Constant.CODES.add(input);
                    }

                    @Override
                    public void canceled() {
                        Gdx.app.log("Codigo Ingresado: ", "Salida del teclado");
                    }
                };

                Gdx.input.getTextInput(textListener, "Ingresar Código: ", "", "");
            }
        });
        btn.setPosition(3 * Constant.SCREEN_WIDTH / 4,
                Constant.SCREEN_HEIGTH / 2 - 25, Align.center);
        addActor(btn);
    }

    private void crearBotonBack() {
        Skin skin = new Skin();
        skin.add("Up", Constant.MANAGER.get("PantallaOpciones/Back.png", Texture.class));
        skin.add("Down", Constant.MANAGER.get("PantallaOpciones/BackYellow.png", Texture.class));

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
        btn.setPosition(Constant.SCREEN_WIDTH / 4 - 10,
                Constant.SCREEN_HEIGTH / 6 + 40, Align.center);
        addActor(btn);
    }

    private void crearBotonSonido() {
        Skin skin = new Skin();
        skin.add("Si", Constant.MANAGER.get("PantallaOpciones/BotonSonido.png", Texture.class));
        skin.add("No", Constant.MANAGER.get("PantallaOpciones/BotonNoSonido.png", Texture.class));

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
        btn.setPosition(3 * Constant.SCREEN_WIDTH / 4,
                2 * Constant.SCREEN_HEIGTH / 3 + 40, Align.center);
        addActor(btn);
    }

    private void crearBotonMusica() {
        Skin skin = new Skin();
        skin.add("Si", Constant.MANAGER.get("PantallaOpciones/BotonMusica.png", Texture.class));
        skin.add("No", Constant.MANAGER.get("PantallaOpciones/BotonNoMusica.png", Texture.class));

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
        btn.setPosition(3 * Constant.SCREEN_WIDTH / 4,
                Constant.SCREEN_HEIGTH / 2 + 75, Align.center);
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
