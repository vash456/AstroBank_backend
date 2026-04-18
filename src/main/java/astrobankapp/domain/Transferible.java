package astrobankapp.domain;

public interface Transferible {
    public void transferir(Cuenta cuentaDestino, double monto);
    public boolean validarDestino(Cuenta cuenta);
}
