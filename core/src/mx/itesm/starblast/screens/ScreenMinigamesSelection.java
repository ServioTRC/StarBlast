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

class ScreenMinigamesSelection extends ScreenSB {
    private final StarBlast menu;
    private final boolean story;

    //SpriteBatch
    private SpriteBatch batch;

    //Escenas
    private Stage selectionScene;

    ScreenMinigamesSelection(StarBlast menu, boolean story) {
        this.menu = menu;
        this.story = story;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        selectionScene = new Stage(view, batch) {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    Gdx.app.log("ScreenMinigamesSelection ", "Going to Screen Menu");
                    dispose();
                    PreferencesSB.clickedSound();
                    menu.setScreen(new ScreenMenu(menu));
                    return true;
                }
                return super.keyDown(keycode);
            }
        };
        Image backgroundImage =
                new Image(Constant.MANAGER.get("MinigameSelectionScreen/MinigameSelectionBackground.png", Texture.class));
        selectionScene.addActor(backgroundImage);
        createMinigame1Button();
        createMinigame2Button();
        createMinigame3Button();
        createBackButton();

        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(selectionScene);
    }

    private void createMinigame1Button() {
        boolean active = Gdx.app.getPreferences("Minigames").getBoolean("1", false) | story;
        Skin skin = new Skin();
        skin.add("up", Constant.MANAGER.get("MinigameSelectionScreen/ButtonMinigame1" + (active ? "" : "Grey") + ".png", Texture.class));
        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("up");
        Button btn = new Button(style);
        btn.setPosition(Constant.SCREEN_WIDTH / 5 - btn.getWidth() / 2,
                Constant.SCREEN_HEIGTH / 4 - btn.getHeight() / 2);
        selectionScene.addActor(btn);
        if (!active) {
            return;
        }
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ScreenMinigamesSelection ", "Going to MiniGame1");
                PreferencesSB.clickedSound();
                menu.setScreen(new ScreenLoading(menu, Constant.Screens.MINI1, story));
            }
        });
    }

    private void createMinigame2Button() {
        boolean active = Gdx.app.getPreferences("Minigames").getBoolean("2", false) | story;
        Skin skin = new Skin();
        skin.add("up", Constant.MANAGER.get("MinigameSelectionScreen/ButtonMinigame2" + (active ? "" : "Grey") + ".png", Texture.class));
        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("up");
        Button btn = new Button(style);
        btn.setPosition(Constant.SCREEN_WIDTH / 2 - btn.getWidth() / 2,
                Constant.SCREEN_HEIGTH / 2 - btn.getHeight() / 2);
        selectionScene.addActor(btn);
        if (!active) {
            return;
        }
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ScreenMinigamesSelection ", "Going to MiniGame2");
                dispose();
                PreferencesSB.clickedSound();
                menu.setScreen(new ScreenLoading(menu, Constant.Screens.MINI2, story));
            }
        });
    }

    private void createMinigame3Button() {
        boolean active = Gdx.app.getPreferences("Minigames").getBoolean("3", false) | story;
        Skin skin = new Skin();
        skin.add("up", Constant.MANAGER.get("MinigameSelectionScreen/ButtonMinigame3" + (active ? "" : "Grey") + ".png", Texture.class));
        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("up");
        Button btn = new Button(style);
        btn.setPosition(Constant.SCREEN_WIDTH - Constant.SCREEN_WIDTH / 5 - btn.getWidth() / 2,
                Constant.SCREEN_HEIGTH / 4 - btn.getHeight() / 2);
        selectionScene.addActor(btn);
        if (!active) {
            return;
        }
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ScreenMinigamesSelection ", "Going to MiniGame3");
                PreferencesSB.clickedSound();
                menu.setScreen(new ScreenLoading(menu, Constant.Screens.MINI3, story));
            }
        });
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
                7 * Constant.SCREEN_HEIGTH / 8, Align.center);
        selectionScene.addActor(btn);
    }

    @Override
    public void render(float delta) {
        selectionScene.draw();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
