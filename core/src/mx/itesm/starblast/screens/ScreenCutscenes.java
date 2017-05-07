package mx.itesm.starblast.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

import mx.itesm.starblast.PreferencesSB;
import mx.itesm.starblast.StarBlast;

class ScreenCutscenes extends ScreenSB {

    private int level;
    StarBlast app;
    private ArrayList<Texture> textures = new ArrayList<Texture>();
    private SpriteBatch batch;
    private int num = 0;
    private Sprite sprite;
    private long startTime;

    ScreenCutscenes(StarBlast app, int level) {
        super();
        this.app = app;
        this.level = level;
        //TODO kp2 con el ending
        switch (level) {
            case 1:
                for (int i = 1; i < 6; i++) {
                    textures.add(new Texture("StoryScreen/Intro/Level1_Story" + i + ".jpg"));
                }
                textures.add(new Texture("StoryScreen/Intro/Mision1.jpg"));
                break;
            case 2:
                for (int i = 1; i < 3; i++) {
                    textures.add(new Texture("StoryScreen/Level 2/Level2_Story" + i + ".jpg"));
                }
                textures.add(new Texture("StoryScreen/Level 2/Mision2.jpg"));
                break;
            case 3:
                for (int i = 1; i < 3; i++) {
                    textures.add(new Texture("StoryScreen/Level 3/Level3_Story" + i + ".jpg"));
                }
                textures.add(new Texture("StoryScreen/Level 3/Mision3.jpg"));
                break;
            case 4:
                for (int i = 1; i < 3; i++) {
                    textures.add(new Texture("StoryScreen/Ending/Ending" + i + ".jpg"));
                }
                textures.add(new Texture("StoryScreen/Ending/TheEnd.jpg"));
                textures.add(new Texture("StoryScreen/Ending/EndlessIntro.jpg"));
                break;
            default:
                app.setScreen(new ScreenMenu(app));
                break;
        }
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        sprite = new Sprite(textures.get(0));
        startTime = TimeUtils.millis();
        Stage stage = new Stage(view, batch) {
            @Override
            public boolean keyDown(int keyCode) {
                PreferencesSB.clickedSound();
                if (keyCode == Input.Keys.BACK) {
                    num--;
                    if (num >= 0) {
                        sprite.setTexture(textures.get(num));
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
                    sprite.setTexture(textures.get(num));
                } else {
                    switch (level) {
                        case 1:
                            app.setScreen(new Level1(app));
                            break;
                        case 2:
                            app.setScreen(new Level2(app));
                            break;
                        case 3:
                            app.setScreen(new Level3(app));
                            break;
                        case 4:
                            app.setScreen(new EndlessScreen(app));
                            break;
                        default:
                            app.setScreen(new ScreenMenu(app));
                            break;
                    }
                }
                return true;
            }
        };
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        batch.begin();
        sprite.draw(batch);
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
