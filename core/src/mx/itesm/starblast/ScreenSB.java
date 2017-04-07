package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

abstract class ScreenSB implements Screen{


    OrthographicCamera camera;
    Viewport view;

    ScreenSB() {
        camera = new OrthographicCamera(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGTH);
        camera.position.set(Constants.SCREEN_WIDTH /2, Constants.SCREEN_HEIGTH /2, 0);
        camera.update();
        view = new StretchViewport(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGTH, camera);
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
