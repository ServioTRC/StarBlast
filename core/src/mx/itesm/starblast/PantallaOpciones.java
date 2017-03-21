package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class PantallaOpciones extends Pantalla {
    private final StarBlast menu;

    //Texturas
    private Texture texturaFondo;

    //SpriteBatch
    private SpriteBatch batch;

    //Escenas
    private Stage escenaOpciones;

    //Texto
    private Texto texto;
    private TextButton.TextButtonStyle textButtonStyle;

    //Sprite
    private GeneralSprite spriteSonido;
    private GeneralSprite spriteMusica;
    private GeneralSprite spriteAyuda;
    private GeneralSprite spriteCodigos;
    private GeneralSprite spriteReiniciar;
    private GeneralSprite spriteBack;

    private final String[] stringsBotonMusica = {"PantallaOpciones/BotonMusica.png","PantallaOpciones/BotonNoMusica.png"};
    private final String[] stringsBotonSonido = {"PantallaOpciones/BotonSonido.png","PantallaOpciones/BotonNoSonido.png"};


    public PantallaOpciones(StarBlast menu) {
        this.menu=menu;
    }

    @Override
    public void show() {
        cargarTexturas();
        crearObjetos();
    }

    private void crearObjetos() {
        batch = new SpriteBatch();
        escenaOpciones = new Stage(vista, batch);
        Image imgFondo = new Image(texturaFondo);
        escenaOpciones.addActor(imgFondo);
        texto = new Texto(Constantes.TEXTO_FUENTE);
        //crearBotonAtras();
        crearSprites();
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(new ProcesadorEntrada());
    }

    private void crearSprites(){
        spriteAyuda = new GeneralSprite("PantallaOpciones/BotonAyuda.png",3*Constantes.ANCHO_PANTALLA/4,
                Constantes.ALTO_PANTALLA/2-25);
        spriteMusica = new GeneralSprite(stringsBotonMusica[Preferencias.MUSICA_HABILITADA?0:1],3*Constantes.ANCHO_PANTALLA/4,
                Constantes.ALTO_PANTALLA/2+80);
        spriteSonido = new GeneralSprite(stringsBotonSonido[Preferencias.SONIDO_HABILITADO?0:1],3*Constantes.ANCHO_PANTALLA/4,
                2*Constantes.ALTO_PANTALLA/3+40);
        spriteCodigos = new GeneralSprite("PantallaOpciones/BotonCodigos.png",3*Constantes.ANCHO_PANTALLA/4,
                Constantes.ALTO_PANTALLA/3+10);
        spriteReiniciar = new GeneralSprite("PantallaOpciones/BotonReset.png",3*Constantes.ANCHO_PANTALLA/4,
                Constantes.ALTO_PANTALLA/6+20);
        spriteBack = new GeneralSprite("PantallaOpciones/Back.png",12*Constantes.ANCHO_PANTALLA/13,
                Constantes.ALTO_PANTALLA/8);
    }

    private void cargarTexturas() {
        texturaFondo = new Texture("PantallaOpciones/PantallaOpciones.jpg");
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        escenaOpciones.draw();
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        spriteAyuda.draw(batch);
        spriteCodigos.draw(batch);
        spriteSonido.draw(batch);
        spriteReiniciar.draw(batch);
        spriteMusica.draw(batch);
        spriteBack.draw(batch);
        batch.end();
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
        Constantes.ASSET_GENERAL.dispose();
    }

    private class ProcesadorEntrada extends InputAdapter {
        private Vector3 vector;
        @Override
        public boolean keyDown(int keycode) {
            Gdx.app.log("Pantalla Creditos: ","Voy al Menu");
            menu.setScreen(new PantallaMenu(menu));
            return true;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            vector = new Vector3(screenX,screenY,0);
            vector = camara.unproject(vector);
            if(spriteBack.isTouched(vector.x, vector.y)) {
                spriteBack.setTexture("PantallaOpciones/BackYellow.png");
            }
            return true;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            vector = new Vector3(screenX,screenY,0);
            vector = camara.unproject(vector);
            if(spriteSonido.isTouched(vector.x, vector.y)){
                Preferencias.SONIDO_HABILITADO = !Preferencias.SONIDO_HABILITADO;
                spriteSonido.setTexture(stringsBotonSonido[Preferencias.SONIDO_HABILITADO?0:1]);
                Preferencias.escribirPreferenciasSonidos();
            } else if(spriteMusica.isTouched(vector.x, vector.y)){
                Preferencias.MUSICA_HABILITADA = !Preferencias.MUSICA_HABILITADA;
                if(Preferencias.MUSICA_HABILITADA){
                    Gdx.app.log("Pantalla Opciones", "Play a Música");
                    menu.playMusica();
                }else{
                    menu.pauseMusica();
                }
                spriteMusica.setTexture(stringsBotonMusica[Preferencias.MUSICA_HABILITADA?0:1]);
                Preferencias.escribirPreferenciasSonidos();
            } else if(spriteAyuda.isTouched(vector.x, vector.y)){

            } else if(spriteCodigos.isTouched(vector.x, vector.y)){
                Input.TextInputListener textListener = new Input.TextInputListener()
                {
                    @Override
                    public void input(String input)
                    {
                        Gdx.app.log("Codigo Ingresado: ",input);
                        Constantes.CODIGOS.add(input);
                    }

                    @Override
                    public void canceled()
                    {
                        Gdx.app.log("Codigo Ingresado: ", "Salida del teclado");
                    }
                };

                Gdx.input.getTextInput(textListener, "Ingresar Código: ", "", "");
            } else if(spriteReiniciar.isTouched(vector.x, vector.y)){

            } else if(spriteBack.isTouched(vector.x, vector.y)){
                Gdx.app.log("Pantalla Creditos: ","Voy a pantalla menu");
                menu.setScreen(new PantallaMenu(menu));
            } else {
                spriteBack.setTexture("PantallaOpciones/Back.png");
            }
            return true;
        }
    }

}

