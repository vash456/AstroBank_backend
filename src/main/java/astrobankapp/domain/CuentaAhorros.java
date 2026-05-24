package astrobankapp.domain;

import astrobankapp.domain.enums.EstadoCuenta;
import astrobankapp.domain.enums.TipoMovimiento;
import astrobankapp.exception.InvalidAmountException;
import astrobankapp.utils.FormularioValidacion;

import java.util.ArrayList;

public class CuentaAhorros extends Cuenta{

    private double tasaInteres;

    public CuentaAhorros(String numeroCuenta, Cliente propietario, EstadoCuenta estadoCuenta, double saldo, double tasaInteres) {
        super(numeroCuenta, propietario, estadoCuenta, saldo);
        this.tasaInteres = tasaInteres;
    }

    public void aplicarIntereses(){
        double intereses = calcularIntereses();
        this.saldo += intereses;
        registrarMovimiento(new Movimiento(
                TipoMovimiento.INTERES, intereses, saldo,
                "Intereses aplicados (tasa: " + tasaInteres + ")"));
    }

    public double calcularIntereses(){
        return this.saldo * (this.tasaInteres / 100);
    }

    @Override
    public void retirar(double monto){
        FormularioValidacion.validatePositiveAmount(monto);
        if (saldo < monto){
            throw new InvalidAmountException("Saldo insuficiente");
        }
        this.saldo -= monto;
        aplicarIntereses();
        registrarMovimiento(new Movimiento(
                TipoMovimiento.RETIRO, monto, this.saldo,
                "Retiro de $" + monto));
        System.out.println("Retiro exitoso ✅");
    }

    @Override
    public double consultarSaldo() {
        return super.consultarSaldo();
    }

    @Override
    public boolean validarDestino(Cuenta cuenta) {
        return super.validarDestino(cuenta);
    }

    @Override
    public void transferir(Cuenta cuentaDestino, double monto) {
        super.transferir(cuentaDestino, monto);
    }

    @Override
    public void registrarMovimiento(Movimiento movimiento) {
        super.registrarMovimiento(movimiento);
    }

    @Override
    public ArrayList<Movimiento> obtenerMovimientos() {
        return super.obtenerMovimientos();
    }

    @Override
    public void consignar(double monto) {
        super.consignar(monto);
    }


    @Override
    public String toString() {
        return "\nCuentaAhorros:" +
                "\n\tnumeroCuenta='" + numeroCuenta + '\'' +
                "\n\tsaldo=" + saldo +
                "\n\ttasaInteres=" + tasaInteres +
                "\n\tpropietario=" + propietario.getNombreCompleto() +
                "\n\testadoCuenta=" + estadoCuenta +
                "\n\tfechaApertura='" + fechaApertura + '\'';
    }

    public double getTasaInteres() { return tasaInteres; }

}
