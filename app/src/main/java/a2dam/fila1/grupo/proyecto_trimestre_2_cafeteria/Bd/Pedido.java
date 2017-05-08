package a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria.Bd;

import java.util.ArrayList;

/**
 * Created by Raquel on 12/01/17.
 */

public class Pedido {
    private Usuario usuario;
    private Producto producto;
    private int cantidad;
    private float precio;
    private String comentarios, hora;

    public Pedido(Usuario usuario, Producto producto, int cantidad, float precio, String comentarios, String hora) {
        this.usuario = usuario;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.comentarios = comentarios;
        this.hora = hora;
    }

    public Usuario getUsuario() {        return usuario;    }
    public Producto getProducto() {        return producto;    }
    public int getCantidad() {        return cantidad;    }
    public float getPrecio() {        return precio;     }
    public String getComentarios() {        return comentarios;    }
    public String getHora() {
        return hora;
    }
}
