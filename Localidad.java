package Application;

public class Localidad {
    public int id;
    public String nombre;

    public Localidad(int loc_id, String loc_nombre) {
        this.id = loc_id;
        this.nombre = loc_nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
