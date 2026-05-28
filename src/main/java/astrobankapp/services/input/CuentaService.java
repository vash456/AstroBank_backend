package astrobankapp.services.input;

import astrobankapp.domain.Cliente;
import astrobankapp.domain.Cuenta;

import java.util.List;

public interface CuentaService {
    public Cuenta buscarPorNumeroCuenta(String numeroCuenta);
    List<Cuenta> findCuentasByClienteId(int clienteId);
    void initializeCuentasCliente(Cliente cliente);
}
