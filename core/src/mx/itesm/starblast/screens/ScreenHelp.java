package mx.itesm.starblast.screens;

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

import mx.itesm.starblast.Constant;
import mx.itesm.starblast.PreferencesSB;
import mx.itesm.starblast.StarBlast;

/**
 * Created by Ian Neumann on 02/05/2017.
 */

public class ScreenHelp extends ScreenSB{

    private StarBlast starBlast;
    private Image background;
    private Button tapToContinue;
    private Stage stageHUD;
    private SpriteBatch batch;

    public ScreenHelp(final StarBlast starBlast){
        super();
        this.starBlast = starBlast;
        batch = new SpriteBatch();
        stageHUD = new Stage(view,batch){
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    Gdx.app.log("ScreenOptions ", "Going to ScreenMenu");
                    PreferencesSB.clickedSound();
                    starBlast.setScreen(new ScreenOptions(starBlast));
                    return true;
                }
                return super.keyDown(keycode);
            }
        };
        background = new Image(Constant.MANAGER.get("HelpScreen/TutorialScreen.png",Texture.class));
        stageHUD.addActor(background);
        createTapToContinue();

        Gdx.input.setInputProcessor(stageHUD);
    }

    private void createTapToContinue() {
        Skin skin = new Skin();
        skin.add("Continue", Constant.MANAGER.get("HelpScreen/TutorialMask.png",Texture.class));

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("Continue");

        tapToContinue = new Button(style);
        tapToContinue.setPosition(0,0);

        tapToContinue.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                PreferencesSB.clickedSound();
                starBlast.setScreen(new ScreenLoading(starBlast, Constant.Screens.OPTIONS));
                return true;
            }
        });
        stageHUD.addActor(tapToContinue);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        stageHUD.draw();
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
