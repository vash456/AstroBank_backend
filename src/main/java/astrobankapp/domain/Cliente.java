package astrobankapp.domain;

import astrobankapp.exception.AuthenticationException;

import java.util.ArrayList;
import java.util.List;

public class Cliente implements Autenticable{
    private int id;
    private String identificacion;
    private String nombreCompleto;
    private String usuario;
    private String correo;
    private String celular;
    private String contrasena;
    private int intentosFallidos;
    private boolean bloqueado;

    private List<Cuenta> cuentas;

    public Cliente(){
        this.cuentas = new ArrayList<Cuenta>();
    }

    public Cliente(int id, String contrasena, String celular, String correo, String usuario, String nombreCompleto, String identificacion) {
        this.id = id;
        this.bloqueado = false;
        this.intentosFallidos = 0;
        this.contrasena = contrasena;
        this.celular = celular;
        this.correo = correo;
        this.usuario = usuario;
        this.nombreCompleto = nombreCompleto;
        this.identificacion = identificacion;
        this.cuentas = new ArrayList<>();
    }

    public void agregarCuenta(Cuenta cuenta){
        this.cuentas.add(cuenta);
    }

    @Override
    public boolean autenticar(String usuario, String contrasena){
        if (!usuario.equals(this.usuario) || !contrasena.equals(this.contrasena)) {
            intentosFallidos++;
            return false;
        }
        intentosFallidos = 0;
        return true;
    }

    @Override
    public void cambiarContrasena(String anteriorContrasena, String nuevaContrasena){

    }

    public void incrementarIntentos(){

    }

    public void resetearIntentos(){

    }

    public void editarPerfil(){

    }

    @Override
    public void cerrarSesion(){

    }

    @Override
    public String toString() {
        return "\nCliente:" +
                "\n\tnombreCompleto = '" + nombreCompleto + '\'' +
                "\n\tusuario = '" + usuario + '\'' +
                "\n\tidentificacion = '" + identificacion + '\'' +
                "\n\tcorreo = '" + correo + '\'' +
                "\n\tcelular = '" + celular + '\'';
    }

    // Setters and Getters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public int getIntentosFallidos() {
        return intentosFallidos;
    }

    public void setIntentosFallidos(int intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
