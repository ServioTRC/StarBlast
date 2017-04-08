package mx.itesm.starblast;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.TimeUtils;

class PantallaCargando extends ScreenSB {

    StarBlast menu;
    Constants.Pantallas nextScreen;
    private ProgressBar barraCargando;
    private SpriteBatch batch;
    private Stage escenaCargando;
    private Stage escenaTextos;
    private Texture[] textos;
    private final int TIEMPO_POR_TEXTO = 1000;
    private final int NUMERO_DE_TEXTOS = 5;
    private final int TIEMPO_TOTAL;

    PantallaCargando(StarBlast menu, Constants.Pantallas pantalla){
        this.menu = menu;
        nextScreen = pantalla;

        batch = new SpriteBatch();
        escenaCargando = new Stage(view,batch);
        escenaTextos = new Stage(view,batch);

        cargarTextos();
        TIEMPO_TOTAL = TIEMPO_POR_TEXTO*NUMERO_DE_TEXTOS;
    }

    private void cargarTextos() {
        textos = new Texture[NUMERO_DE_TEXTOS];
        for(int i = 1; i <=NUMERO_DE_TEXTOS;i++){
            textos[i-1] = new Texture("PantallaCargando/Texto"+i+"Cargando.png");
        }
    }

    //region metodos pantalla

    @Override
    public void show() {
        crearProgressBar();
        crearTextos();
        loadNextScreen();
    }


    @Override
    public void render(float delta) {
        barraCargando.setPorcentage(Constants.MANAGER.getProgress());
        escenaCargando.draw();
        mostrarTextoCorrecto();
        escenaTextos.draw();
        if(Constants.MANAGER.update()){
           goToNextScreen();
        }
    }

    private void mostrarTextoCorrecto() {
        for(Actor a:escenaTextos.getActors()){
            a.setVisible(false);
        }
        escenaTextos.getActors().get(((int)(TimeUtils.millis()%TIEMPO_TOTAL))/1000).setVisible(true);
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

    //endregion

    //region Metodos Show
    private void crearTextos() {
        Image img;
        for(Texture tex : textos){
            img = new Image(tex);
            img.setPosition((Constants.SCREEN_WIDTH -img.getWidth())/2, Constants.SCREEN_HEIGTH /3);
            escenaTextos.addActor(img);
        }
    }

    private void crearProgressBar() {
        barraCargando = new ProgressBar(new Texture("PantallaCargando/MascaraCargando.png"),false);
        barraCargando.setFrame(new Texture("PantallaCargando/FondoCargando.jpg"));
        barraCargando.setPosition((Constants.SCREEN_WIDTH -barraCargando.getWidth())/2,(Constants.SCREEN_HEIGTH -barraCargando.getHeight())/2);

        escenaCargando.addActor(barraCargando);
    }
    //endregion

    private void goToNextScreen() {
        switch (nextScreen){

            case SPLASH:
                break;
            case INICIO:
                break;
            case MENU:
                break;
            case HISTORIA:
//                menu.setScreen(new NivelPrueba(menu));
                break;
            case NIVEL1:
                menu.setScreen(new Nivel1(menu));
                break;
            case ENDLESS:
                menu.setScreen(new NivelPrueba(menu));
                break;
            case MINIJUEGOS:
                menu.setScreen(new PantallaSeleccionMinijuegos(menu));
                break;
            case MINI1:
                menu.setScreen(new PantallaMinijuego1(menu, false));
                break;
            case MINI2:
                break;
            case MINI3:
                break;
            case PUNTAJES:
                menu.setScreen(new PantallaPuntajes(menu));
                break;
            case OPCIONES:
                menu.setScreen(new PantallaOpciones(menu));
                break;
            case CREDITOS:
                break;
        }
    }

    private void loadNextScreen() {
        switch (nextScreen){

            case SPLASH:
                break;
            case INICIO:
                break;
            case MENU:
                break;
            case HISTORIA:
                cargarHistoria();
                break;
            case NIVEL1:
                cargarNivel1();
                break;
            case ENDLESS:
                cargarEndless();
                break;
            case MINIJUEGOS:
                cargarMinijuegos();
                break;
            case MINI1:
                cargarMinijuego1();
                break;
            case MINI2:
                cargarMinijuego2();
                break;
            case MINI3:
                cargarMinijuego3();
                break;
            case PUNTAJES:
                break;
            case OPCIONES:
                cargarOpciones();
            case CREDITOS:
                break;
        }
    }

    private void cargarNivel1() {
        Constants.MANAGER.load("PantallaJuego/Nivel 1/LoopingBackground.jpg", Texture.class);
        cargarHistoria();
    }

    private void cargarHistoria() {
        Constants.MANAGER.load("PantallaJuego/AvatarSprite.png", Texture.class);
        Constants.MANAGER.load("PantallaJuego/BotonVolverMenu.png", Texture.class);
        Constants.MANAGER.load("PantallaJuego/BotonSiguienteNivel.png", Texture.class);
        Constants.MANAGER.load("PantallaJuego/BulletSprite.png", Texture.class);
        Constants.MANAGER.load("PantallaJuego/BulletSpriteEnemigo.png", Texture.class);
        Constants.MANAGER.load("PantallaJuego/DroidHelperSprite.png", Texture.class);
        Constants.MANAGER.load("PantallaJuego/Enemigo1Sprite.png", Texture.class);
        Constants.MANAGER.load("PantallaJuego/Enemigo2Sprite.png", Texture.class);
        Constants.MANAGER.load("PantallaJuego/Enemigo3Sprite.png", Texture.class);
        Constants.MANAGER.load("PantallaJuego/NaveJefe.png", Texture.class);
        Constants.MANAGER.load("PantallaJuego/Pausa.png", Texture.class);
        Constants.MANAGER.load("PantallaJuego/PowerupSprite.png", Texture.class);
        Constants.MANAGER.load("PantallaJuego/SplashMisionCumplida.png", Texture.class);
        Constants.MANAGER.load("Animaciones/ExplosionNaveFrames.png", Texture.class);

        Constants.MANAGER.load("PantallaPerder/FondoDerribado.jpg", Texture.class);
        Constants.MANAGER.load("PantallaPerder/Countdown.png", Texture.class);

        cargarSonidosHistoria();
        cargarHUD();
        cargarPausa();
    }

    private void cargarSonidosHistoria(){
        Constants.MANAGER.load("EfectosSonoros/SonidoDisparo1.mp3", Sound.class);
        Constants.MANAGER.load("EfectosSonoros/SonidoDisparo2.mp3", Sound.class);
    }

    private void cargarHUD(){
        Constants.MANAGER.load("HUD/BotonAPresionado.png",Texture.class);
        Constants.MANAGER.load("HUD/BotonAStandby.png",Texture.class);
        Constants.MANAGER.load("HUD/BotonBPresionado.png",Texture.class);
        Constants.MANAGER.load("HUD/BotonBStandby.png",Texture.class);
        Constants.MANAGER.load("HUD/JoystickPad.png",Texture.class);
        Constants.MANAGER.load("HUD/JoystickStick.png",Texture.class);
        Constants.MANAGER.load("HUD/LifeBarBar.png",Texture.class);
        Constants.MANAGER.load("HUD/LifeBarFrame.png",Texture.class);
    }

    private void cargarPausa(){
        Constants.MANAGER.load("PantallaOpciones/CuadroOpciones.png", Texture.class);
        Constants.MANAGER.load("PantallaOpciones/BotonReset.png", Texture.class);
        Constants.MANAGER.load("PantallaOpciones/BotonResetYellow.png", Texture.class);
        Constants.MANAGER.load("PantallaOpciones/BotonCodigos.png", Texture.class);
        Constants.MANAGER.load("PantallaOpciones/Back.png", Texture.class);
        Constants.MANAGER.load("PantallaOpciones/BackYellow.png", Texture.class);
        Constants.MANAGER.load("PantallaOpciones/BotonSonido.png", Texture.class);
        Constants.MANAGER.load("PantallaOpciones/BotonNoSonido.png", Texture.class);
        Constants.MANAGER.load("PantallaOpciones/BotonMusica.png", Texture.class);
        Constants.MANAGER.load("PantallaOpciones/BotonNoMusica.png", Texture.class);
    }

    private void cargarEndless() {
        cargarHistoria();
        //TODO quitar esto
        Constants.MANAGER.load("PantallaJuego/FondoNivel2.jpg",Texture.class);
    }

    private void cargarMinijuegos() {
        Constants.MANAGER.load("PantallaSeleccionMinijuego/BotonMinijuego1.png",Texture.class);
        Constants.MANAGER.load("PantallaSeleccionMinijuego/BotonMinijuego2.png",Texture.class);
        Constants.MANAGER.load("PantallaSeleccionMinijuego/BotonMinijuego3.png",Texture.class);
        Constants.MANAGER.load("PantallaSeleccionMinijuego/PantallaSeleccionMinijuego.png",Texture.class);
    }

    private void cargarMinijuego1() {
    }

    private void cargarMinijuego2() {
    }

    private void cargarMinijuego3() {
    }

    private void cargarOpciones() {
    }
}
