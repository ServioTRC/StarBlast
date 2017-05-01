package mx.itesm.starblast.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Locale;

import mx.itesm.starblast.Constant;
import mx.itesm.starblast.PreferencesSB;
import mx.itesm.starblast.StarBlast;
import mx.itesm.starblast.Text;
import mx.itesm.starblast.screens.ScreenMenu;
import mx.itesm.starblast.screens.ScreenScores;


public class StageLostEndless extends Stage {

    private final StarBlast menu;
    private int score = 0;
    private Text text = new Text(Constant.SOURCE_TEXT);
    Batch batch;

    public StageLostEndless(Viewport viewport, Batch batch, StarBlast starBlast) {
        super(viewport, batch);
        this.menu = starBlast;
        this.batch = batch;
        Image background = new Image(Constant.MANAGER.get("DefeatScreen/ArcadeDefeatBackground.jpg", Texture.class));
        background.setPosition(Constant.SCREEN_WIDTH / 2 - background.getWidth() / 2,
                Constant.SCREEN_HEIGTH / 2 - background.getHeight() / 2);
        addActor(background);
        crateSaveButton();
        createIgnoreButton();
    }

    private void crateSaveButton() {
        TextButton.TextButtonStyle textButtonStyle = text.generateText(new Color(Color.rgba8888(85f / 255f, 127f / 255f, 139f / 255f, 1f)), Color.GOLD, 1.5f);
        TextButton btn = new TextButton("GUARDAR", textButtonStyle);
        btn.setPosition(Constant.SCREEN_WIDTH * 5 / 8 - 40, 140);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Input.TextInputListener textListener = new Input.TextInputListener() {
                    @Override
                    public void input(String input) {
                        input = input.trim();
                        if (input.length() == 0 || input.length() > 5) {
                            return;
                        }
                        for (char c : input.toCharArray()) {
                            if (!Character.isLetterOrDigit(c)) {
                                return;
                            }
                        }
                        PreferencesSB.saveScore(input, score);
                        menu.setScreen(new ScreenScores(menu));
                    }

                    @Override
                    public void canceled() {
                    }
                };
                Gdx.input.getTextInput(textListener, "Ingresa tu nombre: ", "", "Maximo 5 caracteres alfanumericos");
            }
        });
        addActor(btn);
    }

    private void createIgnoreButton() {
        TextButton.TextButtonStyle textButtonStyle = text.generateText(new Color(Color.rgba8888(85f / 255f, 127f / 255f, 139f / 255f, 1f)), Color.GOLD, 1.5f);
        TextButton btn = new TextButton("CONTINUAR", textButtonStyle);
        btn.setPosition(Constant.SCREEN_WIDTH / 8 - 20, 140);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menu.setScreen(new ScreenMenu(menu));
            }
        });
        addActor(btn);
    }

    public void setScore(int score) {
        this.score = Math.min(Math.max(score, 0), 99999);
        text.generateStaticMessage(String.format(Locale.US, "%d", this.score), new Color(Color.rgba8888(85f / 255f, 127f / 255f, 139f / 255f, 1f)), 1.5f);
    }

    @Override
    public void draw() {
        super.draw();
        batch.begin();
        text.showStaticMessage(batch, Constant.SCREEN_WIDTH / 2 + 40, 468);
        batch.end();
    }
}
