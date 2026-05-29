package astrobankapp.services.input;

import astrobankapp.domain.Cliente;

import java.util.Optional;

public interface ClienteService {
    public Cliente createCliente();
    public Cliente getClienteById(int id);
    public Optional<Cliente> getClienteByEmail(String email);
    public Cliente updateCliente(int id);
    public void deleteCliente(int id);
    public Cliente authenticate(String username, String password);
    public Cliente cambiarContrasena(Cliente cliente, String actual, String nueva, String confirmar);
    Optional<Cliente> findClienteByUsername(String username);

}
