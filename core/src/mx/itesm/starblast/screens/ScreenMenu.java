package mx.itesm.starblast.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import mx.itesm.starblast.Constant;
import mx.itesm.starblast.PreferencesSB;
import mx.itesm.starblast.StarBlast;
import mx.itesm.starblast.Text;

public class ScreenMenu extends ScreenSB {

    private final StarBlast menu;

    //Texturas
    private Texture backgroundTexture;

    //Escenas
    private Stage menuScene;

    //Text
    private Text text;
    private TextButton.TextButtonStyle textButtonStyle;

    public ScreenMenu(StarBlast menu) {
        this.menu=menu;
    }

    @Override
    public void show() {
        backgroundTexture = new Texture("MainScreen/MainScreen.jpg");
        creatingObjects();
    }

    private void creatingObjects() {
        SpriteBatch batch = new SpriteBatch();
        menuScene = new Stage(view, batch)
        {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    // DEBUG
                    Gdx.app.log("ScreenMenu","Going to ScreenStart");
                    
                    menu.setScreen(new ScreenStart(menu));
                    return true;
                }
                return super.keyDown(keycode);
            }
        };
        Image backgroundImg = new Image(backgroundTexture);
//        backgroundImg.setScale(Math.max(Constant.SCREEN_WIDTH/backgroundTexture.getWidth(),Constant.SCREEN_HEIGTH/backgroundTexture.getHeight()));
        menuScene.addActor(backgroundImg);
        text = new Text(Constant.SOURCE_TEXT);
        creatingButtons();

        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(menuScene);

    }

    //region crear botones

    private void creatingButtons() {
        creatingCreditsButton();
        creatingEndlessButton();
        creatingHistoryButton();
        creatingOptionsButton();
        creatingScoresButton();
        creatingMiniGamesButton();
    }

    private void creatingHistoryButton(){
        textButtonStyle = text.generateText(Color.BLUE,Color.GOLD,1);
        TextButton btnPlay = new TextButton("MODO HISTORIA", textButtonStyle);
        btnPlay.setPosition(Constant.SCREEN_WIDTH /2-btnPlay.getWidth()/2, Constant.SCREEN_HEIGTH /2-btnPlay.getHeight()/2+40);

        menuScene.addActor(btnPlay);

        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ScreenMenu ","Going to History mode");
                int level = PreferencesSB.readLevelProgress();
                if(level == 1) {
                    Gdx.app.log("ScreenMenu ", "Going to Level1");
                    menu.setScreen(new ScreenLoading(menu, Constant.Screens.LEVEL1));
                } else if (level == 2) {
                    Gdx.app.log("ScreenMenu ","Going to Level2");
                    menu.setScreen(new ScreenLoading(menu, Constant.Screens.LEVEL2));
                } else if (level >= 3) {
                    Gdx.app.log("ScreenMenu ", "Going to Level3");
                    menu.setScreen(new ScreenLoading(menu, Constant.Screens.LEVEL3));
                }
            }
        });
    }

    private void creatingEndlessButton(){
        final boolean active = PreferencesSB.readLevelProgress()>3;
        textButtonStyle = text.generateText(active?Color.BLUE:Color.GRAY,active?Color.GOLD:Color.GRAY,1);
        TextButton btnPlay = new TextButton("MODO ENDLESS", textButtonStyle);
        btnPlay.setPosition(Constant.SCREEN_WIDTH /2-btnPlay.getWidth()/2, 3* Constant.SCREEN_HEIGTH /8-btnPlay.getHeight()/2+80);

        menuScene.addActor(btnPlay);

        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ScreenMenu ","Going to Endless Mode");
                //TODO quitar || true
                if(active) menu.setScreen(new ScreenLoading(menu, Constant.Screens.ENDLESS));
            }
        });
    }

    private void creatingScoresButton(){
        textButtonStyle = text.generateText(Color.BLUE,Color.GOLD,1);
        TextButton btnPlay = new TextButton("PUNTAJES MAS ALTOS", textButtonStyle);
        btnPlay.setPosition(Constant.SCREEN_WIDTH /2-btnPlay.getWidth()/2, Constant.SCREEN_HEIGTH /3-btnPlay.getHeight()/2-25);

        menuScene.addActor(btnPlay);

        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ScreenMenu ","Going to Scores");
                //TODO Quitar o no la parte de cargar los assets en la pantalla opciones
                menu.setScreen(new ScreenLoading(menu, Constant.Screens.SCORES));
            }
        });
    }

    private void creatingOptionsButton(){
        textButtonStyle = text.generateText(Color.BLUE,Color.GOLD,1);
        TextButton btnPlay = new TextButton("OPCIONES", textButtonStyle);
        btnPlay.setPosition(Constant.SCREEN_WIDTH /2-btnPlay.getWidth()/2, 2* Constant.SCREEN_HEIGTH /8-btnPlay.getHeight()/2-30);

        menuScene.addActor(btnPlay);

        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ScreenMenu ","Going to Scores");

                menu.setScreen(new ScreenLoading(menu, Constant.Screens.OPTIONS));
            }
        });
    }

    private void creatingCreditsButton(){
        textButtonStyle = text.generateText(Color.BLUE,Color.GOLD,1);
        TextButton btnPlay = new TextButton("CREDITOS", textButtonStyle);
        btnPlay.setPosition(Constant.SCREEN_WIDTH /2-btnPlay.getWidth()/2, Constant.SCREEN_HEIGTH /8-btnPlay.getHeight()/2);
        menuScene.addActor(btnPlay);

        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ScreenMenu ","Going to Credits");
                
                menu.setScreen(new ScreenCredits(menu));
            }
        });
    }

    private void creatingMiniGamesButton(){
        textButtonStyle = text.generateText(Color.BLUE,Color.GOLD,1);
        TextButton btnPlay = new TextButton("MINIJUEGOS", textButtonStyle);
        btnPlay.setPosition(Constant.SCREEN_WIDTH /2-btnPlay.getWidth()/2, Constant.SCREEN_HEIGTH /3-btnPlay.getHeight()/2+40);
        menuScene.addActor(btnPlay);

        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ScreenMenu ","Going to MiniGames");
                menu.setScreen(new ScreenLoading(menu, Constant.Screens.MINIGAMES));
            }
        });
    }

    //endregion

    @Override
    public void render(float delta) {
        clearScreen();
        menuScene.draw();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
    }
}
