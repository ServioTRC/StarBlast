package mx.itesm.starblast;

import com.badlogic.gdx.graphics.Texture;

public class Nivel1 extends NivelHistoria {
    Nivel1(StarBlast app) {
        super(app,10,10,2);
        loopingBackground = Constants.MANAGER.get("PantallaJuego/Nivel 1/LoopingBackground.jpg", Texture.class);
    }
}
