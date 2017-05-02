package mx.itesm.starblast.screens;

import com.badlogic.gdx.graphics.Texture;

import mx.itesm.starblast.Constant;
import mx.itesm.starblast.StarBlast;

public class Level2 extends LevelStory {
    Level2(StarBlast app) {
        super(app, 10, 10, 2, 3, 2);
        enemyBossTexture = Constant.MANAGER.get("GameScreen/EnemyBoss2Sprite.png", Texture.class);
    }
}
