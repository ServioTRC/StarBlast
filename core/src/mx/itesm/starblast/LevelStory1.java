package mx.itesm.starblast;

import com.badlogic.gdx.graphics.Texture;

public class LevelStory1 extends LevelStory {
    LevelStory1(StarBlast app) {
        super(app,10,10,2);
        loopingBackground = Constant.MANAGER.get("PantallaJuego/Nivel 1/LoopingBackground.jpg", Texture.class);
    }
}
