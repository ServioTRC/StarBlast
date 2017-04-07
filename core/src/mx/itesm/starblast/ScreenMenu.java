package mx.itesm.starblast;

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

class ScreenMenu extends ScreenSB {

    private final StarBlast menu;

    //Texturas
    private Texture backgroundTexture;

    //Escenas
    private Stage menuScene;

    //Text
    private Text text;
    private TextButton.TextButtonStyle textButtonStyle;

    ScreenMenu(StarBlast menu) {
        this.menu=menu;
    }

    @Override
    public void show() {
        backgroundTexture = new Texture("PantallaMenu/PantallaMenu.jpg");
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
        Image imgFondo = new Image(backgroundTexture);
        menuScene.addActor(imgFondo);
        text = new Text(Constants.SOURCE_TEXT);
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
        btnPlay.setPosition(Constants.SCREEN_WIDTH /2-btnPlay.getWidth()/2, Constants.SCREEN_HEIGTH /2-btnPlay.getHeight()/2+40);

        menuScene.addActor(btnPlay);

        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ScreenMenu ","Going to History mode");
                menu.setScreen(new PantallaCargando(menu, Constants.Pantallas.NIVEL1));
            }
        });
    }

    private void creatingEndlessButton(){
        textButtonStyle = text.generateText(Color.GRAY,Color.GRAY,1);
        TextButton btnPlay = new TextButton("MODO ENDLESS", textButtonStyle);
        btnPlay.setPosition(Constants.SCREEN_WIDTH /2-btnPlay.getWidth()/2, 3* Constants.SCREEN_HEIGTH /8-btnPlay.getHeight()/2+80);

        menuScene.addActor(btnPlay);

        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ScreenMenu ","Going to Endless Mode");
                menu.setScreen(new PantallaCargando(menu, Constants.Pantallas.ENDLESS));
            }
        });
    }

    private void creatingScoresButton(){
        textButtonStyle = text.generateText(Color.BLUE,Color.GOLD,1);
        TextButton btnPlay = new TextButton("PUNTAJES MAS ALTOS", textButtonStyle);
        btnPlay.setPosition(Constants.SCREEN_WIDTH /2-btnPlay.getWidth()/2, Constants.SCREEN_HEIGTH /3-btnPlay.getHeight()/2-25);

        menuScene.addActor(btnPlay);

        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ScreenMenu ","Going to Scores");
                
                menu.setScreen(new PantallaCargando(menu, Constants.Pantallas.PUNTAJES));
            }
        });
    }

    private void creatingOptionsButton(){
        textButtonStyle = text.generateText(Color.BLUE,Color.GOLD,1);
        TextButton btnPlay = new TextButton("OPCIONES", textButtonStyle);
        btnPlay.setPosition(Constants.SCREEN_WIDTH /2-btnPlay.getWidth()/2, 2* Constants.SCREEN_HEIGTH /8-btnPlay.getHeight()/2-30);

        menuScene.addActor(btnPlay);

        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ScreenMenu ","Going to Scores");
                
                menu.setScreen(new PantallaCargando(menu, Constants.Pantallas.OPCIONES));
            }
        });
    }

    private void creatingCreditsButton(){
        textButtonStyle = text.generateText(Color.BLUE,Color.GOLD,1);
        TextButton btnPlay = new TextButton("CREDITOS", textButtonStyle);
        btnPlay.setPosition(Constants.SCREEN_WIDTH /2-btnPlay.getWidth()/2, Constants.SCREEN_HEIGTH /8-btnPlay.getHeight()/2);
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
        btnPlay.setPosition(Constants.SCREEN_WIDTH /2-btnPlay.getWidth()/2, Constants.SCREEN_HEIGTH /3-btnPlay.getHeight()/2+40);
        menuScene.addActor(btnPlay);

        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ScreenMenu ","Going to MiniGames");
                
                menu.setScreen(new PantallaCargando(menu, Constants.Pantallas.MINIJUEGOS));
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
