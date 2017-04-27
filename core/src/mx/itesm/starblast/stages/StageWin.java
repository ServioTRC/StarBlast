package mx.itesm.starblast.stages;

import com.badlogic.gdx.Gdx;
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

import mx.itesm.starblast.Constant;
import mx.itesm.starblast.PreferencesSB;
import mx.itesm.starblast.StarBlast;
import mx.itesm.starblast.screens.ScreenMenu;

public class StageWin extends Stage {

    private final StarBlast app;

    public StageWin(Viewport viewport, Batch batch, StarBlast menu) {
        super(viewport, batch);
        app = menu;

        Image img = new Image(Constant.MANAGER.get("GameScreen/SpriteStageClear.png", Texture.class));
        img.setPosition(Constant.SCREEN_WIDTH/2,Constant.SCREEN_HEIGTH-150,Align.center);
        addActor(img);

        Skin skin = new Skin();
        skin.add("Up", Constant.MANAGER.get("GameScreen/ButtonNextLevel.png", Texture.class));
        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("Up");
        Button btn = new Button(style);
        btn.setPosition(Constant.SCREEN_WIDTH /3-100, 150, Align.center);
        btn.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                 //TODO handle next level
                int level = PreferencesSB.readingLevelProgress();
                if(level == 1) {
                    Gdx.app.log("ScreenMenu ", "Going to Level1");
                    app.setScreen(new mx.itesm.starblast.screens.ScreenLoading(app, Constant.Screens.LEVEL1));
                } else if (level == 2) {
                    Gdx.app.log("ScreenMenu ","Going to Level2");
                    app.setScreen(new mx.itesm.starblast.screens.ScreenLoading(app, Constant.Screens.LEVEL2));
                } else if (level == 3) {
                    Gdx.app.log("ScreenMenu ", "Going to Level3");
                    app.setScreen(new mx.itesm.starblast.screens.ScreenLoading(app, Constant.Screens.LEVEL3));
                }
            }
        });
        addActor(btn);

        skin = new Skin();
        skin.add("Up", Constant.MANAGER.get("GameScreen/ButtonBack.png", Texture.class));
        style = new Button.ButtonStyle();
        style.up = skin.getDrawable("Up");
        btn = new Button(style);
        btn.setPosition(2 * Constant.SCREEN_WIDTH /3+100, 150, Align.center);
        btn.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                app.setScreen(new ScreenMenu(app));
            }
        });
        addActor(btn);
    }
}
