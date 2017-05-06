package mx.itesm.starblast.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

import mx.itesm.starblast.PreferencesSB;
import mx.itesm.starblast.StarBlast;

public class ScreenTutoMG extends ScreenSB {

    private StarBlast menu;
    private Texture backgroundTexture;
    private Array<Texture> tutorialTextures;
    private Sprite tutorial;
    private SpriteBatch batch;
    private Stage tutorialMG;
    private boolean isStoryMode;
    private int numImage = 0;
    private int numMG;


    public ScreenTutoMG(StarBlast menu, boolean isStoryMode, int numMG) {
        this.menu = menu;
        this.isStoryMode = isStoryMode;
        this.numMG = numMG;
        tutorialTextures = new Array<Texture>();
        if(PreferencesSB.getMinigameCount() == 0){
            tutorialTextures.add(new Texture("MinigameSelectionScreen/SplashTutorial1.png"));
        }
    }

    @Override
    public void show() {
        loadTextures();
        createObjects();
    }

    private void createObjects() {
        Image imgFondo = new Image(backgroundTexture);
        batch = new SpriteBatch();
        tutorialMG = new Stage(view, batch) {
            @Override
            public boolean keyDown(int keycode) {
                PreferencesSB.clickedSound();
                if (keycode == Input.Keys.BACK) {
                    numImage--;
                    if (numImage < 0) {
                        menu.setScreen(isStoryMode ? new ScreenMenu(menu) : new ScreenMinigamesSelection(menu, isStoryMode));
                    }
                    else{
                        tutorial.setTexture(tutorialTextures.get(numImage));
                    }
                    return true;
                }
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                PreferencesSB.clickedSound();
                numImage++;
                if(numImage >= tutorialTextures.size){
                    switch (numMG){
                        case 1:
                            menu.setScreen(new ScreenMinigame1(menu,isStoryMode));
                            break;
                        case 2:
                            menu.setScreen(new ScreenMinigame2(menu,isStoryMode));
                            break;
                        case 3:
                            menu.setScreen(new ScreenMinigame3(menu,isStoryMode));
                            break;
                        default:
                            break;
                    }
                }
                else{
                    tutorial.setTexture(tutorialTextures.get(numImage));
                }
                return true;
            }
        };
        tutorialMG.addActor(imgFondo);
        tutorial = new Sprite(tutorialTextures.get(0));
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(tutorialMG);
    }

    private void loadTextures() {
        backgroundTexture = new Texture("HighScoresScreen/BackgroundHighScores.jpg");
        if (numMG == 1) {
            tutorialTextures.add(new Texture("Minigame1Screen/SplashTutorial1.png"));
            tutorialTextures.add(new Texture("Minigame1Screen/SplashTutorial2.png"));
        } else if (numMG == 2) {
            tutorialTextures.add(new Texture("Minigame2Screen/SplashTutorial1.png"));
            tutorialTextures.add(new Texture("Minigame2Screen/SplashTutorial2.png"));
        } else if (numMG == 3) {
            tutorialTextures.add(new Texture("Minigame3Screen/SplashTutorial1.png"));
            tutorialTextures.add(new Texture("Minigame3Screen/SplashTutorial2.png"));
        }
    }

    @Override
    public void render(float delta) {
        clearScreen();
        tutorialMG.draw();
        batch.begin();
        tutorial.draw(batch);
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
