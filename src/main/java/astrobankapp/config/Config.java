package astrobankapp.config;

import astrobankapp.domain.Cliente;
import astrobankapp.persistence.repository.ClienteRepository;
import astrobankapp.services.ClienteService;
import astrobankapp.services.ClienteServiceImpl;
import astrobankapp.services.CuentaService;
import astrobankapp.services.CuentaServiceImpl;
import astrobankapp.userinterface.MenuApp;
import astrobankapp.view.ClienteView;
import astrobankapp.view.CuentaView;

public class Config {

    public static MenuApp crearMenuApp(){
        ClienteRepository clienteRepository = new ClienteRepository();
        ClienteService clienteService = new ClienteServiceImpl(clienteRepository);
        ClienteView clienteView = new ClienteView(clienteService);

        CuentaService cuentaService = new CuentaServiceImpl(clienteRepository);
        CuentaView cuentaView = new CuentaView(cuentaService);

        //datos de prueba iniciales
        Cliente cliente = new Cliente(
        1, "123456", "1234567890", "c@c.c", "dar", "Darlin Estrada", "10404040"
        );
        clienteRepository.guardarCliente(cliente);
        clienteService.iniciarlizarCliente(cliente);

        return new MenuApp(clienteView,cuentaView);
    }


}
