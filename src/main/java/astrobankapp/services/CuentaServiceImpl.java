package astrobankapp.services;

import astrobankapp.domain.Cliente;
import astrobankapp.domain.Cuenta;
import astrobankapp.exception.AccountMismatchException;
import astrobankapp.persistence.repository.ClienteRepository;

import java.util.ArrayList;

public class CuentaServiceImpl implements CuentaService{

    private final ClienteRepository clienteRepository;

    public CuentaServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }


    @Override
    public Cuenta buscarPorNumeroCuenta(String numeroCuenta) {
        ArrayList<Cliente> clientes = (ArrayList<Cliente>) clienteRepository.getClientes();
        for (Cliente cliente: clientes){
            for (Cuenta c : cliente.getCuentas()) {
                if (c.getNumeroCuenta().equalsIgnoreCase(numeroCuenta.trim())) {
                    return c;
                }
            }
        }
        throw new AccountMismatchException("⚠ No se encontró ninguna cuenta con el número: " + numeroCuenta);
    }

}
