package mx.itesm.starblast.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

import mx.itesm.starblast.Constant;
import mx.itesm.starblast.PreferencesSB;
import mx.itesm.starblast.StarBlast;

class ScreenCutscenes extends ScreenSB {

    private boolean isStoryMode;
    private Constant.Screens screen;
    StarBlast app;
    private ArrayList<Texture> textures = new ArrayList<Texture>();
    private SpriteBatch batch;
    private int num = 0;
    private Sprite sprite;
    private long startTime;
    private float currentTime = 0;
    private Sprite tapToContinue;
    private Texture background;
    private boolean inTransition;
    private float transitionTime;

    ScreenCutscenes(StarBlast app, Constant.Screens screen) {
        this(app, screen, false);
    }

    ScreenCutscenes(StarBlast app, Constant.Screens screen, boolean isStoryMode) {
        super();
        this.app = app;
        this.screen = screen;
        this.isStoryMode = isStoryMode;
        background = new Texture("HighScoresScreen/BackgroundHighScores.jpg");
        switch (screen) {
            case LEVEL1:
                for (int i = 1; i < 6; i++) {
                    textures.add(new Texture("StoryScreen/Intro/Level1_Story" + i + ".jpg"));
                }
                textures.add(new Texture("StoryScreen/Intro/Mision1.jpg"));
                break;
            case LEVEL2:
                for (int i = 1; i < 3; i++) {
                    textures.add(new Texture("StoryScreen/Level 2/Level2_Story" + i + ".jpg"));
                }
                textures.add(new Texture("StoryScreen/Level 2/Mision2.jpg"));
                break;
            case LEVEL3:
                for (int i = 1; i < 3; i++) {
                    textures.add(new Texture("StoryScreen/Level 3/Level3_Story" + i + ".jpg"));
                }
                textures.add(new Texture("StoryScreen/Level 3/Mision3.jpg"));
                break;
            case ENDLESS:
                for (int i = 1; i < 3; i++) {
                    textures.add(new Texture("StoryScreen/Ending/Ending" + i + ".jpg"));
                }
                textures.add(new Texture("StoryScreen/Ending/TheEnd.jpg"));
                textures.add(new Texture("StoryScreen/Ending/EndlessIntro.jpg"));
                break;
            case MINI1:
                textures.add(new Texture("Minigame1Screen/SplashTutorial1.png"));
                textures.add(new Texture("Minigame1Screen/SplashTutorial2.png"));
                break;
            case MINI2:
                textures.add(new Texture("Minigame2Screen/SplashTutorial1.png"));
                textures.add(new Texture("Minigame2Screen/SplashTutorial2.png"));
                break;
            case MINI3:
                textures.add(new Texture("Minigame3Screen/SplashTutorial1.png"));
                textures.add(new Texture("Minigame3Screen/SplashTutorial2.png"));
                break;
            case MINIGAMES:
                textures.add(new Texture("MinigameSelectionScreen/SplashTutorial1.png"));
                break;
        }
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        sprite = new Sprite(textures.get(0));
        startTime = TimeUtils.millis();
        tapToContinue = new Sprite(new Texture("StoryScreen/BannerTapToContinue.png"));
        tapToContinue.setY(Constant.SCREEN_HEIGTH - tapToContinue.getHeight() - 20);
        tapToContinue.setX(Constant.SCREEN_WIDTH - tapToContinue.getWidth() - 20);
        Stage stage = new Stage(view, batch) {
            @Override
            public boolean keyDown(int keyCode) {
                PreferencesSB.clickedSound();
                if (keyCode == Input.Keys.BACK) {
                    num--;
                    if (num >= 0) {
//                        sprite.setTexture(textures.get(num));
                        inTransition = true;
                    } else {
                        app.setScreen(new ScreenMenu(app));
                    }
                    return true;
                }
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (TimeUtils.millis() - startTime < 500) {
                    return true;
                }
                num++;
                if (num < textures.size()) {
//                    sprite.setTexture(textures.get(num));
                    inTransition = true;
                } else {
                    switch (screen) {
                        case LEVEL1:
                            app.setScreen(new Level1(app));
                            break;
                        case LEVEL2:
                            app.setScreen(new Level2(app));
                            break;
                        case LEVEL3:
                            app.setScreen(new Level3(app));
                            break;
                        case ENDLESS:
                            app.setScreen(new EndlessScreen(app));
                            break;
                        case MINI1:
                            app.setScreen(new ScreenMinigame1(app, isStoryMode));
                            break;
                        case MINI2:
                            app.setScreen(new ScreenMinigame2(app, isStoryMode));
                            break;
                        case MINI3:
                            app.setScreen(new ScreenMinigame3(app, isStoryMode));
                            break;
                        case MINIGAMES:
                            app.setScreen(new ScreenMinigamesSelection(app, isStoryMode));
                            break;
                    }
                }
                return true;
            }
        };
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(stage);

        if (screen == Constant.Screens.MINIGAMES && PreferencesSB.getMinigameCount() > 0) {
            app.setScreen(new ScreenMinigamesSelection(app, isStoryMode));
        }
    }

    @Override
    public void render(float delta) {
        if (inTransition) {
            transitionTime += delta;
            if (transitionTime >= 0.5f) {
                sprite.setTexture(textures.get(num));
            }
            if (transitionTime >= 1f) {
                transitionTime = 0;
                inTransition = false;
            }
            clearScreen();
            batch.begin();
            sprite.setAlpha((MathUtils.cos(MathUtils.PI2 * transitionTime) + 1) / 2f);
            sprite.draw(batch);
            batch.end();
            return;
        }
        batch.begin();
        batch.draw(background, 0, 0);
        sprite.draw(batch);
        if (num == 0) {
            currentTime += delta;
            tapToContinue.setAlpha((MathUtils.cos(MathUtils.PI * currentTime) + 1) / 2f);
            tapToContinue.draw(batch);
        }
        batch.end();
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
