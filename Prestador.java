package Application;

public class Prestador {
    public int id;
    public String nombre;

    public Prestador(int pre_id, String pre_nombre) {
        this.id = pre_id;
        this.nombre = pre_nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
