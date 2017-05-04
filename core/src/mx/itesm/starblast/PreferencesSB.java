package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PreferencesSB {
    public static boolean MUSIC_ENABLE = true;
    public static boolean SOUNDS_ENABLE = true;

    public static void readSoundPreferences() {
        Preferences prefs = Gdx.app.getPreferences("Sounds");
        MUSIC_ENABLE = prefs.getBoolean("MUSIC_ENABLE", true);
        SOUNDS_ENABLE = prefs.getBoolean("SOUNDS_ENABLE", true);
    }

    public static void saveSoundPreferences() {
        Preferences prefs = Gdx.app.getPreferences("Sounds");
        prefs.putBoolean("MUSIC_ENABLE", MUSIC_ENABLE);
        prefs.putBoolean("SOUNDS_ENABLE", SOUNDS_ENABLE);
        prefs.flush();
    }

    public static void saveScore(String userName, int userScore) {
        //Comparing Values
        Preferences prefs = Gdx.app.getPreferences("High Scores");
        ArrayList<String> scores = new ArrayList<String>(6);
        scores.add(userName + " " + userScore);
        for (int i = 0; i < 5; i++) {
            scores.add(prefs.getString(i + "", "----- 00000"));
        }
        Collections.sort(scores, new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return -Integer.valueOf(s.split(" ")[1]).compareTo(Integer.valueOf(t1.split(" ")[1]));
            }
        });
        scores.remove(5);
        //Saving Scores
        for (int i = 0; i < 5; i++) {
            prefs.putString("" + i, scores.get(i));
        }
        prefs.flush();
    }

    public static void eraseGameInfo() {
        Preferences prefs = Gdx.app.getPreferences("High Scores");
        prefs.clear();
        prefs.flush();
        prefs = Gdx.app.getPreferences("Levels");
        prefs.clear();
        prefs.flush();
        prefs = Gdx.app.getPreferences("Codes");
        prefs.clear();
        prefs.flush();
        prefs = Gdx.app.getPreferences("Minigames");
        prefs.clear();
        prefs.flush();
    }

    public static void saveLevelProgress(int level) {
        Preferences prefs = Gdx.app.getPreferences("Levels");
        prefs.putInteger("Level", level);
        prefs.flush();
    }

    public static int readLevelProgress() {
        Preferences prefs = Gdx.app.getPreferences("Levels");
        return prefs.getInteger("Level", 1);
    }

    public static void activateCheatCode(String code, StarBlast app) {
        Gdx.app.log("Codigo Ingresado", code);
        code = code.toLowerCase().trim();
        Preferences prefs = Gdx.app.getPreferences("Codes");
        if (code.equals("fat man")) {
            //ininite missiles
            prefs.putBoolean("InfMissiles", !prefs.getBoolean("InfMissiles", false));
        } else if (code.equals("heart container")) {
            //infinite health
            Gdx.app.log("Cheats", "hard container");
            prefs.putBoolean("InfHealth", !prefs.getBoolean("InfHealth", false));
        } else if (code.equals("duke nuke")) {
            //super damage (balas explosivas)
        } else if (code.equals("darude")) {
            //darude sandstorm
            boolean darude = prefs.getBoolean("darude", false);
            prefs.putBoolean("darude", !darude);
            app.changeMusic(!darude ? Constant.DARUDE : Constant.ORIGINAL_MUSIC);
        } else if (code.equals("dark souls")) {
            //get used to dying
        } else if (code.equals("sonic the hedgehog")) {
            //super speed
            Gdx.app.log("Cheats", "speed");
            prefs.putBoolean("speed", !prefs.getBoolean("speed", false));
        } else if (code.equals("yeah that's honest")) {
            //think
        } else if (code.equals("speed run")) {
            //gana
            Gdx.app.log("Cheats", "speed run");
            PreferencesSB.saveLevelProgress(PreferencesSB.readLevelProgress() > 3 ? 1 : 4);
            Preferences pref = Gdx.app.getPreferences("Minigames");
            pref.putBoolean("1", true);
            pref.putBoolean("2", true);
            pref.putBoolean("3", true);
            pref.flush();
        } else if (code.equals("mario party")) {
            //unlock minigames
        } else if (code.equals("uuddlrlrab")) {
            //konamy code (+ misiles, 2xVida)
            prefs.putBoolean("konami", !prefs.getBoolean("konami", false));
        } else if (code.equals("360 no scope")) {
            //homing misiles (and bullets)
        } else if(code.equals("hard as nails")) {
            //extra colision damage
            prefs.putBoolean("nails", !prefs.getBoolean("nails", false));
        }else if (code.split(" ")[0].equals("lvl")) {
            String[] tmp = code.split(" ");
            if (tmp.length == 2) {
                int level;
                try {
                    level = Integer.valueOf(tmp[1]);
                    if (level <= 0) {
                        return;
                    }
                } catch (NumberFormatException ex) {
                    return;
                }
                Preferences tmpPref = Gdx.app.getPreferences("Levels");
                tmpPref.putInteger("Level", level);
                tmpPref.flush();
            }
        } else if (code.split(" ")[0].equals("mng")) {
            String[] tmp = code.split(" ");
            if (tmp.length == 2) {
                int mng;
                try {
                    mng = Integer.valueOf(tmp[1]);
                    if (mng <= 0 || mng > 3) {
                        return;
                    }
                } catch (NumberFormatException ex) {
                    return;
                }
                Preferences tmpPref = Gdx.app.getPreferences("Minigames");
                tmpPref.putBoolean(mng + "", true);
                tmpPref.flush();
            }
        }
        prefs.flush();
    }

    public static void saveMinigameProgress(int minigame) {
        Preferences prefs = Gdx.app.getPreferences("Minigames");
        if (!prefs.getBoolean("" + minigame, false)) {
            prefs.putBoolean("" + minigame, true);
            prefs.flush();
        }
    }

    public static int getMinigameCount() {
        Preferences prefs = Gdx.app.getPreferences("Minigames");
        int toR = 0;
        if (prefs.getBoolean("1", false)) {
            toR++;
        }
        if (prefs.getBoolean("2", false)) {
            toR++;
        }
        if (prefs.getBoolean("3", false)) {
            toR++;
        }
        return toR;
    }

    public static void clickedSound(){
        if(PreferencesSB.SOUNDS_ENABLE)
            Constant.clickSound.play(0.5f);
    }

}
