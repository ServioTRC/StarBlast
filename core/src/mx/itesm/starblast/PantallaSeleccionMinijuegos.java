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

class PantallaSeleccionMinijuegos extends ScreenSB {
    //TODO texturas para cuando estas haciendo click en las diferentes opciones
    private final StarBlast menu;

    //SpriteBatch
    private SpriteBatch batch;

    //Escenas
    private Stage escenaSeleccion;

    PantallaSeleccionMinijuegos(StarBlast menu) {
        this.menu = menu;
    }

    @Override
    public void show() {
        crearObjetos();
    }

    private void crearObjetos() {
        batch = new SpriteBatch();
        escenaSeleccion = new Stage(view, batch){
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    Gdx.app.log("PantallaSeleccionMinijuegos: ","Voy al Menu");
                    dispose();
                    menu.setScreen(new ScreenMenu(menu));
                    return true;
                }
                return super.keyDown(keycode);
            }
        };
        Image imgFondo = new Image(Constants.MANAGER.get("PantallaSeleccionMinijuego/PantallaSeleccionMinijuego.png",Texture.class));
        escenaSeleccion.addActor(imgFondo);
        //TODO checar cual minijuego es cual
        crearBotonMinijuego1();
        crearBotonMinijuego2();
        crearBotonMinijuego3();

        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(escenaSeleccion);
    }

    private void crearBotonMinijuego1() {
        Skin skin = new Skin();
        skin.add("up", Constants.MANAGER.get("PantallaSeleccionMinijuego/BotonMinijuego1.png",Texture.class));
        Button.ButtonStyle estilo = new Button.ButtonStyle();
        estilo.up = skin.getDrawable("up");
        Button btn = new Button(estilo);
        btn.setPosition(Constants.SCREEN_WIDTH / 5 - btn.getWidth() / 2,
                        Constants.SCREEN_HEIGTH / 4 - btn.getHeight() / 2);
        escenaSeleccion.addActor(btn);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("PantallaSeleccionMinijuegos: ", "Voy a pantalla minijuego1");
                dispose();
                menu.setScreen(new PantallaMinijuego1(menu, false));
            }
        });
    }

    private void crearBotonMinijuego2() {
        Skin skin = new Skin();
        skin.add("up", Constants.MANAGER.get("PantallaSeleccionMinijuego/BotonMinijuego2.png",Texture.class));
        Button.ButtonStyle estilo = new Button.ButtonStyle();
        estilo.up = skin.getDrawable("up");
        Button btn = new Button(estilo);
        btn.setPosition(Constants.SCREEN_WIDTH / 2 - btn.getWidth() / 2,
                Constants.SCREEN_HEIGTH / 2 - btn.getHeight() / 2);
        escenaSeleccion.addActor(btn);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("PantallaSeleccionMinijuegos: ", "Voy a pantalla minijuego2");
                //menu.setScreen(new PantallaJuego(menu));
            }
        });
    }

    private void crearBotonMinijuego3() {
        Skin skin = new Skin();
        skin.add("up", Constants.MANAGER.get("PantallaSeleccionMinijuego/BotonMinijuego3.png",Texture.class));
        Button.ButtonStyle estilo = new Button.ButtonStyle();
        estilo.up = skin.getDrawable("up");
        Button btn = new Button(estilo);
        btn.setPosition(Constants.SCREEN_WIDTH - Constants.SCREEN_WIDTH / 5 - btn.getWidth() / 2,
                Constants.SCREEN_HEIGTH / 4 - btn.getHeight() / 2);
        escenaSeleccion.addActor(btn);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("PantallaSeleccionMinijuegos: ", "Voy a pantalla minijuego3");
                //menu.setScreen(new PantallaJuego(menu));
            }
        });
    }

    @Override
    public void render(float delta) {
        clearScreen();
        escenaSeleccion.draw();
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
