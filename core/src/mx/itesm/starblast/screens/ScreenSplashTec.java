package mx.itesm.starblast.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;

import mx.itesm.starblast.Constant;
import mx.itesm.starblast.StarBlast;

public class ScreenSplashTec extends ScreenSB {

	private final StarBlast menu;

	//Texturas
	private Texture textureBtn;

	//Escenas
	private Stage sceneSplashTec;

	//Cronómetro
	private long startingTime;

	public ScreenSplashTec(StarBlast menu) {
		this.menu=menu;
	}

	@Override
	public void show() {
		loadingTextures();
		creatingObjects();
		startingTime = TimeUtils.millis();
	}

	private void creatingObjects() {
		SpriteBatch batch = new SpriteBatch();
		sceneSplashTec = new Stage(view, batch);

		TextureRegionDrawable trdBtnPlay = new TextureRegionDrawable(new TextureRegion(textureBtn));
		ImageButton btnPlay = new ImageButton(trdBtnPlay);
		btnPlay.setPosition(Constant.SCREEN_WIDTH /2-btnPlay.getWidth()/2, Constant.SCREEN_HEIGTH /2-btnPlay.getHeight()/2);

		sceneSplashTec.addActor(btnPlay);

		btnPlay.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("ScreenSplashTec ","Going to ScreenStart");
				menu.setScreen(new ScreenStart(menu));
			}
		});

        Gdx.input.setInputProcessor(sceneSplashTec);
		Gdx.input.setCatchBackKey(true);
	}

	private void loadingTextures() {
		textureBtn = new Texture("LogoTec.jpg");
			}


	@Override
	public void render(float delta) {
		clearScreen();
		sceneSplashTec.draw();
		if((TimeUtils.millis() - startingTime) > 2500){
			Gdx.app.log("ScreenSplashTec ","Going to ScreenStart");
			menu.setScreen(new ScreenStart(menu));
		}
	}

	@Override
	public void resize(int width, int height) {
		view.update(width, height);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}
}
