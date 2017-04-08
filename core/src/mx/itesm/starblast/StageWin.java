package mx.itesm.starblast;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

class StageWin extends Stage {

    private final StarBlast app;

    StageWin(Viewport viewport, Batch batch, StarBlast menu) {
        super(viewport, batch);
        app = menu;

        Image img = new Image(Constants.MANAGER.get("PantallaJuego/SplashMisionCumplida.png", Texture.class));
        img.setPosition(Constants.SCREEN_WIDTH/2,Constants.SCREEN_HEIGTH-150,Align.center);
        addActor(img);

        Skin skin = new Skin();
        skin.add("Up", Constants.MANAGER.get("PantallaJuego/BotonSiguienteNivel.png", Texture.class));
        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("Up");
        Button btn = new Button(style);
        btn.setPosition(Constants.SCREEN_WIDTH /3-100, 150, Align.center);
        btn.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                 //TODO handle next level
            }
        });
        addActor(btn);

        skin = new Skin();
        skin.add("Up", Constants.MANAGER.get("PantallaJuego/BotonVolverMenu.png", Texture.class));
        style = new Button.ButtonStyle();
        style.up = skin.getDrawable("Up");
        btn = new Button(style);
        btn.setPosition(2 * Constants.SCREEN_WIDTH /3+100, 150, Align.center);
        btn.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                app.setScreen(new ScreenMenu(app));
            }
        });
        addActor(btn);
    }
}
