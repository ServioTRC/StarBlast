package mx.itesm.starblast;

import com.badlogic.gdx.graphics.Texture;

class Level1 extends LevelStory {
    Level1(StarBlast app) {
        super(app,10,10,2, 1);
        loopingBackground = Constant.MANAGER.get("GameScreen/Level1/LoopingBackground.jpg", Texture.class);
    }
}
