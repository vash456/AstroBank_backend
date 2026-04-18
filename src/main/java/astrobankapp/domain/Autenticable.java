package astrobankapp.domain;

public interface Autenticable {
    public boolean autenticar(String usuario, String contrasena);
    public void cerrarSesion();
    public void cambiarContrasena(String anteriorContrasena, String nuevaContrasena);
}
