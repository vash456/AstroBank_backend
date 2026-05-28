package astrobankapp.services.outputport;

import astrobankapp.domain.Cuenta;

import java.util.List;
import java.util.Optional;

public interface CuentaPersistensePort {
    Cuenta saveCuenta(Cuenta cuenta);
    List<Cuenta> findAllCuentas();
    Optional<Cuenta> findCuentaById(int id);
    Optional<Cuenta> findCuentaByNumeroCuenta(String numeroCuenta);
    List<Cuenta> findCuentasByClienteId(int clienteId);
    Cuenta updateCuenta(Cuenta cuenta);
    void deleteCuenta(int id);
}
