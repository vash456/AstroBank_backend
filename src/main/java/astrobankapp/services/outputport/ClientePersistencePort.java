package astrobankapp.services.outputport;

import astrobankapp.domain.Cliente;

import java.util.List;

public interface ClientePersistencePort {
    Cliente saveCliente(Cliente cliente);
    List<Cliente> findAllClientes();
    Cliente findClienteById(int id);
    Cliente updateCliente(Cliente cliente);
    void deleteCliente(int id);
}
