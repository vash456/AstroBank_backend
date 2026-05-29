package astrobankapp.services.input;

import astrobankapp.domain.Cliente;
import astrobankapp.domain.Cuenta;

import java.util.List;

public interface CuentaService {
    public Cuenta buscarPorNumeroCuenta(String numeroCuenta);
    List<Cuenta> findCuentasByClienteId(int clienteId);
    void initializeCuentasCliente(Cliente cliente);
    Cuenta consignarConPersistencia(Cuenta cuenta, double monto);
    Cuenta retirarConPersistencia(Cuenta cuenta, double monto);
    void transferirConPersistencia(Cuenta cuentaOrigen, Cuenta cuentaDestino, double monto);
}
