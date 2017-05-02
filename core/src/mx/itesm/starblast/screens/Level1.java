package mx.itesm.starblast.screens;

import com.badlogic.gdx.graphics.Texture;

import mx.itesm.starblast.Constant;
import mx.itesm.starblast.StarBlast;

public class Level1 extends LevelStory {
    Level1(StarBlast app) {
        super(app, 10, 10, 2, 4, 1);
        enemyBossTexture = Constant.MANAGER.get("GameScreen/EnemyBoss1Sprite.png", Texture.class);
    }
}
