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
        clienteService.createCliente();
    }

    public void getClienteById(int id){
        clienteService.getClienteById(id);
    }

    public void actualizarCliente(){
        clienteService.updateCliente(FormularioValidacion.validateInt("Ingrese el ID"));
    }

    public void borrarCliente(){
        clienteService.deleteCliente(FormularioValidacion.validateInt("Ingrese el ID del cliente a eliminar"));
    }

    public Cliente autenticar(){
        String usuario = FormularioValidacion.validateString("Ingrese el usuario:");
        String contrasena = FormularioValidacion.validateString("Ingrese la Contraseña:");
        Cliente cliente = clienteService.authenticate(usuario,contrasena);
        System.out.println("Login Exitoso ✅");
        return cliente;
    }

}
