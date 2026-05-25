package astrobankapp.services.outputport;

import astrobankapp.domain.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClientePersistencePort {
    Cliente saveCliente(Cliente cliente);
    List<Cliente> findAllClientes();
    Optional<Cliente> findClienteById(int id);
    Cliente updateCliente(Cliente cliente);
    void deleteCliente(int id);

    Optional<Cliente> findClienteByUsername(String username);
}
