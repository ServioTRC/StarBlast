package mx.itesm.starblast.stages;

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

import mx.itesm.starblast.Constant;
import mx.itesm.starblast.PreferencesSB;
import mx.itesm.starblast.StarBlast;
import mx.itesm.starblast.Text;
import mx.itesm.starblast.gameEntities.IPausable;

public class StagePause extends Stage {

    private final StarBlast menu;
    private final IPausable parent;

    public StagePause(Viewport viewport, Batch batch, StarBlast menu, IPausable parent) {
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
        createReturnButton();
    }

    private void createReturnButton() {
        Skin skin = new Skin();
        skin.add("Up", Constant.MANAGER.get("SettingsScreen/ButtonBack.png", Texture.class));
        skin.add("Down", Constant.MANAGER.get("SettingsScreen/ButtonBackYellow.png", Texture.class));

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("Up");
        style.down = skin.getDrawable("Down");

        final Button btn = new Button(style);
        ClickListener tmp = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PreferencesSB.clickedSound();
                parent.unPauseIP();
            }
        };
        btn.addListener(tmp);
        btn.setPosition(3 * Constant.SCREEN_WIDTH / 4 - 75,
                Constant.SCREEN_HEIGTH / 6 + 50, Align.center);
        addActor(btn);
        Text text = new Text(Constant.SOURCE_TEXT);
        TextButton.TextButtonStyle textButtonStyle = text.generateText(new Color(Color.rgba8888(19f / 255f, 114f / 255f, 211f / 255f, 1f)), Color.GOLD, 1.5f);
        TextButton tbt = new TextButton("CONTINUAR", textButtonStyle);
        tbt.addListener(tmp);
        tbt.setPosition(3 * Constant.SCREEN_WIDTH / 4 - 75,
                105, Align.center);
        addActor(tbt);
    }

    private void createBackButton() {
        Skin skin = new Skin();
        skin.add("Up", Constant.MANAGER.get("SettingsScreen/Back.png", Texture.class));
        skin.add("Down", Constant.MANAGER.get("SettingsScreen/BackYellow.png", Texture.class));

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("Up");
        style.down = skin.getDrawable("Down");

        final Button btn = new Button(style);
        ClickListener tmp = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PreferencesSB.clickedSound();
                menu.setScreen(new mx.itesm.starblast.screens.ScreenMenu(menu));
            }
        };
        btn.addListener(tmp);
        btn.setPosition(Constant.SCREEN_WIDTH / 4 - 10,
                Constant.SCREEN_HEIGTH / 6 + 40, Align.center);
        addActor(btn);
        Text text = new Text(Constant.SOURCE_TEXT);
        TextButton.TextButtonStyle textButtonStyle = text.generateText(new Color(Color.rgba8888(19f / 255f, 114f / 255f, 211f / 255f, 1f)), Color.GOLD, 1.5f);
        TextButton tbt = new TextButton("SALIR", textButtonStyle);
        tbt.addListener(tmp);
        tbt.setPosition(Constant.SCREEN_WIDTH / 4 - 10,
                105, Align.center);
        addActor(tbt);
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
                PreferencesSB.saveSoundPreferences();
                PreferencesSB.clickedSound();
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
                PreferencesSB.clickedSound();
                PreferencesSB.MUSIC_ENABLE = !btn.isChecked();
                PreferencesSB.saveSoundPreferences();
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
            PreferencesSB.clickedSound();
            parent.unPauseIP();
            return true;
        }
        return super.keyDown(keyCode);
    }
}
