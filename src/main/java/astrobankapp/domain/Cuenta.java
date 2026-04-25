package astrobankapp.domain;

import astrobankapp.exception.AccountMismatchException;
import astrobankapp.utils.FormularioValidacion;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
        this.fechaApertura = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));;
        this.saldo = saldo;
    }

    public double consultarSaldo(){
        return this.saldo;
    }

    public void consignar(double monto){
        FormularioValidacion.validatePositiveAmount(monto);
        if (this.estadoCuenta != EstadoCuenta.ACTIVA){
            throw new IllegalStateException();
        }
        this.saldo += monto;
        registrarMovimiento(new Movimiento(
                TipoMovimiento.CONSIGNACION, monto, saldo,
                "Consignación de $" + monto));
        System.out.println("Consignacion exitosa ✅");
    }

    public abstract void retirar(double monto);

    public ArrayList<Movimiento> obtenerMovimientos(){
        return new ArrayList<>(this.movimientos);
    }

    public void registrarMovimiento(Movimiento movimiento){
        this.movimientos.add(movimiento);
    };

    @Override
    public void transferir(Cuenta cuentaDestino, double monto){

    }

    @Override
    public boolean validarDestino(Cuenta cuentaDestino){
        if (cuentaDestino.estadoCuenta != EstadoCuenta.ACTIVA){
            throw new IllegalStateException();
        }
        if (this.numeroCuenta.equals(cuentaDestino.numeroCuenta)){
            throw new AccountMismatchException(cuentaDestino.numeroCuenta);
        }
        return true;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public EstadoCuenta getEstadoCuenta() {
        return estadoCuenta;
    }
}
