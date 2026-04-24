package astrobankapp.domain;

import java.util.ArrayList;

public interface Transaccion {
    public void consignar(double monto);
    public void retirar(double monto);
    public double consultarSaldo();
    public ArrayList<Movimiento> obtenerMovimientos();
}
