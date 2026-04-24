package astrobankapp.domain;

import java.util.ArrayList;

public class CuentaAhorros extends Cuenta{

    private double tasaInteres;


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
}
