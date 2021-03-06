package mx.itesm.starblast.screens;

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

import mx.itesm.starblast.Constant;
import mx.itesm.starblast.PreferencesSB;
import mx.itesm.starblast.StarBlast;

class ScreenOptions extends ScreenSB {
    private final StarBlast menu;

    //Texturas
    private Texture backgroundTexture;

    //SpriteBatch
    private SpriteBatch batch;

    //Escenas
    private Stage optionScene;
    private Stage stageConfirm;

    private boolean isDialogOpen;

    ScreenOptions(StarBlast menu) {
        this.menu = menu;
    }

    @Override
    public void show() {
        loadTextures();
        createObjects();
    }

    private void createObjects() {
        batch = new SpriteBatch();
        optionScene = new Stage(view, batch) {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    Gdx.app.log("ScreenOptions ", "Going to ScreenMenu");
                    PreferencesSB.clickedSound();
                    menu.setScreen(new ScreenMenu(menu));
                    return true;
                }
                return super.keyDown(keycode);
            }
        };
        stageConfirm = new Stage(view, batch) {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    Gdx.input.setInputProcessor(optionScene);
                    PreferencesSB.clickedSound();
                    isDialogOpen = false;
                    return true;
                }
                return super.keyDown(keycode);
            }
        };
        Image imgFondo = new Image(backgroundTexture);
        optionScene.addActor(imgFondo);
        createConfirmDialog();
        createButtons();
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(optionScene);
    }

    private void createConfirmDialog() {
        final Image img = new Image(new Texture("SettingsScreen/ConfirmResetWIndow.jpg"));
        img.setPosition(Constant.SCREEN_WIDTH / 2, Constant.SCREEN_HEIGTH / 2, Align.center);
        stageConfirm.addActor(img);
        Skin skin = new Skin();
        skin.add("Up", new Texture("SettingsScreen/ButtonYes.png"));
        skin.add("Down", new Texture("SettingsScreen/ButtonYesYellow.png"));

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("Up");
        style.down = skin.getDrawable("Down");

        Button btn = new Button(style);
        btn.setPosition(Constant.SCREEN_WIDTH / 2 + 150, Constant.SCREEN_HEIGTH / 2 - 80, Align.center);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PreferencesSB.clickedSound();
                PreferencesSB.eraseGameInfo();
                isDialogOpen = false;
                Gdx.input.setInputProcessor(optionScene);
            }
        });
        stageConfirm.addActor(btn);

        skin = new Skin();
        skin.add("Up", new Texture("SettingsScreen/ButtonNo.png"));
        skin.add("Down", new Texture("SettingsScreen/ButtonNoYellow.png"));

        style = new Button.ButtonStyle();
        style.up = skin.getDrawable("Up");
        style.down = skin.getDrawable("Down");
        btn = new Button(style);
        btn.setPosition(Constant.SCREEN_WIDTH / 2 - 150, Constant.SCREEN_HEIGTH / 2 - 80, Align.center);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PreferencesSB.clickedSound();
                isDialogOpen = false;
                Gdx.input.setInputProcessor(optionScene);
            }
        });
        stageConfirm.addActor(btn);
    }

    private void createButtons() {
        createHelpButton();
        createMusicButton();
        createSoundButton();
        createCodesButton();
        createRestartButton();
        createBackButton();
    }

    private void createBackButton() {
        Skin skin = new Skin();
        skin.add("Up", new Texture("SettingsScreen/Back.png"));
        skin.add("Down", new Texture("SettingsScreen/BackYellow.png"));

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("Up");
        style.down = skin.getDrawable("Down");

        final Button btn = new Button(style);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PreferencesSB.clickedSound();
                menu.setScreen(new ScreenMenu(menu));
            }
        });
        btn.setPosition(12 * Constant.SCREEN_WIDTH / 13,
                Constant.SCREEN_HEIGTH / 8, Align.center);
        optionScene.addActor(btn);
    }

    private void createRestartButton() {
        Skin skin = new Skin();
        skin.add("Up", new Texture("SettingsScreen/ButtonReset.png"));
        skin.add("Down", new Texture("SettingsScreen/ButtonResetYellow.png"));

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("Up");
        style.down = skin.getDrawable("Down");

        Button btn = new Button(style);
        btn.setPosition(3 * Constant.SCREEN_WIDTH / 4,
                Constant.SCREEN_HEIGTH / 6 + 20, Align.center);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PreferencesSB.clickedSound();
                Gdx.input.setInputProcessor(stageConfirm);
                isDialogOpen = true;
            }
        });

        optionScene.addActor(btn);
    }

    private void createCodesButton() {
        Skin skin = new Skin();
        skin.add("Up", new Texture("SettingsScreen/ButtonCodes.png"));

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("Up");

        final Button btn = new Button(style);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PreferencesSB.clickedSound();
                Input.TextInputListener textListener = new Input.TextInputListener() {
                    @Override
                    public void input(String input) {
                        PreferencesSB.activateCheatCode(input, menu);
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
                Constant.SCREEN_HEIGTH / 3 + 10, Align.center);
        optionScene.addActor(btn);
    }

    private void createSoundButton() {
        Skin skin = new Skin();
        skin.add("Yes", new Texture("SettingsScreen/ButtonSound.png"));
        skin.add("No", new Texture("SettingsScreen/ButtonSoundOff.png"));

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("Yes");
        style.checked = skin.getDrawable("No");

        final Button btn = new Button(style);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PreferencesSB.SOUNDS_ENABLE = !btn.isChecked();
                PreferencesSB.saveSoundPreferences();
                PreferencesSB.clickedSound();
            }
        });
        btn.setChecked(!PreferencesSB.SOUNDS_ENABLE);
        btn.setPosition(3 * Constant.SCREEN_WIDTH / 4,
                2 * Constant.SCREEN_HEIGTH / 3 + 40, Align.center);
        optionScene.addActor(btn);
    }

    private void createMusicButton() {
        Skin skin = new Skin();
        skin.add("Yes", new Texture("SettingsScreen/ButtonMusic.png"));
        skin.add("No", new Texture("SettingsScreen/ButtonMusicOff.png"));

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("Yes");
        style.checked = skin.getDrawable("No");

        final Button btn = new Button(style);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PreferencesSB.MUSIC_ENABLE = !btn.isChecked();
                PreferencesSB.saveSoundPreferences();
                PreferencesSB.clickedSound();
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
        optionScene.addActor(btn);
    }

    private void createHelpButton() {
        Skin skin = new Skin();
        skin.add("Up", new Texture("SettingsScreen/ButtonHelp.png"));

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("Up");

        Button btn = new Button(style);
        btn.setPosition(3 * Constant.SCREEN_WIDTH / 4,
                Constant.SCREEN_HEIGTH / 2 - 25, Align.center);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PreferencesSB.clickedSound();
                Constant.isGameHelp = false;
                menu.setScreen(new ScreenLoading(menu, Constant.Screens.HELP));
            }
        });

        optionScene.addActor(btn);
    }

    private void loadTextures() {
        backgroundTexture = new Texture("SettingsScreen/SettingsWindow.jpg");
    }

    @Override
    public void render(float delta) {
        clearScreen();
        batch.setProjectionMatrix(camera.combined);
        optionScene.draw();
        if (isDialogOpen) {
            stageConfirm.draw();
        }
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
        Constant.MANAGER.dispose();
    }

}

