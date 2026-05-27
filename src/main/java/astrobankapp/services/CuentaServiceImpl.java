package astrobankapp.services;

import astrobankapp.domain.*;
import astrobankapp.domain.enums.EstadoCuenta;
import astrobankapp.exception.AccountMismatchException;
import astrobankapp.persistence.repository.ClienteRepository;
import astrobankapp.services.input.CuentaService;
import astrobankapp.services.outputport.CuentaPersistensePort;

import java.time.Instant;
import java.util.ArrayList;

public class CuentaServiceImpl implements CuentaService {

    private final CuentaPersistensePort cuentaRepository;

    public CuentaServiceImpl(CuentaPersistensePort cuentaPersistensePort) {
        this.cuentaRepository = cuentaPersistensePort;
    }


    @Override
    public Cuenta buscarPorNumeroCuenta(String numeroCuenta) {
        /*ArrayList<Cliente> clientes = (ArrayList<Cliente>) cuentaRepository.getClientes();
        for (Cliente cliente: clientes){
            for (Cuenta c : cliente.getCuentas()) {
                if (c.getNumeroCuenta().equalsIgnoreCase(numeroCuenta.trim())) {
                    return c;
                }
            }
        }
        throw new AccountMismatchException("⚠ No se encontró ninguna cuenta con el número: " + numeroCuenta);*/
        return null;
    }

    public String obtenerNumeroUnico(){
        long segundosUnicos = Instant.now().getEpochSecond();
        return String.valueOf(segundosUnicos);
    }

    @Override
    public void initializeCliente(Cliente cliente){
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

}
