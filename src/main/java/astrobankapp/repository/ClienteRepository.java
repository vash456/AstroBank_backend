package astrobankapp.repository;

import astrobankapp.domain.Cliente;
import astrobankapp.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {
    List<Cliente> clientes = new ArrayList<>();

    public int cantidadListaCliente(){
        return clientes.size();
    }

    public Cliente guardarCliente(Cliente cliente){

        clientes.add(cliente);

        return cliente;
    }

    public Cliente buscarPorUsuario(String nombreUsuario) {
        for (Cliente cliente:clientes){
            if (cliente.getUsuario().equals(nombreUsuario)){
                return cliente;
            }
        }
        throw new UserNotFoundException(nombreUsuario);
    }

    public List<Cliente> getClientes() {
        return clientes;
    }
}
