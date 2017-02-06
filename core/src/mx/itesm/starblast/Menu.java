package mx.itesm.starblast;

import com.badlogic.gdx.Game;

/**
 * Created by Servio T on 05/02/2017.
 */

public class Menu extends Game {
    @Override
    public void create () {
        setScreen(new PantallaSplashTec(this));
    }
}
