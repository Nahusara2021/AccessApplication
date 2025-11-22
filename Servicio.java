package Application;

public class Servicio {
    public int id;
    public String nombre;

    public Servicio(int serv_id, String serv_nombre) {
        this.id = serv_id;
        this.nombre = serv_nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
