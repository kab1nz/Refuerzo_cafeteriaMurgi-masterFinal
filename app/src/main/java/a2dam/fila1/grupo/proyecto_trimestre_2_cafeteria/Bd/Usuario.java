package a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria.Bd;

/**
 * Created by Raquel on 12/01/17.
 */

public class Usuario {
    private int id, categoria;
    //Categoria 0=Admin, 1=Camarero, 2=Profesores, 3=Alumnos

    private String nombre, pass, mail;
    private int telefono;

    public Usuario(int id, String nombre, String pass, int telefono, String mail, int categoria) {
        this.id = id;
        this.nombre = nombre;
        this.pass = pass;
        this.telefono = telefono;
        this.mail = mail;
        this.categoria = categoria;
    }

    public Usuario(int id, String nombre, String mail) {
        this.id = id;
        this.nombre = nombre;
        this.mail = mail;
    }

    public int getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public String getPass() {
        return pass;
    }
    public int getCategoria() {return categoria;}
    public int getTelefono() {return telefono;}
    public String getMail() {        return mail;    }
}
