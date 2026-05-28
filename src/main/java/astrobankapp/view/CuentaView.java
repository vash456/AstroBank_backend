package astrobankapp.view;

import astrobankapp.domain.Cliente;
import astrobankapp.domain.Cuenta;
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
}
