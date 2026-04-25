package astrobankapp.view;

import astrobankapp.domain.Cuenta;
import astrobankapp.services.CuentaService;
import astrobankapp.utils.FormularioValidacion;

public class CuentaView {

    private final CuentaService cuentaService;

    public CuentaView(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    public Cuenta buscarPorNumeroCuenta() {
        return this.cuentaService.buscarPorNumeroCuenta(FormularioValidacion.validateString("Ingrese el numero de cuenta"));
    }
}
