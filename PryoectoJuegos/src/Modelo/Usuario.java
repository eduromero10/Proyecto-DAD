package Modelo;

public class Usuario {
    private int idUsuario;
    private String correo;
    private String contraseña;

    public Usuario(int idUsuario, String correo, String contraseña) {
        this.idUsuario = idUsuario;
        this.correo = correo;
        this.contraseña = contraseña;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getCorreo() {
        return correo;
    }

    public String getContraseña() {
        return contraseña;
    }
}