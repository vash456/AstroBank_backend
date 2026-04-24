package astrobankapp.userinterface;

import astrobankapp.domain.Cliente;
import astrobankapp.exception.ValidationException;
import astrobankapp.utils.ClienteFormularioValidacion;
import astrobankapp.view.ClienteView;

import java.util.Scanner;

public class MenuApp {
    Scanner sc = new Scanner(System.in);
    private final ClienteView clienteView;

    public MenuApp(ClienteView clienteView){
        this.clienteView = clienteView;
    }

    public void mostrarMenuPrincipal(){
        System.out.println("Bienvenido a AstroBank ⭐");

        int init = 1;
        while (init != 0){
            System.out.println("""
                    \nSeleccione:
                     1. Registrar Usuario
                     2. Iniciar Sesion
                     3. Salir""");
            int opcion = ClienteFormularioValidacion.validateInt("> ");

            switch (opcion){
                case 1:
                    System.out.println("\nRegistro:");
                    try {
                        clienteView.crearCliente();
                    }catch (ValidationException e) {
                        // Aquí capturas cualquiera de las excepciones porque todas heredan de ValidationException
                        System.out.println("Error de validación: " + e.getMessage() + " ❌");
                    } catch (Exception e) {
                        System.out.println("Error inesperado del sistema. ❌");
                    }

                    break;
                case 2:
                    System.out.println("Inicio sesion");
                    try {
                        Cliente cliente = clienteView.autenticar();

                    }catch (ValidationException e) {
                        // Aquí capturas cualquiera de las excepciones porque todas heredan de ValidationException
                        System.out.println("Error de validación: " + e.getMessage() + " ❌");
                    } catch (Exception e) {
                        System.out.println("Error inesperado del sistema. ❌");
                    }

                    break;
                case 3:
                    init = 0;
                    break;
                default:
                    System.out.println("Opcion no valida, por favor seleccione una opcion valida");
            }
        }
    }

    public void mostrarMenuCliente(){
        int initCliente = 1;
        while (initCliente != 0){
            System.out.println("""
                Que desea hacer?
                1. Consultar saldo
                2. Consignar
                3. Retirar
                4. Transferir
                5. Comprar con tarjeta
                6. Pagar tarjeta
                7. Consultar Movimientos
                8. Ver perfil
                9. Cerrar Sesion""");
            int opcionCliente = ClienteFormularioValidacion.validateInt("> ");

            switch (opcionCliente){
                case 1:
                    System.out.println("Saldos:");
                    break;
                case 2:
                    mostrarMenuCuentas();
                    break;
                case 9:
                    initCliente = 0;
                    break;
                default:
                    System.out.println("Opcion no valida, por favor seleccione una opcion valida");
            }
        }
    }

    public void mostrarMenuCuentas(){
        int initCuenta = 1;
        while (initCuenta != 0){
            System.out.println("""
                        A que cuenta desea consignar?
                        1. Cuenta Ahorros
                        2. Cuenta Corriente
                        3. Volver""");
            int opcionCuentas = ClienteFormularioValidacion.validateInt("> ");
            switch (opcionCuentas){
                case 1:
                    System.out.println("Cuenta Ahorros");
                    break;
                case 2:
                    System.out.println("Cuenta Corriente");
                    break;
                case 3:
                    initCuenta = 0;
                    break;
                default:
                    System.out.println("Opcion no valida, por favor seleccione una opcion valida");

            }
        }
    }
}
