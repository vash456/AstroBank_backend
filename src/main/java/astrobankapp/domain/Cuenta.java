package astrobankapp.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public abstract class Cuenta implements Transferible, Transaccion {
    protected String numeroCuenta;
    protected double saldo;
    protected String fechaApertura;
    protected EstadoCuenta estadoCuenta;
    protected ArrayList<Movimiento> movimientos;

    protected Cliente propietario;

    public Cuenta(String numeroCuenta, Cliente propietario, EstadoCuenta estadoCuenta, double saldo) {
        this.numeroCuenta = numeroCuenta;
        this.propietario = propietario;
        this.movimientos = new ArrayList<>();
        this.estadoCuenta = estadoCuenta;
        this.fechaApertura = LocalDateTime.now().toString();
        this.saldo = saldo;
    }

    public double consultarSaldo(){
        return this.saldo;
    }

    public void consignar(double monto){}

    public void retirar(double monto){}

    public ArrayList<Movimiento> obtenerMovimientos(){
        return null;
    }

    public abstract void registrarMovimiento(Movimiento movimiento);

    @Override
    public void transferir(Cuenta cuentaDestino, double monto){

    }

    @Override
    public boolean validarDestino(Cuenta cuenta){
        return false;
    }
}
