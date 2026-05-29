package astrobankapp.services;

import astrobankapp.domain.*;
import astrobankapp.domain.enums.EstadoCuenta;
import astrobankapp.exception.AccountMismatchException;
import astrobankapp.persistence.repository.ClienteRepository;
import astrobankapp.services.input.CuentaService;
import astrobankapp.services.outputport.CuentaPersistensePort;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CuentaServiceImpl implements CuentaService {

    private final CuentaPersistensePort cuentaRepository;

    public CuentaServiceImpl(CuentaPersistensePort cuentaPersistensePort) {
        this.cuentaRepository = cuentaPersistensePort;
    }


    @Override
    public Cuenta buscarPorNumeroCuenta(String numeroCuenta) {
        return cuentaRepository.findCuentaByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new AccountMismatchException("⚠ No se encontró ninguna cuenta con el número: " + numeroCuenta));
    }

    @Override
    public List<Cuenta> findCuentasByClienteId(int clienteId) {
        return cuentaRepository.findCuentasByClienteId(clienteId);
    }

    public String obtenerNumeroUnico(){
        long segundosUnicos = Instant.now().getEpochSecond();
        return String.valueOf(segundosUnicos);
    }

    @Override
    public void initializeCuentasCliente(Cliente cliente){
        String numeroCuentaAhorros = "CA-" + obtenerNumeroUnico();
        CuentaAhorros cuentaAhorros = new CuentaAhorros(numeroCuentaAhorros, cliente, EstadoCuenta.ACTIVA, 0, 1.5);
        String numeroCuentaCorriente = "CC-" + obtenerNumeroUnico();
        CuentaCorriente cuentaCorriente = new CuentaCorriente(numeroCuentaCorriente, cliente, EstadoCuenta.ACTIVA,0, 20, 500000);
        String numeroTarjetaCredito = "TC-" + obtenerNumeroUnico();
        TarjetaCredito tarjetaCredito = new TarjetaCredito(numeroTarjetaCredito, cliente, EstadoCuenta.ACTIVA, 0, 3000000, 0, 12);
        /*cliente.agregarCuenta(cuentaAhorros);
        cliente.agregarCuenta(cuentaCorriente);
        cliente.agregarCuenta(tarjetaCredito);*/

        cuentaRepository.saveCuenta(cuentaAhorros);
        cuentaRepository.saveCuenta(cuentaCorriente);
        cuentaRepository.saveCuenta(tarjetaCredito);
    }

    @Override
    public Cuenta consignarConPersistencia(Cuenta cuenta, double monto) {
        cuenta.consignar(monto);
        return cuentaRepository.updateCuenta(cuenta);
    }

    @Override
    public Cuenta retirarConPersistencia(Cuenta cuenta, double monto) {
        cuenta.retirar(monto);
        return cuentaRepository.updateCuenta(cuenta);
    }

    @Override
    public void transferirConPersistencia(Cuenta cuentaOrigen, Cuenta cuentaDestino, double monto) {
        cuentaOrigen.transferir(cuentaDestino, monto);
        cuentaRepository.actualizarCuentas(cuentaOrigen, cuentaDestino);
    }

    @Override
    public TarjetaCredito comprarConTarjetaConPersistencia(TarjetaCredito tarjeta, double monto, int cuotas) {
        tarjeta.comprar(monto, cuotas);
        return (TarjetaCredito) cuentaRepository.updateCuenta(tarjeta);
    }

}
