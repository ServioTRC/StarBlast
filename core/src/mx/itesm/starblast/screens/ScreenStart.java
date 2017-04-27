package mx.itesm.starblast.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import mx.itesm.starblast.Constant;
import mx.itesm.starblast.StarBlast;

class ScreenStart extends ScreenSB {

    private final StarBlast menu;

    //Texturas
    private Texture backgroundTexture;

    //SpriteBatch
    private SpriteBatch batch;

    //Escenas
    private Stage startScene;

    //Sprite
    private Sprite sprite;

    private float alphaChange = 1/120f;
    private float alpha = 1f;
    private int count = 0;

    ScreenStart(StarBlast menu) {
        this.menu=menu;
    }

    @Override
    public void show() {
        loadingTextures();
        creatingObjects();
    }

    private void creatingObjects() {
        batch = new SpriteBatch();
        startScene = new Stage(view, batch);
        Image imgFondo = new Image(backgroundTexture);
        startScene.addActor(imgFondo);
        sprite = new Sprite(new Texture("SplashScreen/TAP.png"));
        sprite.setCenter(Constant.SCREEN_WIDTH /2,1* Constant.SCREEN_HEIGTH /4);
        Gdx.input.setCatchBackKey(false);
        Gdx.input.setInputProcessor(new ScreenStart.InputProcessorSB());
    }


    private void loadingTextures() {
        backgroundTexture = new Texture("SplashScreen/SplashBackground.jpg");
    }

    @Override
    public void render(float delta) {
        clearScreen();
        startScene.draw();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        sprite.draw(batch);
        sprite.setAlpha(alpha);
        if(count >= 120){
            alphaChange *= -1;
            count = 0;
        }
        alpha -= alphaChange;
        count++;
        batch.end();
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

    }


    private class InputProcessorSB implements InputProcessor {

        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            Gdx.app.log("ScreenStart ","Going to ScreenMenu");
            menu.setScreen(new ScreenMenu(menu));
            return true;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }
    }


}