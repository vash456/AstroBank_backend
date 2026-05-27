package astrobankapp.services.input;

import astrobankapp.domain.Cliente;
import astrobankapp.domain.Cuenta;

public interface CuentaService {
    public Cuenta buscarPorNumeroCuenta(String numeroCuenta);

    void initializeCliente(Cliente cliente);
}
