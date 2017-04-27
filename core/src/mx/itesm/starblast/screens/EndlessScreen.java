package mx.itesm.starblast.screens;

import mx.itesm.starblast.StarBlast;

public class EndlessScreen extends LevelStory {

    private int wavesSinceChange = 3;

    EndlessScreen(StarBlast app) {
        super(app, 5, 2, 0, 0);
    }

    @Override
    void handleWaves() {
        if (spawnedEnemiesForThisWave == numberEnemiesForThisWave && enemies.size() == 0) {
            if(wavesSinceChange--==0){
                wavesSinceChange = 3;
                extraPerWave++;
                spawnTimeuot -= 0.05;
            }
            waveNumber++;
            timeSinceLastSpawn = 0;
            spawnedEnemiesForThisWave = 0;
            numberEnemiesForThisWave += extraPerWave;
            switchingWaves = true;
        }
    }
}
