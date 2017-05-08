package a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria.Bd;

/**
 * Created by Gand on 31/01/17.
 */

public class VistaPedido {
    private String nombre;
    private String hora;
    private float precioTotal;
    private int estado;

    public VistaPedido(String nombre, String hora, float precioTotal, int estado) {
        this.nombre = nombre;
        this.hora = hora;
        this.precioTotal = precioTotal;
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public String getHora() {
        return hora;
    }

    public float getPrecioTotal() {
        return precioTotal;
    }

    public int getEstado() {
        return estado;
    }
}
