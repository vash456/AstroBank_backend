package astrobankapp.domain;

import astrobankapp.domain.enums.EstadoCuenta;

import java.util.ArrayList;

public class TarjetaCredito extends Cuenta{
    private double cupo;
    private double deuda;
    private int numeroCuotas;

    public void comprar(double monto, int cuotas){}

    public void pagar(double monto){}

    public double calcularTasa(int coutas){
        return 0;
    }

    public double calcularCoutaMensual(){
        return 0;
    }

    public TarjetaCredito(String numeroCuenta, Cliente propietario, EstadoCuenta estadoCuenta, double saldo, double cupo, double deuda, int numeroCuotas) {
        super(numeroCuenta, propietario, estadoCuenta, saldo);
        this.cupo = cupo;
        this.deuda = deuda;
        this.numeroCuotas = numeroCuotas;
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
    public void retirar(double monto) {

    }

    @Override
    public ArrayList<Movimiento> obtenerMovimientos() {
        return super.obtenerMovimientos();
    }

    @Override
    public void registrarMovimiento(Movimiento movimiento) {
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
        return "\nTarjetaCredito:" +
                "\n\tnumeroCuenta = '" + numeroCuenta + '\'' +
                "\n\tcupo = " + cupo +
                "\n\tdeuda = " + deuda +
                "\n\tpropietario = " + propietario.getNombreCompleto() +
                "\n\testadoCuenta = " + estadoCuenta +
                "\n\tfechaApertura = '" + fechaApertura + '\'';
    }
}
