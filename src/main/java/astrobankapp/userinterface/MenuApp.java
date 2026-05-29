package astrobankapp.userinterface;

import astrobankapp.domain.Cliente;
import astrobankapp.domain.Cuenta;
import astrobankapp.domain.Movimiento;
import astrobankapp.domain.TarjetaCredito;
import astrobankapp.exception.ValidationException;
import astrobankapp.persistence.database.DataBaseConnectionMySql;
import astrobankapp.session.Session;
import astrobankapp.utils.FormularioValidacion;
import astrobankapp.view.ClienteView;
import astrobankapp.view.CuentaView;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuApp {
    Scanner sc = new Scanner(System.in);
    private final ClienteView clienteView;
    private final CuentaView cuentaView;
    private final Session session;

    public MenuApp(ClienteView clienteView, CuentaView cuentaView, Session session){
        this.clienteView = clienteView;
        this.cuentaView = cuentaView;
        this.session = session;
    }

    public void mostrarMenuPrincipal(){
        System.out.println("Bienvenido a AstroBank ⭐");
        DataBaseConnectionMySql.getInstance().getConnection();

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
                        Cliente cliente = clienteView.crearCliente();
                        cuentaView.initializeCuentasCliente(cliente);
                    }catch (ValidationException e) {
                        // Aquí capturas cualquiera de las excepciones porque todas heredan de ValidationException
                        System.out.println("Error de validación: " + e.getMessage() + " ❌");
                    } catch (Exception e) {
                        System.out.println("Error inesperado del sistema. ❌" + e.getMessage());
                    }

                    break;
                case 2:
                    System.out.println("Inicio sesion");
                    try {
                        Cliente cliente = clienteView.autenticar();
                        cliente.setCuentas(cuentaView.buscarCuentasPorCliente(cliente.getId()));
                        System.out.println("funciona");
                        session.start(cliente);
                        System.out.println("funciona 2");
                        mostrarMenuCliente();
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
        Cliente cliente = session.getCliente();
        if (cliente == null) {
            System.out.println("No hay una sesión activa. Por favor inicie sesión primero.");
            return;
        }
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
                        Cuenta cuentaSeleccionada = seleccionarCuenta((ArrayList<Cuenta>) cliente.getCuentas(), true);
                        if (cuentaSeleccionada == null) {
                            break;
                        }
                        Cuenta cuentaActualizada = cuentaView.buscarPorNumeroCuenta(cuentaSeleccionada.getNumeroCuenta());
                        if (cuentaActualizada.getPropietario().getId() != cliente.getId()) {
                            throw new ValidationException("La cuenta seleccionada no pertenece al cliente autenticado.");
                        }
                        System.out.printf("Saldo actual de la cuenta %s: %.2f%n", cuentaActualizada.getNumeroCuenta(), cuentaActualizada.consultarSaldo());
                    }catch (ValidationException e) {
                        System.out.println("Error de validación: " + e.getMessage() + " ❌");
                    } catch (Exception e) {
                        System.out.println("Error inesperado del sistema. ❌" + e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        System.out.println("Consignar:");
                        Cuenta cuentaSeleccionada = seleccionarCuenta((ArrayList<Cuenta>) cliente.getCuentas(), true);
                        if (cuentaSeleccionada == null) {
                            break;
                        }
                        double monto = FormularioValidacion.validateDouble("Ingrese el monto: ");
                        Cuenta cuentaActualizada = cuentaView.consignarDinero(cuentaSeleccionada, monto);
                        System.out.printf("Consignación exitosa. Nuevo saldo: %.2f%n", cuentaActualizada.consultarSaldo());
                    }catch (ValidationException e) {
                        System.out.println("Error de validación: " + e.getMessage() + " ❌");
                    } catch (Exception e) {
                        System.out.println("Error inesperado del sistema. ❌" + e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        System.out.println("Retirar:");
                        Cuenta cuentaSeleccionada = seleccionarCuenta((ArrayList<Cuenta>) cliente.getCuentas(),true);
                        if (cuentaSeleccionada == null) {
                            break;
                        }
                        double monto = FormularioValidacion.validateDouble("Ingrese el monto: ");
                        Cuenta cuentaActualizada = cuentaView.retirarDinero(cuentaSeleccionada, monto);
                        System.out.printf("Retiro exitoso. Nuevo saldo: %.2f%n", cuentaActualizada.consultarSaldo());
                    }catch (ValidationException e) {
                        System.out.println("Error de validación: " + e.getMessage() + " ❌");
                    } catch (Exception e) {
                        System.out.println("Error inesperado del sistema. ❌" + e.getMessage());
                    }
                    break;

                case 4:
                    try {
                        System.out.println("Transferir:");
                        Cuenta cuentaSeleccionada = seleccionarCuenta((ArrayList<Cuenta>) cliente.getCuentas(),true);
                        if (cuentaSeleccionada == null) {
                            break;
                        }
                        System.out.println("Cuenta Destino");
                        Cuenta cuentaDestino = cuentaView.buscarPorNumeroCuenta();
                        double monto = FormularioValidacion.validateDouble("Ingrese el monto:");
                        cuentaView.transferirDinero(cuentaSeleccionada, cuentaDestino, monto);
                        System.out.printf("Transferencia exitosa.%nSaldo origen (%s): %.2f%nSaldo destino (%s): %.2f%n",
                                cuentaSeleccionada.getNumeroCuenta(), cuentaSeleccionada.consultarSaldo(),
                                cuentaDestino.getNumeroCuenta(), cuentaDestino.consultarSaldo());
                    }catch (ValidationException e) {
                        System.out.println("Error de validación: " + e.getMessage() + " ❌");
                    } catch (Exception e) {
                        System.out.println("Error inesperado del sistema. ❌" + e.getMessage());
                    }
                    break;

                case 5:
                    try {
                        System.out.println("Comprar con tarjeta:");
                        // Seleccionar tarjeta de crédito
                        ArrayList<Cuenta> soloTarjetas = new ArrayList<>();
                        for (Cuenta c : cliente.getCuentas()) {
                            if (c instanceof TarjetaCredito) {
                                soloTarjetas.add(c);
                            }
                        }
                        
                        if (soloTarjetas.isEmpty()) {
                            System.out.println("⚠ No tienes tarjetas de crédito disponibles.");
                            break;
                        }
                        
                        System.out.println("\nSelecciona tu tarjeta de crédito:");
                        System.out.printf("%-10s %-15s %-12s %-12s%n", "#", "Número", "Cupo", "Deuda");
                        for (int i = 0; i < soloTarjetas.size(); i++) {
                            TarjetaCredito tc = (TarjetaCredito) soloTarjetas.get(i);
                            System.out.printf("%-10d %-15s $%-11.2f $%-11.2f%n", 
                                    i + 1, tc.getNumeroCuenta(), tc.getCupo(), tc.getDeuda());
                        }
                        int opcionTarjeta = FormularioValidacion.validateInt("> ");
                        if (opcionTarjeta < 1 || opcionTarjeta > soloTarjetas.size()) {
                            System.out.println("Opción inválida.");
                            break;
                        }
                        TarjetaCredito tarjetaSeleccionada = (TarjetaCredito) soloTarjetas.get(opcionTarjeta - 1);
                        
                        // Solicitar monto y cuotas
                        double montoCom = FormularioValidacion.validateDouble("Ingrese el monto de la compra: ");
                        
                        System.out.println("\nSelecciona el número de cuotas:");
                        System.out.println("1. 2 cuotas (0% interés)");
                        System.out.println("2. 3 cuotas (1.9% interés mensual)");
                        System.out.println("3. 4 cuotas (1.9% interés mensual)");
                        System.out.println("4. 5 cuotas (1.9% interés mensual)");
                        System.out.println("5. 6 cuotas (1.9% interés mensual)");
                        System.out.println("6. 7 cuotas (2.3% interés mensual)");
                        System.out.println("7. 8 cuotas (2.3% interés mensual)");
                        System.out.println("8. 12 cuotas (2.3% interés mensual)");
                        int opcionCuotas = FormularioValidacion.validateInt("> ");
                        
                        int cuotasSeleccionadas = 0;
                        switch (opcionCuotas) {
                            case 1 -> cuotasSeleccionadas = 2;
                            case 2 -> cuotasSeleccionadas = 3;
                            case 3 -> cuotasSeleccionadas = 4;
                            case 4 -> cuotasSeleccionadas = 5;
                            case 5 -> cuotasSeleccionadas = 6;
                            case 6 -> cuotasSeleccionadas = 7;
                            case 7 -> cuotasSeleccionadas = 8;
                            case 8 -> cuotasSeleccionadas = 12;
                            default -> {
                                System.out.println("Opción inválida.");
                                break;
                            }
                        }
                        
                        if (cuotasSeleccionadas > 0) {
                            TarjetaCredito tarjetaActualizada = cuentaView.comprarConTarjeta(tarjetaSeleccionada, montoCom, cuotasSeleccionadas);
                            System.out.printf("Cupo disponible: $%.2f%nDeuda total: $%.2f%n",
                                    tarjetaActualizada.getCupo() - tarjetaActualizada.getDeuda(), tarjetaActualizada.getDeuda());
                        }
                    }catch (ValidationException e) {
                        System.out.println("Error de validación: " + e.getMessage() + " ❌");
                    } catch (Exception e) {
                        System.out.println("Error inesperado del sistema. ❌" + e.getMessage());
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
                    System.out.println("Cerrando sesión...");
                    session.end();
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
