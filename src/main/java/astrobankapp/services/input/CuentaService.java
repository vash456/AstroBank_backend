package astrobankapp.services.input;

import astrobankapp.domain.Cliente;
import astrobankapp.domain.Cuenta;
import astrobankapp.domain.TarjetaCredito;

import java.util.List;

public interface CuentaService {
    public Cuenta buscarPorNumeroCuenta(String numeroCuenta);
    List<Cuenta> findCuentasByClienteId(int clienteId);
    void initializeCuentasCliente(Cliente cliente);
    Cuenta consignarConPersistencia(Cuenta cuenta, double monto);
    Cuenta retirarConPersistencia(Cuenta cuenta, double monto);
    void transferirConPersistencia(Cuenta cuentaOrigen, Cuenta cuentaDestino, double monto);
    TarjetaCredito comprarConTarjetaConPersistencia(TarjetaCredito tarjeta, double monto, int cuotas);
    TarjetaCredito pagarTarjetaConPersistencia(Cuenta cuentaOrigen, TarjetaCredito tarjeta, double monto);
}
