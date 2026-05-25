package astrobankapp.services;

import astrobankapp.domain.Cliente;

import java.util.Optional;

public interface ClienteService {
    public Cliente createCliente();
    public Cliente getClienteById(int id);
    public Optional<Cliente> getClienteByEmail(String email);
    public Cliente updateCliente(int id);
    public void deleteCliente(int id);
    void initializeCliente(Cliente cliente);
    public Cliente authenticate(String username, String password);
    Optional<Cliente> findClienteByUsername(String username);

}
