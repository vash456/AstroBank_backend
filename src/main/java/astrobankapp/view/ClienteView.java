package astrobankapp.view;

import astrobankapp.domain.Cliente;
import astrobankapp.services.ClienteService;
import astrobankapp.utils.FormularioValidacion;

public class ClienteView {
    private final ClienteService clienteService;

    public ClienteView(ClienteService clienteService){
        this.clienteService = clienteService;
    }

    public void crearCliente(){
        clienteService.crearCliente();
    }

    public void getClienteById(int id){
        clienteService.getClienteById(id);
    }

    public void actualizarCliente(){
        clienteService.actualizarCliente(FormularioValidacion.validateInt("Ingrese el ID"));
    }

    public void borrarCliente(){
        clienteService.borrarCliente(FormularioValidacion.validateInt("Ingrese el ID del cliente a eliminar"));
    }

    public Cliente autenticar(){
        return clienteService.autenticar();
    }

}
