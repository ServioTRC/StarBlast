package mx.itesm.starblast.gameEntities;

/**
 * Created by Ian Neumann on 29/04/2017.
 */

public interface IPowerUp {

    public float getBonus();

    public Type type();

    public static enum Type{
        health,
        damage,
        speed,
        shield,
        missile;
    }
}
