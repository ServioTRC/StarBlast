package mx.itesm.starblast;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Ian Neumann on 16/02/2017.
 */

public class NaveEnemiga extends NavesEspaciales {

    public NaveEnemiga(String ubicacion, float x, float y) {
        super(ubicacion, x, y);
    }

    @Override
    protected void generarDisparo() {
        throw new NotImplementedException();
    }
}
