package mx.itesm.starblast.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.TimeUtils;

import mx.itesm.starblast.Constant;
import mx.itesm.starblast.PreferencesSB;
import mx.itesm.starblast.gameEntities.ProgressBar;
import mx.itesm.starblast.StarBlast;

public class ScreenLoading extends ScreenSB {

    StarBlast menu;
    private Constant.Screens nextScreen;
    private ProgressBar loadingBar;
    private Stage loadingScene;
    private Stage textScene;
    private Texture[] texts;
    private final int TIME_PER_TEXT = 1000;
    private final int NUMBER_OF_TEXTS = 5;
    private final int TOTAL_TIME;
    private boolean story;

    public ScreenLoading(StarBlast menu, Constant.Screens screens, boolean story) {
        this(menu, screens);
        this.story = story;
    }

    public ScreenLoading(StarBlast menu, Constant.Screens screens) {
        this.menu = menu;
        nextScreen = screens;

        SpriteBatch batch = new SpriteBatch();
        loadingScene = new Stage(view, batch);
        textScene = new Stage(view, batch);

        loadingTexts();
        TOTAL_TIME = TIME_PER_TEXT * NUMBER_OF_TEXTS;
    }

    private void loadingTexts() {
        texts = new Texture[NUMBER_OF_TEXTS];
        for (int i = 1; i <= NUMBER_OF_TEXTS; i++) {
            texts[i - 1] = new Texture("LoadingScreen/Text" + i + "Loading.png");
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
        if (Constant.MANAGER.update()) {
            goToNextScreen();
        }
    }

    private void showRightText() {
        for (Actor a : textScene.getActors()) {
            a.setVisible(false);
        }
        textScene.getActors().get(((int) (TimeUtils.millis() % TOTAL_TIME)) / 1000).setVisible(true);
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
        for (Texture tex : texts) {
            img = new Image(tex);
            img.setPosition((Constant.SCREEN_WIDTH - img.getWidth()) / 2, Constant.SCREEN_HEIGTH / 3);
            textScene.addActor(img);
        }
    }

    private void creatingProgressBar() {
        loadingBar = new ProgressBar(new Texture("LoadingScreen/MaskLoading.png"), false, false);
        loadingBar.setFrame(new Texture("LoadingScreen/BackgroundLoading.jpg"));
        loadingBar.setPosition((Constant.SCREEN_WIDTH - loadingBar.getWidth()) / 2, (Constant.SCREEN_HEIGTH - loadingBar.getHeight()) / 2);

        loadingScene.addActor(loadingBar);
    }
    //endregion

    private void goToNextScreen() {
        switch (nextScreen) {

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
                menu.setScreen(new EndlessScreen(menu));
                break;
            case MINIGAMES:
                menu.setScreen(new ScreenMinigamesSelection(menu, story));
                break;
            case MINI1:
                if (Gdx.app.getPreferences("Minigames").getBoolean("1", false)) {
                    menu.setScreen(new ScreenMinigame1(menu, story));
                } else {
                    menu.setScreen(new ScreenTutoMG(menu, story, 1));
                }
                break;
            case MINI2:
                if (Gdx.app.getPreferences("Minigames").getBoolean("2", false)) {
                    menu.setScreen(new ScreenMinigame2(menu, story));
                } else {
                    menu.setScreen(new ScreenTutoMG(menu, story, 2));
                }
                break;
            case MINI3:
                if (Gdx.app.getPreferences("Minigames").getBoolean("3", false)) {
                    menu.setScreen(new ScreenMinigame3(menu, story));
                } else {
                    menu.setScreen(new ScreenTutoMG(menu, story, 3));
                }
                break;
            case SCORES:
                menu.setScreen(new ScreenScores(menu));
                break;
            case OPTIONS:
                menu.setScreen(new ScreenOptions(menu));
                break;
            case HELP:
                menu.setScreen(new ScreenHelp(menu));
            case CREDITS:
                break;
        }
    }

    private void loadNextScreen() {
        switch (nextScreen) {

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
            case NEXT_LEVEL:
                switch (PreferencesSB.readLevelProgress()) {
                    case 1:
                        nextScreen = Constant.Screens.LEVEL1;
                        break;
                    case 2:
                        nextScreen = Constant.Screens.LEVEL2;
                        break;
                    case 3:
                        nextScreen = Constant.Screens.LEVEL3;
                        break;
                    default:
                        PreferencesSB.unlockMinigames();
                        nextScreen = Constant.Screens.ENDLESS;
                        break;
                }
                loadNextScreen();
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
            case HELP:
                loadHelp();
            case CREDITS:
                break;
        }
    }

    private void loadLevel1() {
        loadStory();
        Constant.MANAGER.load("GameScreen/EnemyBoss1Sprite.png", Texture.class);
    }

    private void loadLevel2() {
        loadStory();
        Constant.MANAGER.load("GameScreen/EnemyBoss2Sprite.png", Texture.class);
    }

    private void loadLevel3() {
        loadStory();
        Constant.MANAGER.load("GameScreen/EnemyBoss3Sprite.png", Texture.class);
    }

    private void loadStory() {
        Constant.MANAGER.load("GameScreen/AvatarSprite.png", Texture.class);
        Constant.MANAGER.load("GameScreen/ButtonBack.png", Texture.class);
        Constant.MANAGER.load("GameScreen/BulletSprite.png", Texture.class);
        Constant.MANAGER.load("GameScreen/MissileSprite.png", Texture.class);
        Constant.MANAGER.load("GameScreen/BulletSpriteEnemy.png", Texture.class);
        Constant.MANAGER.load("GameScreen/BulletSpritePowered.png", Texture.class);
        Constant.MANAGER.load("GameScreen/ButtonNextLevel.png", Texture.class);
        Constant.MANAGER.load("GameScreen/DroidHelperSprite.png", Texture.class);
        Constant.MANAGER.load("GameScreen/ShieldSprite.png", Texture.class);
        Constant.MANAGER.load("GameScreen/Enemy1Sprite.png", Texture.class);
        Constant.MANAGER.load("GameScreen/Enemy2Sprite.png", Texture.class);
        Constant.MANAGER.load("GameScreen/Enemy3Sprite.png", Texture.class);
        Constant.MANAGER.load("GameScreen/Pause.png", Texture.class);
        Constant.MANAGER.load("GameScreen/SpriteStageClear.png", Texture.class);
        Constant.MANAGER.load("Animations/ExplosionFrames.png", Texture.class);
        Constant.MANAGER.load("Animations/ExplosionMissileFrames.png", Texture.class);

        Constant.MANAGER.load("DefeatScreen/DefeatBackground.jpg", Texture.class);
        Constant.MANAGER.load("DefeatScreen/Countdown.png", Texture.class);

        loadSoundsFromStory();
        loadPowerups();
        loadHUD();
        loadPause();
        loadBackgrounds();
    }

    private void loadPowerups() {
        Constant.MANAGER.load("GameScreen/PowerupHealthSprite.png", Texture.class);
        Constant.MANAGER.load("GameScreen/PowerupShieldSprite.png", Texture.class);
        Constant.MANAGER.load("GameScreen/PowerupSpeedSprite.png", Texture.class);
        Constant.MANAGER.load("GameScreen/PowerupDamageSprite.png", Texture.class);
        Constant.MANAGER.load("GameScreen/PowerupMissileSprite.png", Texture.class);


    }

    private void loadBackgrounds() {
        Constant.MANAGER.load("GameScreen/FondoBegin.jpg", Texture.class);
        Constant.MANAGER.load("GameScreen/FondoEnd.jpg", Texture.class);
        Constant.MANAGER.load("GameScreen/FondoTile2.jpg", Texture.class);
        Constant.MANAGER.load("GameScreen/FondoTile3.jpg", Texture.class);
        Constant.MANAGER.load("GameScreen/FondoTile4.jpg", Texture.class);
        Constant.MANAGER.load("GameScreen/FondoTile5.jpg", Texture.class);
        Constant.MANAGER.load("GameScreen/FondoTile6.jpg", Texture.class);
        Constant.MANAGER.load("GameScreen/FondoTile7.jpg", Texture.class);
        Constant.MANAGER.load("GameScreen/FondoTile8.jpg", Texture.class);
        Constant.MANAGER.load("GameScreen/FondoTileGrande.jpg", Texture.class);
        Constant.MANAGER.load("GameScreen/FondoTileGrande2.jpg", Texture.class);
        Constant.MANAGER.load("GameScreen/FondoTileGrande3.jpg", Texture.class);
    }

    private void loadSoundsFromStory() {
        Constant.MANAGER.load("SoundEffects/ShootingSound1.mp3", Sound.class);
        Constant.MANAGER.load("SoundEffects/ShootingSound2.mp3", Sound.class);
        Constant.MANAGER.load("SoundEffects/Explosion1.mp3", Sound.class);
        Constant.MANAGER.load("SoundEffects/Explosion2.mp3", Sound.class);
        //Constant.MANAGER.load("SoundEffects/Explosion3.mp3",Sound.class);
        Constant.MANAGER.load("SoundEffects/MissileSound.wav", Sound.class);
    }

    private void loadHUD() {
        Constant.MANAGER.load("HUD/ButtonAPress.png", Texture.class);
        Constant.MANAGER.load("HUD/ButtonAStandby.png", Texture.class);
        Constant.MANAGER.load("HUD/ButtonBPress.png", Texture.class);
        Constant.MANAGER.load("HUD/ButtonBStandby.png", Texture.class);
        Constant.MANAGER.load("HUD/JoystickPad.png", Texture.class);
        Constant.MANAGER.load("HUD/JoystickStick.png", Texture.class);
        Constant.MANAGER.load("HUD/LifeBarBar.png", Texture.class);
        Constant.MANAGER.load("HUD/LifeBarFrame.png", Texture.class);
    }

    private void loadPause() {
        Constant.MANAGER.load("SettingsScreen/SettingsWindow.png", Texture.class);
        Constant.MANAGER.load("SettingsScreen/ButtonReset.png", Texture.class);
        Constant.MANAGER.load("SettingsScreen/ButtonResetYellow.png", Texture.class);
        Constant.MANAGER.load("SettingsScreen/ButtonCodes.png", Texture.class);
        Constant.MANAGER.load("SettingsScreen/Back.png", Texture.class);
        Constant.MANAGER.load("SettingsScreen/BackYellow.png", Texture.class);
        Constant.MANAGER.load("SettingsScreen/ButtonBack.png", Texture.class);
        Constant.MANAGER.load("SettingsScreen/ButtonBackYellow.png", Texture.class);
        Constant.MANAGER.load("SettingsScreen/ButtonSound.png", Texture.class);
        Constant.MANAGER.load("SettingsScreen/ButtonSoundOff.png", Texture.class);
        Constant.MANAGER.load("SettingsScreen/ButtonMusic.png", Texture.class);
        Constant.MANAGER.load("SettingsScreen/ButtonMusicOff.png", Texture.class);
    }

    private void loadEndless() {
        loadStory();
        Constant.MANAGER.load("DefeatScreen/ArcadeDefeatBackground.jpg", Texture.class);
    }

    private void loadMinigames() {
        Constant.MANAGER.load("MinigameSelectionScreen/ButtonMinigame1.png", Texture.class);
        Constant.MANAGER.load("MinigameSelectionScreen/ButtonMinigame2.png", Texture.class);
        Constant.MANAGER.load("MinigameSelectionScreen/ButtonMinigame3.png", Texture.class);
        Constant.MANAGER.load("MinigameSelectionScreen/ButtonMinigame1Grey.png", Texture.class);
        Constant.MANAGER.load("MinigameSelectionScreen/ButtonMinigame2Grey.png", Texture.class);
        Constant.MANAGER.load("MinigameSelectionScreen/ButtonMinigame3Grey.png", Texture.class);
        Constant.MANAGER.load("MinigameSelectionScreen/MinigameSelectionBackground.png", Texture.class);
    }

    private void loadMinigame1() {
        Constant.MANAGER.load("Minigame1Screen/Minigame1Background.jpg", Texture.class);
        Constant.MANAGER.load("SettingsScreen/Back.png", Texture.class);
        Constant.MANAGER.load("SettingsScreen/BackYellow.png", Texture.class);
        Constant.MANAGER.load("Minigame1Screen/SplashMinigame1Win.png", Texture.class);
        Constant.MANAGER.load("Minigame1Screen/SplashMinigameLoss.png", Texture.class);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Constant.MANAGER.load("Minigame1Screen/PuzzlePieces/Pieza" + String.format("%c", 'A' + i) + (j + 1) + ".png", Texture.class);
            }
        }

        if (!Gdx.app.getPreferences("Minigames").getBoolean("1", false)) {
            Constant.MANAGER.load("Minigame1Screen/SplashTutorial1.png", Texture.class);
            Constant.MANAGER.load("Minigame1Screen/SplashTutorial2.png", Texture.class);
        }
    }

    private void loadMinigame2() {
        Constant.MANAGER.load("Minigame2Screen/BackgroundMinigame2.jpg", Texture.class);
        Constant.MANAGER.load("SettingsScreen/Back.png", Texture.class);
        Constant.MANAGER.load("SettingsScreen/BackYellow.png", Texture.class);
        Constant.MANAGER.load("Minigame2Screen/TopBanner.png", Texture.class);
        Constant.MANAGER.load("Minigame2Screen/SplashMinigame2Win.png", Texture.class);
        Constant.MANAGER.load("Minigame1Screen/SplashMinigameLoss.png", Texture.class);
        Constant.MANAGER.load("Minigame2Screen/BadCollectible.png", Texture.class);
        Constant.MANAGER.load("Minigame2Screen/Collectible1.png", Texture.class);
        Constant.MANAGER.load("Minigame2Screen/Collectible3.png", Texture.class);
        Constant.MANAGER.load("Minigame2Screen/Collectible3.png", Texture.class);
        Constant.MANAGER.load("Minigame2Screen/BottomBanner.png", Texture.class);
        Constant.MANAGER.load("Minigame2Screen/ErrorAnimation.png", Texture.class);
        Constant.MANAGER.load("SoundEffects/Explosion1.mp3", Sound.class);
        Constant.MANAGER.load("SoundEffects/PowerupPickupSound.wav", Sound.class);

        if (!Gdx.app.getPreferences("Minigames").getBoolean("2", false)) {
            Constant.MANAGER.load("Minigame2Screen/SplashTutorial1.png", Texture.class);
            Constant.MANAGER.load("Minigame2Screen/SplashTutorial2.png", Texture.class);
        }
    }

    private void loadMinigame3() {
        Constant.MANAGER.load("Minigame3Screen/Minigame3Background.jpg", Texture.class);
        Constant.MANAGER.load("SettingsScreen/Back.png", Texture.class);
        Constant.MANAGER.load("SettingsScreen/BackYellow.png", Texture.class);
        Constant.MANAGER.load("Minigame3Screen/SplashMinigame3Win.png", Texture.class);
        Constant.MANAGER.load("Minigame1Screen/SplashMinigameLoss.png", Texture.class);
        Constant.MANAGER.load("Minigame3Screen/Minigame3Background.jpg", Texture.class);
        Constant.MANAGER.load("SoundEffects/RockDiggingSound.wav", Sound.class);
        Constant.MANAGER.load("SoundEffects/SelectionSound.mp3", Sound.class);
        Constant.MANAGER.load("Minigame3Screen/RockSpriteNew.png", Texture.class);
        Constant.MANAGER.load("Minigame3Screen/RockSpriteDamaged.png", Texture.class);
        Constant.MANAGER.load("Minigame3Screen/RockSpriteVeryDamaged.png", Texture.class);
        Constant.MANAGER.load("Minigame3Screen/Minigame3Crystal.png", Texture.class);
        Constant.MANAGER.load("Minigame3Screen/RockBreakingAnimation.png", Texture.class);

        if (!Gdx.app.getPreferences("Minigames").getBoolean("2", false)) {
            Constant.MANAGER.load("Minigame3Screen/SplashTutorial1.png", Texture.class);
            Constant.MANAGER.load("Minigame3Screen/SplashTutorial2.png", Texture.class);
        }
    }

    private void loadOptions() {
    }

    private void loadHelp() {
        Constant.MANAGER.load("HelpScreen/TutorialMask.png", Texture.class);
        Constant.MANAGER.load("HelpScreen/TutorialMaskOptions.png", Texture.class);
        Constant.MANAGER.load("HelpScreen/TutorialScreen.png", Texture.class);
    }
}
