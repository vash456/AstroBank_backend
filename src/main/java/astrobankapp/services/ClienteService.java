package astrobankapp.services;

import astrobankapp.domain.Cliente;

import java.util.Optional;

public interface ClienteService {
    public Cliente crearCliente();
    public Cliente getClienteById(int id);
    public Optional<Cliente> getClienteByEmail(String email);
    public Cliente actualizarCliente(int id);
    public void borrarCliente(int id);
    public Cliente autenticar();
}
