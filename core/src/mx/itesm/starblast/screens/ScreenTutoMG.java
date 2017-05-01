package mx.itesm.starblast.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.TimeUtils;

import mx.itesm.starblast.Constant;
import mx.itesm.starblast.StarBlast;
import mx.itesm.starblast.Text;

/**
 * Created by Servio T on 30/04/2017.
 */

class ScreenTutoMG extends ScreenSB implements InputProcessor{

    private StarBlast menu;
    private Texture backgroundTexture;
    private Texture tutorial1;
    private Texture tutorial2;
    private Sprite tutorial;
    private SpriteBatch batch;
    private Stage tutorialMG1;
    private boolean isStoryMode;
    private int numImage = 1;
    private long timeBetween;
    private long startingTime;
    private int numMG;

    ScreenTutoMG(StarBlast menu, boolean isStoryMode, int numMG) {
        this.menu=menu;
        this.isStoryMode = isStoryMode;
        this.numMG = numMG;
    }

    @Override
    public void show() {
        loadingTextures();
        creatingObjects();
    }

    private void creatingObjects() {
        Image imgFondo = new Image(backgroundTexture);
        batch = new SpriteBatch();
        tutorialMG1 = new Stage(view, batch);
        tutorialMG1.addActor(imgFondo);
        tutorial = new Sprite(tutorial1);
        startingTime = TimeUtils.millis();
    }

    private void loadingTextures() {
        backgroundTexture = new Texture("HighScoresScreen/BackgroundHighScores.jpg");
        if(numMG == 1){
            tutorial1 = new Texture("Minigame1Screen/SplashTutorial1.png");
            tutorial2 = new Texture("Minigame1Screen/SplashTutorial2.png");
        }
        else if(numMG == 2){
            tutorial1 = new Texture("Minigame2Screen/SplashTutorial1.png");
            tutorial2 = new Texture("Minigame2Screen/SplashTutorial2.png");
        } else if(numMG == 3){
            tutorial1 = new Texture("Minigame3Screen/SplashTutorial1.png");
            tutorial2 = new Texture("Minigame3Screen/SplashTutorial2.png");
        }
    }

    @Override
    public void render(float delta) {
        clearScreen();
        tutorialMG1.draw();
        batch.begin();
        tutorial.draw(batch);
        if(((TimeUtils.millis()-startingTime) > 5000)&&(numImage == 1)){
            tutorial.setTexture(tutorial2);
            numImage = 2;
        }
        if((TimeUtils.millis()-startingTime) > 10000){
            if(numMG == 1)
                menu.setScreen(new ScreenMinigame1(menu, isStoryMode));
            else if(numMG == 2)
                menu.setScreen(new ScreenMinigame2(menu, isStoryMode));
            //else if(numMG == 3)
                //menu.setScreen(new ScreenMinigame2(menu, isStoryMode));
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

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK) {
            if(isStoryMode){
                //TODO better handling of back on story mode
                Gdx.app.log("ScreenMinigame1: ","Es historia y no hago nada");
                return true;
            }
            Gdx.app.log("ScreenMinigame1: ","Going to minigames selection");
            menu.setScreen(new ScreenMinigamesSelection(menu));
            return true;
        }
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

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Gdx.app.log("ScreenMinigame1: ","Touch");
        if(numImage == 1){
            tutorial.setTexture(tutorial2);
            numImage = 2;
            timeBetween = TimeUtils.millis();
        }
        if((numImage == 2)&&((TimeUtils.millis()-timeBetween)>=1000)) {
            if (numMG == 1)
                menu.setScreen(new ScreenMinigame1(menu, isStoryMode));
            else if (numMG == 2)
                menu.setScreen(new ScreenMinigame2(menu, isStoryMode));
            //else if(numMG == 3)
            //menu.setScreen(new ScreenMinigame2(menu, isStoryMode));
        }
        return true;
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