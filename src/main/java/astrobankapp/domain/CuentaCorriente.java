package astrobankapp.domain;

import astrobankapp.domain.enums.EstadoCuenta;
import astrobankapp.domain.enums.TipoMovimiento;
import astrobankapp.exception.InvalidAmountException;
import astrobankapp.utils.FormularioValidacion;

import java.util.ArrayList;

public class CuentaCorriente extends Cuenta{
    private double porcentajeSobregiro;
    private double limiteSobregiro;

    public void calcularLimiteSobregiro(){}

    public CuentaCorriente(String numeroCuenta, Cliente propietario, EstadoCuenta estadoCuenta, double saldo, double porcentajeSobregiro, double limiteSobregiro) {
        super(numeroCuenta, propietario, estadoCuenta, saldo);
        this.porcentajeSobregiro = porcentajeSobregiro;
        this.limiteSobregiro = limiteSobregiro;
    }

    @Override
    public double consultarSaldo() {
        return super.consultarSaldo();
    }

    @Override
    public void consignar(double monto) {
        super.consignar(monto);
    }

    @Override
    public ArrayList<Movimiento> obtenerMovimientos() {
        return super.obtenerMovimientos();
    }

    @Override
    public void retirar(double monto) {
        double sobregiro = this.saldo * (this.porcentajeSobregiro / 100);
        FormularioValidacion.validatePositiveAmount(monto);
        if ((this.saldo + this.limiteSobregiro) < monto)
            throw new InvalidAmountException("Supera el límite de sobregiro");
        if ((this.saldo + sobregiro) < monto)
            throw new InvalidAmountException("Supera el porcentaje de sobregiro");
        this.saldo -= monto;
        registrarMovimiento(new Movimiento(
                TipoMovimiento.RETIRO, monto, this.saldo,
                "Retiro de $" + monto));
        System.out.println("Retiro exitoso ✅");
    }

    @Override
    public void registrarMovimiento(Movimiento movimiento) {
        super.registrarMovimiento(movimiento);
    }

    @Override
    public void transferir(Cuenta cuentaDestino, double monto) {
        super.transferir(cuentaDestino, monto);
    }

    @Override
    public boolean validarDestino(Cuenta cuenta) {
        return super.validarDestino(cuenta);
    }

    @Override
    public String toString() {
        return "\nCuentaCorriente:" +
                "\n\tnumeroCuenta = '" + numeroCuenta + '\'' +
                "\n\tsaldo = " + saldo +
                "\n\tporcentajeSobregiro = " + porcentajeSobregiro +
                "\n\tlimiteSobregiro = " + limiteSobregiro +
                "\n\tpropietario = " + propietario.getNombreCompleto() +
                "\n\testadoCuenta = " + estadoCuenta +
                "\n\tfechaApertura = '" + fechaApertura + '\'';
    }

    public double getPorcentajeSobregiro() {
        return porcentajeSobregiro;
    }

    public double getLimiteSobregiro() {
        return limiteSobregiro;
    }
}
