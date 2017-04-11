package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

class ScreenMinigamesSelection extends ScreenSB {
    //TODO texturas para cuando estas haciendo click en las diferentes opciones
    private final StarBlast menu;

    //SpriteBatch
    private SpriteBatch batch;

    //Escenas
    private Stage selectionScene;

    ScreenMinigamesSelection(StarBlast menu) {
        this.menu = menu;
    }

    @Override
    public void show() {
        creatingObjects();
    }

    private void creatingObjects() {
        batch = new SpriteBatch();
        selectionScene = new Stage(view, batch){
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    Gdx.app.log("ScreenMinigamesSelection ","Going to Screen Menu");
                    dispose();
                    menu.setScreen(new ScreenMenu(menu));
                    return true;
                }
                return super.keyDown(keycode);
            }
        };
        Image backgroundImage =
                new Image(Constant.MANAGER.get("PantallaSeleccionMinijuego/PantallaSeleccionMinijuego.png",Texture.class));
        selectionScene.addActor(backgroundImage);
        //TODO checar cual minijuego es cual
        createMinigame1Button();
        createMinigame2Button();
        createMinigame3Button();

        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(selectionScene);
    }

    private void createMinigame1Button() {
        Skin skin = new Skin();
        skin.add("up", Constant.MANAGER.get("PantallaSeleccionMinijuego/BotonMinijuego1.png",Texture.class));
        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("up");
        Button btn = new Button(style);
        btn.setPosition(Constant.SCREEN_WIDTH / 5 - btn.getWidth() / 2,
                        Constant.SCREEN_HEIGTH / 4 - btn.getHeight() / 2);
        selectionScene.addActor(btn);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ScreenMinigamesSelection ","Going to MiniGame1");
                dispose();
                menu.setScreen(new ScreenMinigame1(menu, false));
            }
        });
    }

    private void createMinigame2Button() {
        Skin skin = new Skin();
        skin.add("up", Constant.MANAGER.get("PantallaSeleccionMinijuego/BotonMinijuego2.png",Texture.class));
        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("up");
        Button btn = new Button(style);
        btn.setPosition(Constant.SCREEN_WIDTH / 2 - btn.getWidth() / 2,
                Constant.SCREEN_HEIGTH / 2 - btn.getHeight() / 2);
        selectionScene.addActor(btn);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ScreenMinigamesSelection ","Going to MiniGame2");
                //menu.setScreen(new PantallaJuego(menu));
            }
        });
    }

    private void createMinigame3Button() {
        Skin skin = new Skin();
        skin.add("up", Constant.MANAGER.get("PantallaSeleccionMinijuego/BotonMinijuego3.png",Texture.class));
        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("up");
        Button btn = new Button(style);
        btn.setPosition(Constant.SCREEN_WIDTH - Constant.SCREEN_WIDTH / 5 - btn.getWidth() / 2,
                Constant.SCREEN_HEIGTH / 4 - btn.getHeight() / 2);
        selectionScene.addActor(btn);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ScreenMinigamesSelection ","Going to MiniGame3");
                //menu.setScreen(new PantallaJuego(menu));
            }
        });
    }

    @Override
    public void render(float delta) {
        clearScreen();
        selectionScene.draw();
        batch.begin();

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