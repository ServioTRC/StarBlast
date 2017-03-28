package mx.itesm.starblast;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

class PantallaCargando extends Pantalla {

    StarBlast menu;
    Constantes.Pantallas nextScreen;

    PantallaCargando(StarBlast menu, Constantes.Pantallas pantalla){
        this.menu = menu;
        nextScreen = pantalla;
    }

    //region metodos pantalla

    @Override
    public void show() {
        //TODO progress bar o wtv
        loadNextScreen();
    }

    @Override
    public void render(float delta) {
        //TODO leer arriba
        borrarPantalla();
        if(Constantes.MANAGER.update()){
            goToNextScreen();
        }
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

    private void goToNextScreen() {
        switch (nextScreen){

            case SPLASH:
                break;
            case INICIO:
                break;
            case MENU:
                break;
            case HISTORIA:
                menu.setScreen(new NivelPrueba(menu));
            case ENDLESS:
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

    private void cargarHistoria() {
        //NIVEL
        Constantes.MANAGER.load("PantallaJuego/AvatarSprite.png", Texture.class);
        Constantes.MANAGER.load("PantallaJuego/BotonVolverMenu.png", Texture.class);
        Constantes.MANAGER.load("PantallaJuego/BulletSprite.png", Texture.class);
        Constantes.MANAGER.load("PantallaJuego/BulletSpriteEnemigo.png", Texture.class);
        Constantes.MANAGER.load("PantallaJuego/DroidHelperSprite.png", Texture.class);
        Constantes.MANAGER.load("PantallaJuego/Enemigo1Sprite.png", Texture.class);
        Constantes.MANAGER.load("PantallaJuego/Enemigo2Sprite.png", Texture.class);
        Constantes.MANAGER.load("PantallaJuego/Enemigo3Sprite.png", Texture.class);
        Constantes.MANAGER.load("PantallaJuego/FondoNivel2.jpg", Texture.class);
        Constantes.MANAGER.load("PantallaJuego/NaveJefe.png", Texture.class);
        Constantes.MANAGER.load("PantallaJuego/Pausa.png", Texture.class);
        Constantes.MANAGER.load("PantallaJuego/PowerupSprite.png", Texture.class);
        Constantes.MANAGER.load("PantallaJuego/SplashMisionCumplida.png", Texture.class);
        //SONIDOS
        Constantes.MANAGER.load("EfectosSonoros/SonidoDisparo1.mp3", Sound.class);
        Constantes.MANAGER.load("EfectosSonoros/SonidoDisparo2.mp3", Sound.class);
//        Constantes.MANAGER.load("EfectosSonoros/MusicaFondo.mp3", Sound.class);
        //HUD
        Constantes.MANAGER.load("HUD/BotonAPresionado.png",Texture.class);
        Constantes.MANAGER.load("HUD/BotonAStandby.png",Texture.class);
        Constantes.MANAGER.load("HUD/BotonBPresionado.png",Texture.class);
        Constantes.MANAGER.load("HUD/BotonBStandby.png",Texture.class);
        Constantes.MANAGER.load("HUD/JoystickPad.png",Texture.class);
        Constantes.MANAGER.load("HUD/JoystickStick.png",Texture.class);
        Constantes.MANAGER.load("HUD/LifeBarBar.png",Texture.class);
        Constantes.MANAGER.load("HUD/LifeBarFrame.png",Texture.class);
        //PAUSA
        Constantes.MANAGER.load("PantallaOpciones/CuadroOpciones.png", Texture.class);
        Constantes.MANAGER.load("PantallaOpciones/BotonReset.png", Texture.class);
        Constantes.MANAGER.load("PantallaOpciones/BotonResetYellow.png", Texture.class);
        Constantes.MANAGER.load("PantallaOpciones/BotonCodigos.png", Texture.class);
        Constantes.MANAGER.load("PantallaOpciones/Back.png", Texture.class);
        Constantes.MANAGER.load("PantallaOpciones/BackYellow.png", Texture.class);
        Constantes.MANAGER.load("PantallaOpciones/BotonSonido.png", Texture.class);
        Constantes.MANAGER.load("PantallaOpciones/BotonNoSonido.png", Texture.class);
        Constantes.MANAGER.load("PantallaOpciones/BotonMusica.png", Texture.class);
        Constantes.MANAGER.load("PantallaOpciones/BotonNoMusica.png", Texture.class);
    }

    private void cargarEndless() {
        cargarHistoria();
    }

    private void cargarMinijuegos() {
        Constantes.MANAGER.load("PantallaSeleccionMinijuego/BotonMinijuego1.png",Texture.class);
        Constantes.MANAGER.load("PantallaSeleccionMinijuego/BotonMinijuego2.png",Texture.class);
        Constantes.MANAGER.load("PantallaSeleccionMinijuego/BotonMinijuego3.png",Texture.class);
        Constantes.MANAGER.load("PantallaSeleccionMinijuego/PantallaSeleccionMinijuego.png",Texture.class);
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
