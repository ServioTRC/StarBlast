package mx.itesm.starblast.screens;

import com.badlogic.gdx.graphics.Texture;

import mx.itesm.starblast.Constant;
import mx.itesm.starblast.StarBlast;

public class Level3 extends LevelStory {
    Level3(StarBlast app) {
        super(app,10,10,2,3);
        loopingBackground = Constant.MANAGER.get("GameScreen/Level1/LoopingBackground.jpg", Texture.class);
    }
}
