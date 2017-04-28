package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.ArrayList;

public class PreferencesSB {
    public static boolean MUSIC_ENABLE = true;
    public static boolean SOUNDS_ENABLE = true;

    public static void readingSoundPreferences(){
        Preferences prefs = Gdx.app.getPreferences("Sounds");
        MUSIC_ENABLE = prefs.getBoolean("MUSIC_ENABLE",true);
        SOUNDS_ENABLE = prefs.getBoolean("SOUNDS_ENABLE",true);
    }

    public static void savingSoundPreferences(){
        Preferences prefs = Gdx.app.getPreferences("Sounds");
        prefs.putBoolean("MUSIC_ENABLE", MUSIC_ENABLE);
        prefs.putBoolean("SOUNDS_ENABLE", SOUNDS_ENABLE);
        prefs.flush();
    }

    public static void savingScore(String userName, int userScore){
        //Comparing Values
        Preferences prefs = Gdx.app.getPreferences("High Scores");
        ArrayList<Integer> scores = new ArrayList<Integer>(6);
        ArrayList<String> names = new ArrayList<String>(6);
        String[] arrInfo;
        for(int i = 1; i <= 5; i++) {
            arrInfo = prefs.getString("punt"+i, "----- 00000").split(" ");
            scores.add(Integer.parseInt(arrInfo[1]));
            names.add(arrInfo[0]);
        }
        for(int i = 0; i < 5; i++){
            if(scores.get(i) <= userScore){
                scores.add(i, userScore);
                names.add(i, userName);
                Gdx.app.log("Preferences ",String.valueOf(i));
                break;
            }
        }
        //Saving Scores
        for(int i = 1; i <= 5; i++){
            prefs.putString("punt"+i, names.get(i-1)+" "+scores.get(i-1));
        }
        prefs.flush();
    }

    public static void erasingGameInfo(){
        Preferences prefs = Gdx.app.getPreferences("High Scores");
        //Rewriting information
        for(int i = 1; i <= 5; i++){
            prefs.putString("punt"+i, "----- 00000");
        }
        prefs = Gdx.app.getPreferences("Levels");
        prefs.putInteger("Level", 1);
        prefs.flush();
    }


    public static void saveLevelProgress(int level){
        Preferences prefs = Gdx.app.getPreferences("Levels");
        prefs.putInteger("Level", level);
        prefs.flush();
    }

    public static int readLevelProgress(){
        Preferences prefs = Gdx.app.getPreferences("Levels");
        return prefs.getInteger("Level", 1);
    }

}
