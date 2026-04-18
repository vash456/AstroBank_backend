package astrobankapp.domain;

import java.util.ArrayList;
import java.util.Date;

public class Cuenta {
    private String numeroCuenta;
    private double saldo;
    private String fechaApertura;
    private String estadoCuenta;
    private ArrayList<Movimiento> movimientos;

    public double consultarSaldo(){
        return this.saldo;
    }

    public void consignar(double monto){}

    public void retirar(double monto){}

    public ArrayList<Movimiento> obtenerMovimientos(){
        return null;
    }

    public void registrarMovimiento(Movimiento movimiento){}
}
