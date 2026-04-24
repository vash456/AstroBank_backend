package astrobankapp.config;

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

        return new MenuApp(clienteView);
    }


}
