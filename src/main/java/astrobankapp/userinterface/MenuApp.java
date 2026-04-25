package astrobankapp.userinterface;

import astrobankapp.domain.Cliente;
import astrobankapp.domain.Cuenta;
import astrobankapp.domain.Movimiento;
import astrobankapp.domain.TarjetaCredito;
import astrobankapp.exception.ValidationException;
import astrobankapp.utils.FormularioValidacion;
import astrobankapp.view.ClienteView;

import java.util.ArrayList;
import java.util.List;
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
            int opcion = FormularioValidacion.validateInt("> ");

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
                        mostrarMenuCliente(cliente);
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

    public void mostrarMenuCliente(Cliente cliente){
        int initCliente = 1;
        while (initCliente != 0){
            System.out.println("""
                \nQue desea hacer?
                1. Consultar saldos
                2. Consignar
                3. Retirar
                4. Transferir
                5. Comprar con tarjeta
                6. Pagar tarjeta
                7. Consultar Movimientos
                8. Ver perfil
                9. Cerrar Sesion""");
            int opcionCliente = FormularioValidacion.validateInt("> ");

            switch (opcionCliente){
                case 1:
                    System.out.println("Saldos:");
                    try {
                        Cuenta cuentaSeleccionada = seleccionarCuenta((ArrayList<Cuenta>) cliente.getCuentas(),true);
                        System.out.println(cuentaSeleccionada);
                    }catch (ValidationException e) {
                        System.out.println("Error de validación: " + e.getMessage() + " ❌");
                    } catch (Exception e) {
                        System.out.println("Error inesperado del sistema. ❌");
                    }

                    //  for (Cuenta cuenta:cliente.getCuentas()){
                    //      System.out.println(cuenta.toString());
                    //  }
                    break;
                case 2:
                    try {
                        System.out.println("Consignar:");
                        Cuenta cuentaSeleccionadaAux = seleccionarCuenta((ArrayList<Cuenta>) cliente.getCuentas(),true);
                        double monto = FormularioValidacion.validateDouble("Ingrese el monto: ");
                        cuentaSeleccionadaAux.consignar(monto);
                    }catch (ValidationException e) {
                        System.out.println("Error de validación: " + e.getMessage() + " ❌");
                    } catch (Exception e) {
                        System.out.println("Error inesperado del sistema. ❌");
                    }

                    break;
                case 3:
                    try {
                        System.out.println("Retirar:");
                        Cuenta cuentaSeleccionada = seleccionarCuenta((ArrayList<Cuenta>) cliente.getCuentas(),true);
                        double monto = FormularioValidacion.validateDouble("Ingrese el monto: ");
                        cuentaSeleccionada.retirar(monto);
                    }catch (ValidationException e) {
                        System.out.println("Error de validación: " + e.getMessage() + " ❌");
                    } catch (Exception e) {
                        System.out.println("Error inesperado del sistema. ❌");
                    }
                    break;
                case 7:
                    try {
                        System.out.println("Consultar Movimientos:");
                        Cuenta cuentaSeleccionada = seleccionarCuenta((ArrayList<Cuenta>) cliente.getCuentas(),true);
                        for (Movimiento m:cuentaSeleccionada.obtenerMovimientos()){
                            System.out.println(m);
                        }
                    }catch (ValidationException e) {
                        System.out.println("Error de validación: " + e.getMessage() + " ❌");
                    } catch (Exception e) {
                        System.out.println("Error inesperado del sistema. ❌" + e.getMessage());
                    }
                    break;
                case 8:
                    System.out.println(cliente.toString());
                    break;
                case 9:
                    initCliente = 0;
                    break;
                default:
                    System.out.println("Opcion no valida, por favor seleccione una opcion valida");
            }
        }
    }


    public Cuenta seleccionarCuenta(
            ArrayList<Cuenta> cuentas,
            boolean excluirTarjetas) {

        // Filtrar según el parámetro excluirTarjetas
        List<Cuenta> cuentasFiltradas = new ArrayList<>();
        for (Cuenta c : cuentas) {
            if (excluirTarjetas && c instanceof TarjetaCredito) {
                continue; // omite TarjetaCredito
            }
            cuentasFiltradas.add(c);
        }

        // Validar que haya cuentas para mostrar
        if (cuentasFiltradas.isEmpty()) {
            System.out.println("\n⚠ No hay cuentas disponibles para mostrar.\n");
            return null;
        }

        Scanner scanner = new Scanner(System.in);
        int opcion = -1;

        do {
            // Encabezado del menú
            System.out.println("\nSeleccione la Cuenta:");
            System.out.printf( "%-10s %-15s %-14s%n",
                    "#", "Número cuenta", "Tipo");

            // Listar cuentas filtradas
            for (int i = 0; i < cuentasFiltradas.size(); i++) {
                Cuenta c = cuentasFiltradas.get(i);
                System.out.printf("%-10d %-15s %-14s%n",
                        i + 1,
                        c.getNumeroCuenta(),
                        obtenerTipo(c));
            }

            System.out.println("0  → Cancelar                           ║");
            System.out.print("  Seleccione una opción: ");

            // Leer y validar entrada
            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
            } else {
                scanner.next(); // descartar entrada no numérica
                opcion = -1;
            }

            if (opcion == 0) {
                System.out.println("  Operación cancelada.");
                return null;
            }

            if (opcion < 1 || opcion > cuentasFiltradas.size()) {
                System.out.println("  ⚠ Opción inválida. Intente de nuevo.");
                opcion = -1; // forzar repetición del bucle
            }

        } while (opcion == -1);

        // Retornar la cuenta elegida (índice base-0)
        Cuenta seleccionada = cuentasFiltradas.get(opcion - 1);
        System.out.printf("%n  ✅ Cuenta seleccionada: %s (%s)%n",
                seleccionada.getNumeroCuenta(), obtenerTipo(seleccionada));

        return seleccionada;
    }

    private String obtenerTipo(Cuenta c) {
        return c.getClass().getSimpleName();
    }

    public int menuSeleccionCuentas(Cliente cliente){
        int initCuenta = 1;
        int opcionCuentas = 0;
        while (initCuenta != 0){
            System.out.println("""
                        Elija la cuenta:
                        1. Cuenta Ahorros
                        2. Cuenta Corriente
                        3. Volver""");
            opcionCuentas = FormularioValidacion.validateInt("> ");
            switch (opcionCuentas){
                case 1:
                    System.out.println("Cuenta Ahorros");
                    initCuenta = 0;
                    break;
                case 2:
                    System.out.println("Cuenta Corriente");
                    initCuenta = 0;
                    break;
                case 3:
                    initCuenta = 0;
                    opcionCuentas = 0;
                    break;
                default:
                    System.out.println("Opcion no valida, por favor seleccione una opcion valida");

            }
        }
        return opcionCuentas;
    }
}
