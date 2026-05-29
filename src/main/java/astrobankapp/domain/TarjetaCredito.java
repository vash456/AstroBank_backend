package astrobankapp.domain;

import astrobankapp.domain.enums.EstadoCuenta;
import astrobankapp.domain.enums.TipoMovimiento;

import java.util.ArrayList;

public class TarjetaCredito extends Cuenta{
    private double cupo;
    private double deuda;
    private int numeroCuotas;

    public void comprar(double monto, int cuotas){
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }
        if (cuotas <= 0) {
            throw new IllegalArgumentException("El número de cuotas debe ser positivo");
        }
        if (monto > (this.cupo - this.deuda)) {
            throw new IllegalArgumentException("Cupo insuficiente. Cupo disponible: " + (this.cupo - this.deuda));
        }

        double tasa = calcularTasa(cuotas);
        double cuotaMensual = calcularCoutaMensual(monto, tasa, cuotas);
        double totalIntereses = (cuotaMensual * cuotas) - monto;

        this.deuda += cuotaMensual * cuotas;
        this.numeroCuotas = cuotas;

        registrarMovimiento(new Movimiento(
                TipoMovimiento.COMPRA_TC, monto, this.deuda,
                "Compra con tarjeta: $" + String.format("%.2f", monto) + " en " + cuotas + " cuotas"));

        if (totalIntereses > 0) {
            registrarMovimiento(new Movimiento(
                    TipoMovimiento.INTERES, totalIntereses, this.deuda,
                    "Intereses: " + String.format("%.2f", totalIntereses)));
        }

        System.out.println("Compra exitosa ✅");
        System.out.printf("Monto: $%.2f | Cuotas: %d | Cuota mensual: $%.2f | Intereses: $%.2f%n",
                monto, cuotas, cuotaMensual, totalIntereses);
    }

    public double calcularTasa(int cuotas){
        if (cuotas <= 2) {
            return 0.0; // 0% - sin interés
        } else if (cuotas >= 3 && cuotas <= 6) {
            return 0.019; // 1.9% mensual
        } else {
            return 0.023; // 2.3% mensual para >= 7 cuotas
        }
    }

    public double calcularCoutaMensual(double capital, double tasa, int numerosCuotas){
        if (tasa == 0) {
            return capital / numerosCuotas;
        }
        // Fórmula: Cuota = (Capital × tasa) / (1 - (1 + tasa)^-n)
        double numerador = capital * tasa;
        double denominador = 1 - Math.pow(1 + tasa, -numerosCuotas);
        return numerador / denominador;
    }

    public void pagar(double monto){
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }
        if (monto > this.deuda) {
            throw new IllegalArgumentException("El monto de pago no puede ser mayor que la deuda actual");
        }

        this.deuda -= monto;
        registrarMovimiento(new Movimiento(
                TipoMovimiento.PAGO_TC, monto, this.deuda,
                "Pago de tarjeta de crédito: $" + String.format("%.2f", monto)));

        System.out.println("Pago de tarjeta exitoso ✅");
        System.out.printf("Monto pagado: $%.2f | Deuda restante: $%.2f%n", monto, this.deuda);
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
        return "\nTarjetaCredito:" +
                "\n\tnumeroCuenta = '" + numeroCuenta + '\'' +
                "\n\tcupo = " + cupo +
                "\n\tdeuda = " + deuda +
                "\n\tpropietario = " + propietario.getNombreCompleto() +
                "\n\testadoCuenta = " + estadoCuenta +
                "\n\tfechaApertura = '" + fechaApertura + '\'';
    }

    public double getCupo() {
        return cupo;
    }

    public void setCupo(double cupo) {
        this.cupo = cupo;
    }

    public double getDeuda() {
        return deuda;
    }

    public void setDeuda(double deuda) {
        this.deuda = deuda;
    }

    public int getNumeroCuotas() {
        return numeroCuotas;
    }

    public void setNumeroCuotas(int numeroCuotas) {
        this.numeroCuotas = numeroCuotas;
    }
}
