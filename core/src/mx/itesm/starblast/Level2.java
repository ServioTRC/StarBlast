package mx.itesm.starblast;

import com.badlogic.gdx.graphics.Texture;

class Level2 extends LevelStory {
    Level2(StarBlast app) {
        super(app,10,10,2, 2);
        loopingBackground = Constant.MANAGER.get("PantallaJuego/Nivel 1/LoopingBackground.jpg", Texture.class);
    }
}
