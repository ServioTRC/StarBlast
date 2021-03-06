package mx.itesm.starblast.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import mx.itesm.starblast.Constant;

public abstract class ScreenSB implements Screen{


    OrthographicCamera camera;
    public Viewport view;

    public ScreenSB() {
        camera = new OrthographicCamera(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGTH);
        camera.position.set(Constant.SCREEN_WIDTH /2, Constant.SCREEN_HEIGTH /2, 0);
        camera.update();
        view = new StretchViewport(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGTH, camera);
    }

    void clearScreen() {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        view.update(width, height);
    }

    @Override
    public void hide() {
        dispose();
    }
}
