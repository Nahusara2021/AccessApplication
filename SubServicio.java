package Application;

public class SubServicio {
    public int id;
    public String nombre;

    public SubServicio(int sserv_id, String sserv_nombre) {
        this.id = sserv_id;
        this.nombre = sserv_nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
