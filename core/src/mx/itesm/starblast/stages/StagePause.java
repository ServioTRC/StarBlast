package mx.itesm.starblast.stages;

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

import mx.itesm.starblast.Constant;
import mx.itesm.starblast.PreferencesSB;
import mx.itesm.starblast.StarBlast;

public class StagePause extends Stage {

    private final StarBlast menu;
    private final mx.itesm.starblast.gameEntities.IPausable parent;

    public StagePause(Viewport viewport, Batch batch, StarBlast menu, mx.itesm.starblast.gameEntities.IPausable parent) {
        super(viewport, batch);
        this.menu = menu;
        this.parent = parent;
        init();
    }

    private void init() {
        Image background = new Image(Constant.MANAGER.get("SettingsScreen/SettingsWindow.png", Texture.class));
        background.setPosition(Constant.SCREEN_WIDTH / 2 - background.getWidth() / 2,
                Constant.SCREEN_HEIGTH / 2 - background.getHeight() / 2);
        addActor(background);
        createSoundButton();
        createMusicButton();
        createBackButton();
        createCodesButton();
        createReturnButton();
    }

    private void createReturnButton() {
        Skin skin = new Skin();
        skin.add("Up", Constant.MANAGER.get("SettingsScreen/ButtonReset.png", Texture.class));
        skin.add("Down", Constant.MANAGER.get("SettingsScreen/ButtonResetYellow.png", Texture.class));

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("Up");
        style.down = skin.getDrawable("Down");

        final Button btn = new Button(style);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                parent.unPauseIP();
            }
        });
        btn.setPosition(3 * Constant.SCREEN_WIDTH / 4 - 15,
                Constant.SCREEN_HEIGTH / 6 + 50, Align.center);
        addActor(btn);
    }

    private void createCodesButton() {
        Skin skin = new Skin();
        skin.add("Up", Constant.MANAGER.get("SettingsScreen/ButtonCodes.png", Texture.class));

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("Up");

        final Button btn = new Button(style);
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

    private void createBackButton() {
        Skin skin = new Skin();
        skin.add("Up", Constant.MANAGER.get("SettingsScreen/Back.png", Texture.class));
        skin.add("Down", Constant.MANAGER.get("SettingsScreen/BackYellow.png", Texture.class));

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("Up");
        style.down = skin.getDrawable("Down");

        final Button btn = new Button(style);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menu.setScreen(new mx.itesm.starblast.screens.ScreenMenu(menu));
            }
        });
        btn.setPosition(Constant.SCREEN_WIDTH / 4 - 10,
                Constant.SCREEN_HEIGTH / 6 + 40, Align.center);
        addActor(btn);
    }

    private void createSoundButton() {
        Skin skin = new Skin();
        skin.add("Yes", Constant.MANAGER.get("SettingsScreen/ButtonSound.png", Texture.class));
        skin.add("No", Constant.MANAGER.get("SettingsScreen/ButtonSoundOff.png", Texture.class));

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("Yes");
        style.checked = skin.getDrawable("No");

        final Button btn = new Button(style);
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

    private void createMusicButton() {
        Skin skin = new Skin();
        skin.add("Yes", Constant.MANAGER.get("SettingsScreen/ButtonMusic.png", Texture.class));
        skin.add("No", Constant.MANAGER.get("SettingsScreen/ButtonMusicOff.png", Texture.class));

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("Yes");
        style.checked = skin.getDrawable("No");

        final Button btn = new Button(style);
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
            parent.unPauseIP();
            return true;
        }
        return super.keyDown(keyCode);
    }
}