package astrobankapp.config;

import astrobankapp.domain.Cliente;
import astrobankapp.repository.ClienteRepository;
import astrobankapp.services.ClienteService;
import astrobankapp.services.ClienteServiceImpl;
import astrobankapp.userinterface.MenuApp;
import astrobankapp.view.ClienteView;

public class Config {

    public static MenuApp crearMenuApp(){
        ClienteRepository clienteRepository = new ClienteRepository();
        ClienteService clienteService = new ClienteServiceImpl(clienteRepository);
        ClienteView clienteView = new ClienteView(clienteService);

        //datos de prueba iniciales
        Cliente cliente = new Cliente(
        1, "123456", "1234567890", "c@c.c", "dar", "Darlin Estrada", "10404040"
        );
        clienteRepository.guardarCliente(cliente);
        clienteService.iniciarlizarCliente(cliente);

        return new MenuApp(clienteView);
    }


}
