package astrobankapp.domain;

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
        super.retirar(monto);
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
}
