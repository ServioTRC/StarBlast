package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaSplashTec extends Pantalla {

	private final StarBlast menu;

	//Texturas
	private Texture texturaBtn;

	//SpriteBatch
	private SpriteBatch batch;

	//Escenas
	private Stage escenaSplashTec;

	//Cronómetro
	private long tiempoInicio;

	public PantallaSplashTec(StarBlast menu) {
		this.menu=menu;
	}

	@Override
	public void show() {
		cargarTexturas();
		crearObjetos();
		tiempoInicio = TimeUtils.millis();
	}

	private void crearObjetos() {
		batch = new SpriteBatch();
		escenaSplashTec = new Stage(vista, batch);

		TextureRegionDrawable trdBtnPlay = new TextureRegionDrawable(new TextureRegion(texturaBtn));
		ImageButton btnPlay = new ImageButton(trdBtnPlay);
		btnPlay.setPosition(Constantes.ANCHO_PANTALLA/2-btnPlay.getWidth()/2, Constantes.ALTO_PANTALLA/2-btnPlay.getHeight()/2);

		escenaSplashTec.addActor(btnPlay);

		btnPlay.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("PantallaSplashTec: ","Voy a PantallaInicio");
				menu.setScreen(new PantallaInicio(menu));
			}
		});

		Gdx.input.setInputProcessor(escenaSplashTec);
		Gdx.input.setCatchBackKey(true);
	}

	private void cargarTexturas() {
		texturaBtn = new Texture("LogoTec.jpg");
			}

	@Override
	public void render(float delta) {
		borrarPantalla();
		escenaSplashTec.draw();
		if((TimeUtils.millis() - tiempoInicio) > 2500){
			Gdx.app.log("Pantalla de Inicio: ","Voy a PantallaInicio");
			menu.setScreen(new PantallaInicio(menu));
		}
	}

	@Override
	public void resize(int width, int height) {
		vista.update(width, height);
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
