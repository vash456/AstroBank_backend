package astrobankapp.domain;

import astrobankapp.domain.enums.EstadoCuenta;
import astrobankapp.domain.enums.TipoMovimiento;
import astrobankapp.exception.AccountMismatchException;
import astrobankapp.utils.FormularioValidacion;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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

    @Override
    public boolean validarDestino(Cuenta cuentaDestino){
        if (cuentaDestino.estadoCuenta != EstadoCuenta.ACTIVA){
            throw new IllegalStateException();
        }
        if (this.numeroCuenta.equals(cuentaDestino.numeroCuenta)){
            throw new AccountMismatchException("Cuenta destino '" + cuentaDestino.numeroCuenta + "' invalida");
        }
        return true;
    }

    public ArrayList<Movimiento> obtenerMovimientos(){
        return new ArrayList<>(this.movimientos);
    }

    public void registrarMovimiento(Movimiento movimiento){
        this.movimientos.add(movimiento);
    };

    @Override
    public void transferir(Cuenta cuentaDestino, double monto){
        if (!validarDestino(cuentaDestino))
            throw new IllegalArgumentException("Cuenta destino inválida");

        this.retirar(monto);
        cuentaDestino.consignar(monto);

        // Movimiento adicional que identifica la transferencia
        registrarMovimiento(new Movimiento(
                TipoMovimiento.TRANSFERENCIA_OUT, monto, saldo,
                "Transferencia a cuenta " + cuentaDestino.numeroCuenta));

        cuentaDestino.registrarMovimiento(new Movimiento(
                TipoMovimiento.TRANSFERENCIA_IN, monto, cuentaDestino.saldo,
                "Transferencia desde cuenta " + this.numeroCuenta));
        System.out.println("Transferencia exitosa ✅");
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public EstadoCuenta getEstadoCuenta() {
        return estadoCuenta;
    }
}
