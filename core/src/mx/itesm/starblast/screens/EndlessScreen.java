package mx.itesm.starblast.screens;

import com.badlogic.gdx.Gdx;

import mx.itesm.starblast.StarBlast;
import mx.itesm.starblast.stages.StageLostEndless;

public class EndlessScreen extends LevelStory {

    private final StageLostEndless lostWonStage;
    private int wavesSinceChange = 3;
    private float time;
    private boolean scoreSet = false;

    EndlessScreen(StarBlast app) {
        super(app, 5, 2, 0, 3, 0);
        lostWonStage = new StageLostEndless(view, batch, app);
        Gdx.app.getPreferences("Levels").putBoolean("firstEndles", false).flush();
    }

    @Override
    void handleWaves() {
        if (spawnedEnemiesForThisWave == numberEnemiesForThisWave && enemies.size() == 0) {
            if (wavesSinceChange-- == 0) {
                wavesSinceChange = 3;
                extraPerWave++;
                spawnTimeuot -= 0.1;
                spawnTimeuot = Math.max(spawnTimeuot, 0.5f);
            }
            waveNumber++;
            timeSinceLastSpawn = 0;
            spawnedEnemiesForThisWave = 0;
            numberEnemiesForThisWave += extraPerWave;
            switchingWaves = true;
        }
    }

    @Override
    void handleStates(float delta) {
        time += delta * 10;
        if (youLost) {
            pauseIP();
            if (!scoreSet) {
                lostWonStage.setScore(score - (int) time);
                scoreSet = true;
            }
            Gdx.input.setInputProcessor(lostWonStage);
            lostWonStage.act(delta);
            lostWonStage.draw();
        } else if (isPaused) {
            pauseScene.draw();
        }
    }
}
