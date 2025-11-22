package Application;

public class Discapacidad {
    public int id;
    public String nombre;

    public Discapacidad(int td_id, String td_nombre) {
        this.id = td_id;
        this.nombre = td_nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
