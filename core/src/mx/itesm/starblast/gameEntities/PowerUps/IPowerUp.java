package mx.itesm.starblast.gameEntities.PowerUps;

/**
 * Created by Ian Neumann on 29/04/2017.
 */

public interface IPowerUp {

    float getBonus();

    Type type();

    enum Type{
        health,
        damage,
        speed,
        shield,
        missile;
    }
}
