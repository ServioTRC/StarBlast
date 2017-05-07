package mx.itesm.starblast.screens;

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

import mx.itesm.starblast.Constant;
import mx.itesm.starblast.PreferencesSB;
import mx.itesm.starblast.StarBlast;
import mx.itesm.starblast.Text;

public class ScreenMenu extends ScreenSB {

    private final StarBlast menu;

    //Texturas
    private Texture backgroundTexture;

    //Escenas
    private Stage menuScene;

    //Text
    private Text text;
    private TextButton.TextButtonStyle textButtonStyle;

    public ScreenMenu(StarBlast menu) {
        this.menu = menu;
    }

    @Override
    public void show() {
        menu.changeMusic("SoundEffects/MenuMusic.mp3");
        backgroundTexture = new Texture("MainScreen/MainScreen.jpg");
        creatingObjects();
    }

    private void creatingObjects() {
        SpriteBatch batch = new SpriteBatch();
        menuScene = new Stage(view, batch) {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    // DEBUG
                    Gdx.app.log("ScreenMenu", "Going to ScreenStart");
                    PreferencesSB.clickedSound();
                    menu.setScreen(new ScreenStart(menu));
                    return true;
                }
                return super.keyDown(keycode);
            }
        };
        Image backgroundImg = new Image(backgroundTexture);
//        backgroundImg.setScale(Math.max(Constant.SCREEN_WIDTH/backgroundTexture.getWidth(),Constant.SCREEN_HEIGTH/backgroundTexture.getHeight()));
        menuScene.addActor(backgroundImg);
        text = new Text(Constant.SOURCE_TEXT);
        creatingButtons();

        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(menuScene);

    }

    //region crear botones

    private void creatingButtons() {
        creatingCreditsButton();
        creatingEndlessButton();
        creatingStoryButton();
        creatingOptionsButton();
        creatingScoresButton();
        creatingMiniGamesButton();
    }

    private void creatingStoryButton() {
        textButtonStyle = text.generateText(Color.BLUE, Color.GOLD, 1);
        TextButton btnPlay = new TextButton("MODO HISTORIA", textButtonStyle);
        btnPlay.setPosition(Constant.SCREEN_WIDTH / 2 - btnPlay.getWidth() / 2, Constant.SCREEN_HEIGTH / 2 - btnPlay.getHeight() / 2 + 40);

        menuScene.addActor(btnPlay);

        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constant.isGameHelp = true;
                Gdx.app.log("ScreenMenu ", "Going to History mode");
                PreferencesSB.clickedSound();
                int level = PreferencesSB.readLevelProgress();
                if (level == 1) {
                    menu.setScreen(new ScreenLoading(menu, Constant.Screens.HELP));
                } else if (level == 2) {
                    if (PreferencesSB.getMinigameCount() > 0) {
                        menu.setScreen(new ScreenLoading(menu, Constant.Screens.LEVEL2));
                    } else {
                        menu.setScreen(new ScreenLoading(menu, Constant.Screens.MINIGAMES, true));
                    }
                } else if (level == 3) {
                    if (PreferencesSB.getMinigameCount() > 1) {
                        menu.setScreen(new ScreenLoading(menu, Constant.Screens.LEVEL3));
                    } else {
                        menu.setScreen(new ScreenLoading(menu, Constant.Screens.MINIGAMES, true));
                    }
                } else {
                    if (PreferencesSB.getMinigameCount() > 2) {
                        PreferencesSB.saveLevelProgress(1);
                        menu.setScreen(new ScreenLoading(menu, Constant.Screens.LEVEL1));
                    } else {
                        menu.setScreen(new ScreenLoading(menu, Constant.Screens.MINIGAMES, true));
                    }
                }
            }
        });
    }

    private void creatingEndlessButton() {
        final boolean active = PreferencesSB.readLevelProgress() > 3 || !Gdx.app.getPreferences("Levels").getBoolean("firstEndles", true);
        textButtonStyle = text.generateText(active ? Color.BLUE : Color.GRAY, active ? Color.GOLD : Color.GRAY, 1);
        TextButton btnPlay = new TextButton("MODO ENDLESS", textButtonStyle);
        btnPlay.setPosition(Constant.SCREEN_WIDTH / 2 - btnPlay.getWidth() / 2, 3 * Constant.SCREEN_HEIGTH / 8 - btnPlay.getHeight() / 2 + 80);

        menuScene.addActor(btnPlay);

        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ScreenMenu ", "Going to Endless Mode");
                PreferencesSB.clickedSound();
                if (active) menu.setScreen(new ScreenLoading(menu, Constant.Screens.ENDLESS));
            }
        });
    }

    private void creatingScoresButton() {
        textButtonStyle = text.generateText(Color.BLUE, Color.GOLD, 1);
        TextButton btnPlay = new TextButton("PUNTAJES MAS ALTOS", textButtonStyle);
        btnPlay.setPosition(Constant.SCREEN_WIDTH / 2 - btnPlay.getWidth() / 2, Constant.SCREEN_HEIGTH / 3 - btnPlay.getHeight() / 2 - 25);

        menuScene.addActor(btnPlay);

        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ScreenMenu ", "Going to Scores");
                PreferencesSB.clickedSound();
                //TODO Quitar o no la parte de cargar los assets en la pantalla opciones
                menu.setScreen(new ScreenLoading(menu, Constant.Screens.SCORES));
            }
        });
    }

    private void creatingOptionsButton() {
        textButtonStyle = text.generateText(Color.BLUE, Color.GOLD, 1);
        TextButton btnPlay = new TextButton("OPCIONES", textButtonStyle);
        btnPlay.setPosition(Constant.SCREEN_WIDTH / 2 - btnPlay.getWidth() / 2, 2 * Constant.SCREEN_HEIGTH / 8 - btnPlay.getHeight() / 2 - 30);

        menuScene.addActor(btnPlay);

        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ScreenMenu ", "Going to Scores");
                PreferencesSB.clickedSound();
                menu.setScreen(new ScreenLoading(menu, Constant.Screens.OPTIONS));
            }
        });
    }

    private void creatingCreditsButton() {
        textButtonStyle = text.generateText(Color.BLUE, Color.GOLD, 1);
        TextButton btnPlay = new TextButton("CREDITOS", textButtonStyle);
        btnPlay.setPosition(Constant.SCREEN_WIDTH / 2 - btnPlay.getWidth() / 2, Constant.SCREEN_HEIGTH / 8 - btnPlay.getHeight() / 2);
        menuScene.addActor(btnPlay);

        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ScreenMenu ", "Going to Credits");
                PreferencesSB.clickedSound();
                menu.setScreen(new ScreenCredits(menu));
            }
        });
    }

    private void creatingMiniGamesButton() {
        textButtonStyle = text.generateText(Color.BLUE, Color.GOLD, 1);
        TextButton btnPlay = new TextButton("MINIJUEGOS", textButtonStyle);
        btnPlay.setPosition(Constant.SCREEN_WIDTH / 2 - btnPlay.getWidth() / 2, Constant.SCREEN_HEIGTH / 3 - btnPlay.getHeight() / 2 + 40);
        menuScene.addActor(btnPlay);

        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ScreenMenu ", "Going to MiniGames");
                PreferencesSB.clickedSound();
                menu.setScreen(new ScreenLoading(menu, Constant.Screens.MINIGAMES));
            }
        });
    }

    //endregion

    @Override
    public void render(float delta) {
        clearScreen();
        menuScene.draw();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
    }
}
