package mx.itesm.starblast;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.TimeUtils;

class ScreenLoading extends ScreenSB {

    StarBlast menu;
    Constant.Screens nextScreen;
    private ProgressBar loadingBar;
    private SpriteBatch batch;
    private Stage loadingScene;
    private Stage textScene;
    private Texture[] texts;
    private final int TIME_PER_TEXT = 1000;
    private final int NUMBER_OF_TEXTS = 5;
    private final int TOTAL_TIME;

    ScreenLoading(StarBlast menu, Constant.Screens screens){
        this.menu = menu;
        nextScreen = screens;

        batch = new SpriteBatch();
        loadingScene = new Stage(view,batch);
        textScene = new Stage(view,batch);

        loadingTexts();
        TOTAL_TIME = TIME_PER_TEXT * NUMBER_OF_TEXTS;
    }

    private void loadingTexts() {
        texts = new Texture[NUMBER_OF_TEXTS];
        for(int i = 1; i <= NUMBER_OF_TEXTS; i++){
            texts[i-1] = new Texture("LoadingScreen/Text"+i+"Loading.png");
        }
    }

    //region metodos pantalla

    @Override
    public void show() {
        creatingProgressBar();
        creatingTexts();
        loadNextScreen();
    }


    @Override
    public void render(float delta) {
        loadingBar.setPorcentage(Constant.MANAGER.getProgress());
        loadingScene.draw();
        showRightText();
        textScene.draw();
        if(Constant.MANAGER.update()){
           goToNextScreen();
        }
    }

    private void showRightText() {
        for(Actor a: textScene.getActors()){
            a.setVisible(false);
        }
        textScene.getActors().get(((int)(TimeUtils.millis()% TOTAL_TIME))/1000).setVisible(true);
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

    //endregion

    //region Metodos Show
    private void creatingTexts() {
        Image img;
        for(Texture tex : texts){
            img = new Image(tex);
            img.setPosition((Constant.SCREEN_WIDTH -img.getWidth())/2, Constant.SCREEN_HEIGTH /3);
            textScene.addActor(img);
        }
    }

    private void creatingProgressBar() {
        loadingBar = new ProgressBar(new Texture("LoadingScreen/MaskLoading.png"),false);
        loadingBar.setFrame(new Texture("LoadingScreen/BackgroundLoading.jpg"));
        loadingBar.setPosition((Constant.SCREEN_WIDTH - loadingBar.getWidth())/2,(Constant.SCREEN_HEIGTH - loadingBar.getHeight())/2);

        loadingScene.addActor(loadingBar);
    }
    //endregion

    private void goToNextScreen() {
        switch (nextScreen){

            case SPLASH:
                break;
            case START:
                break;
            case MENU:
                break;
            case LEVEL1:
                menu.setScreen(new Level1(menu));
                break;
            case LEVEL2:
                menu.setScreen(new Level2(menu));
                break;
            case LEVEL3:
                menu.setScreen(new Level3(menu));
                break;
            case ENDLESS:
                menu.setScreen(new LevelProof(menu));
                break;
            case MINIGAMES:
                menu.setScreen(new ScreenMinigamesSelection(menu));
                break;
            case MINI1:
                menu.setScreen(new ScreenMinigame1(menu, false));
                break;
            case MINI2:
                break;
            case MINI3:
                break;
            case SCORES:
                menu.setScreen(new ScreenScores(menu));
                break;
            case OPTIONS:
                menu.setScreen(new ScreenOptions(menu));
                break;
            case CREDITS:
                break;
        }
    }

    private void loadNextScreen() {
        switch (nextScreen){

            case SPLASH:
                break;
            case START:
                break;
            case MENU:
                break;
            case LEVEL1:
                loadLevel1();
                break;
            case LEVEL2:
                loadLevel2();
                break;
            case LEVEL3:
                loadLevel3();
                break;
            case ENDLESS:
                loadEndless();
                break;
            case MINIGAMES:
                loadMinigames();
                break;
            case MINI1:
                loadMinigame1();
                break;
            case MINI2:
                loadMinigame2();
                break;
            case MINI3:
                loadMinigame3();
                break;
            case SCORES:
                break;
            case OPTIONS:
                loadOptions();
            case CREDITS:
                break;
        }
    }

    private void loadLevel1() {
        Constant.MANAGER.load("GameScreen/Level1/LoopingBackground.jpg", Texture.class);
        loadStory();
    }

    private void loadLevel2() {
        Constant.MANAGER.load("GameScreen/Level1/LoopingBackground.jpg", Texture.class);
        loadStory();
    }

    private void loadLevel3() {
        Constant.MANAGER.load("GameScreen/Level1/LoopingBackground.jpg", Texture.class);
        loadStory();
    }

    private void loadStory() {
        Constant.MANAGER.load("GameScreen/AvatarSprite.png", Texture.class);
        Constant.MANAGER.load("GameScreen/ButtonBack.png", Texture.class);
        Constant.MANAGER.load("GameScreen/BulletSprite.png", Texture.class);
        Constant.MANAGER.load("GameScreen/ButtonNextLevel.png", Texture.class);
        Constant.MANAGER.load("GameScreen/BulletSpriteEnemy.png", Texture.class);
        Constant.MANAGER.load("GameScreen/DroidHelperSprite.png", Texture.class);
        Constant.MANAGER.load("GameScreen/Enemy1Sprite.png", Texture.class);
        Constant.MANAGER.load("GameScreen/Enemy2Sprite.png", Texture.class);
        Constant.MANAGER.load("GameScreen/Enemy3Sprite.png", Texture.class);
        Constant.MANAGER.load("GameScreen/EnemyBossSprite.png", Texture.class);
        Constant.MANAGER.load("GameScreen/Pause.png", Texture.class);
        Constant.MANAGER.load("GameScreen/PowerupSprite.png", Texture.class);
        Constant.MANAGER.load("GameScreen/SpriteStageClear.png", Texture.class);
        Constant.MANAGER.load("Animations/ExplosionFrames.png", Texture.class);

        Constant.MANAGER.load("DeleteScreen/DefeatBackground.jpg", Texture.class);
        Constant.MANAGER.load("DeleteScreen/Countdown.png", Texture.class);

        loadSoundsFromStory();
        loadHUD();
        loadPause();
    }

    private void loadSoundsFromStory(){
        Constant.MANAGER.load("SoundEffects/ShootingSound1.mp3", Sound.class);
        Constant.MANAGER.load("SoundEffects/ShootingSound2.mp3", Sound.class);
        Constant.MANAGER.load("SoundEffects/Explosion1.mp3",Sound.class);
        Constant.MANAGER.load("SoundEffects/Explosion2.mp3",Sound.class);
        Constant.MANAGER.load("SoundEffects/Explosion3.mp3",Sound.class);
    }

    private void loadHUD(){
        Constant.MANAGER.load("HUD/ButtonAPress.png",Texture.class);
        Constant.MANAGER.load("HUD/ButtonAStandby.png",Texture.class);
        Constant.MANAGER.load("HUD/ButtonBPress.png",Texture.class);
        Constant.MANAGER.load("HUD/ButtonBStandby.png",Texture.class);
        Constant.MANAGER.load("HUD/JoystickPad.png",Texture.class);
        Constant.MANAGER.load("HUD/JoystickStick.png",Texture.class);
        Constant.MANAGER.load("HUD/LifeBarBar.png",Texture.class);
        Constant.MANAGER.load("HUD/LifeBarFrame.png",Texture.class);
    }

    private void loadPause(){
        Constant.MANAGER.load("SettingsScreen/SettingsWindow.png", Texture.class);
        Constant.MANAGER.load("SettingsScreen/ButtonReset.png", Texture.class);
        Constant.MANAGER.load("SettingsScreen/ButtonResetYellow.png", Texture.class);
        Constant.MANAGER.load("SettingsScreen/ButtonCodes.png", Texture.class);
        Constant.MANAGER.load("SettingsScreen/Back.png", Texture.class);
        Constant.MANAGER.load("SettingsScreen/BackYellow.png", Texture.class);
        Constant.MANAGER.load("SettingsScreen/ButtonSound.png", Texture.class);
        Constant.MANAGER.load("SettingsScreen/ButtonSoundOff.png", Texture.class);
        Constant.MANAGER.load("SettingsScreen/ButtonMusic.png", Texture.class);
        Constant.MANAGER.load("SettingsScreen/ButtonMusicOff.png", Texture.class);
    }

    private void loadEndless() {
        loadStory();
        //TODO quitar esto
        Constant.MANAGER.load("GameScreen/BackgroundLevel2.jpg",Texture.class);
    }

    private void loadMinigames() {
        Constant.MANAGER.load("MinigameSelectionScreen/ButtonMinigame1.png",Texture.class);
        Constant.MANAGER.load("MinigameSelectionScreen/ButtonMinigame2.png",Texture.class);
        Constant.MANAGER.load("MinigameSelectionScreen/ButtonMinigame3.png",Texture.class);
        Constant.MANAGER.load("MinigameSelectionScreen/MinigameSelectionBackground.png",Texture.class);
    }

    private void loadMinigame1() {
    }

    private void loadMinigame2() {
    }

    private void loadMinigame3() {
    }

    private void loadOptions() {
    }
}
