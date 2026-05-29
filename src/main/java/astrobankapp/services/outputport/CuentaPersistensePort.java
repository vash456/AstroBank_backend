package astrobankapp.services.outputport;

import astrobankapp.domain.Cuenta;
import astrobankapp.domain.Movimiento;

import java.util.List;
import java.util.Optional;

public interface CuentaPersistensePort {
    Cuenta saveCuenta(Cuenta cuenta);
    List<Cuenta> findAllCuentas();
    Optional<Cuenta> findCuentaById(int id);
    Optional<Cuenta> findCuentaByNumeroCuenta(String numeroCuenta);
    List<Cuenta> findCuentasByClienteId(int clienteId);
    Cuenta updateCuenta(Cuenta cuenta);
    void saveMovimiento(Cuenta cuenta, Movimiento movimiento);
    void saveMovimientos(Cuenta cuenta);
    List<Movimiento> findMovimientosByNumeroCuenta(String numeroCuenta);
    void actualizarCuentas(Cuenta cuentaOrigen, Cuenta cuentaDestino);
    void deleteCuenta(int id);
}
