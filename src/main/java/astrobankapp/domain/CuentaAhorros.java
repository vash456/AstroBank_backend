package astrobankapp.domain;

import java.util.ArrayList;

public class CuentaAhorros extends Cuenta{

    private double tasaInteres;

    public CuentaAhorros(String numeroCuenta, Cliente propietario, EstadoCuenta estadoCuenta, double saldo, double tasaInteres) {
        super(numeroCuenta, propietario, estadoCuenta, saldo);
        this.tasaInteres = tasaInteres;
    }

    public void aplicarIntereses(){}

    public double calcularIntereses(){
        return 0;
    }

    @Override
    public void retirar(double monto){

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
    }

    @Override
    public ArrayList<Movimiento> obtenerMovimientos() {
        return super.obtenerMovimientos();
    }

    @Override
    public void consignar(double monto) {
        super.consignar(monto);
    }

    public double getTasaInteres() {
        return tasaInteres;
    }

    public void setTasaInteres(double tasaInteres) {
        this.tasaInteres = tasaInteres;
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
}
