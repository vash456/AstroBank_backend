package astrobankapp.view;

import astrobankapp.domain.Cliente;
import astrobankapp.domain.Cuenta;
import astrobankapp.domain.TarjetaCredito;
import astrobankapp.services.input.CuentaService;
import astrobankapp.utils.FormularioValidacion;

import java.util.List;

public class CuentaView {

    private final CuentaService cuentaService;

    public CuentaView(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    public Cuenta buscarPorNumeroCuenta() {
        return this.cuentaService.buscarPorNumeroCuenta(FormularioValidacion.validateString("Ingrese el numero de cuenta"));
    }

    public Cuenta buscarPorNumeroCuenta(String numeroCuenta) {
        return this.cuentaService.buscarPorNumeroCuenta(numeroCuenta);
    }

    public List<Cuenta> buscarCuentasPorCliente(int clienteId) {
        return this.cuentaService.findCuentasByClienteId(clienteId);
    }

    public void initializeCuentasCliente(Cliente cliente){
        this.cuentaService.initializeCuentasCliente(cliente);
    }

    public Cuenta consignarDinero(Cuenta cuenta, double monto) {
        return this.cuentaService.consignarConPersistencia(cuenta, monto);
    }

    public Cuenta retirarDinero(Cuenta cuenta, double monto) {
        return this.cuentaService.retirarConPersistencia(cuenta, monto);
    }
    public void transferirDinero(Cuenta cuentaOrigen, Cuenta cuentaDestino, double monto) {
        this.cuentaService.transferirConPersistencia(cuentaOrigen, cuentaDestino, monto);
    }

    public TarjetaCredito comprarConTarjeta(TarjetaCredito tarjeta, double monto, int cuotas) {
        return this.cuentaService.comprarConTarjetaConPersistencia(tarjeta, monto, cuotas);
    }

    public TarjetaCredito pagarTarjeta(Cuenta cuentaOrigen, TarjetaCredito tarjeta, double monto) {
        return this.cuentaService.pagarTarjetaConPersistencia(cuentaOrigen, tarjeta, monto);
    }
}
