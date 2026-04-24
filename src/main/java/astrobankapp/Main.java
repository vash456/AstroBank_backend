package astrobankapp;

import astrobankapp.config.Config;
import astrobankapp.userinterface.MenuApp;

public class Main {
    public static void main(String[] args) {
        MenuApp menuApp = Config.crearMenuApp();

        menuApp.mostrarMenuPrincipal();
    }
}
