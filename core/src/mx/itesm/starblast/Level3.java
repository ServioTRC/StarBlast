package mx.itesm.starblast;

import com.badlogic.gdx.graphics.Texture;

class Level3 extends LevelStory {
    Level3(StarBlast app) {
        super(app,10,10,2,3);
        loopingBackground = Constant.MANAGER.get("GameScreen/Level1/LoopingBackground.jpg", Texture.class);
    }
}
