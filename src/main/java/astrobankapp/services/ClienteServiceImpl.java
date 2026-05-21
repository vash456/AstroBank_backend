package astrobankapp.services;

import astrobankapp.domain.*;
import astrobankapp.exception.AuthenticationException;
import astrobankapp.exception.UserAlreadyExistsException;
import astrobankapp.exception.UserNotFoundException;
import astrobankapp.persistence.repository.ClienteRepository;
import astrobankapp.services.outputport.ClientePersistencePort;
import astrobankapp.utils.FormularioValidacion;

import java.time.Instant;
import java.util.Optional;

public class ClienteServiceImpl implements ClienteService{

    private final ClientePersistencePort clienteRepository;


    public ClienteServiceImpl(ClientePersistencePort clienteRepository){
        this.clienteRepository = clienteRepository;
    }

    public String obtenerNumeroUnico(){
        long segundosUnicos = Instant.now().getEpochSecond();
        return String.valueOf(segundosUnicos);
    }

    @Override
    public void iniciarlizarCliente(Cliente cliente){
        String numeroCuentaAhorros = "CA-" + obtenerNumeroUnico();
        CuentaAhorros cuentaAhorros = new CuentaAhorros(numeroCuentaAhorros, cliente, EstadoCuenta.ACTIVA, 0, 1.5);
        String numeroCuentaCorriente = "CC-" + obtenerNumeroUnico();
        CuentaCorriente cuentaCorriente = new CuentaCorriente(numeroCuentaCorriente, cliente, EstadoCuenta.ACTIVA,0, 20, 500000);
        String numeroTarjetaCredito = "TC-" + obtenerNumeroUnico();
        TarjetaCredito tarjetaCredito = new TarjetaCredito(numeroTarjetaCredito, cliente, EstadoCuenta.ACTIVA, 0, 3000000, 0, 12);
        cliente.agregarCuenta(cuentaAhorros);
        cliente.agregarCuenta(cuentaCorriente);
        cliente.agregarCuenta(tarjetaCredito);
    }

    @Override
    public Cliente autenticar(){
        //String usuario = FormularioValidacion.validateString("Ingrese el usuario:");
        int id = FormularioValidacion.validateInt("Por favor ingrese el id: ");
        String contrasena = FormularioValidacion.validateString("Ingrese la Contraseña:");
        //FormularioValidacion.validateLoginForm(usuario,contrasena);
        //Cliente cliente = clienteRepository.buscarPorUsuario(usuario);
        Cliente cliente = clienteRepository.findClienteById(id);
        if (!cliente.autenticar(cliente.getUsuario(),contrasena))
            throw new AuthenticationException("Usuario o contraseña incorrectos.");
        System.out.println("Login Exitoso ✅");
        return cliente;
    }


    @Override
    public Cliente crearCliente() {

        Cliente cliente = new Cliente();
        //cliente.setId(clienteRepository.cantidadListaCliente()+1);

        String identificacion = FormularioValidacion.validateString("Ingrese la Identificacion:");

        String nombreCompleto = FormularioValidacion.validateString("Ingrese su nombre completo:");

        String usuario = FormularioValidacion.validateString("Ingrese el Usuario");

        /*try {
            clienteRepository.buscarPorUsuario(usuario);
            throw new UserAlreadyExistsException(usuario);
        }catch (UserNotFoundException e) {
            // si pasa aca, el usuario está disponible.
            System.out.println("El usuario está disponible.");
        } */

        String celular = FormularioValidacion.validateString("Ingrese su celular:");

        String correo = FormularioValidacion.validateString("Ingrese su correo:");

        String contrasena = FormularioValidacion.validateString("Ingrese la contraseña:");

        String confirmarContrasena = FormularioValidacion.validateString("Confirme la contraseña:");

        FormularioValidacion.validateCustomerForm(
                identificacion,
                nombreCompleto,
                usuario,
                celular,
                correo,
                contrasena,
                confirmarContrasena);

        cliente.setIdentificacion(identificacion);
        cliente.setNombreCompleto(nombreCompleto);
        cliente.setUsuario(usuario);
        cliente.setCelular(celular);
        cliente.setCorreo(correo);
        cliente.setContrasena(contrasena);
        cliente.setIntentosFallidos(0);
        cliente.setBloqueado(false);

        iniciarlizarCliente(cliente);

        System.out.println("Usuario registrado exitosamente ✅");
        return clienteRepository.saveCliente(cliente);
    }



    @Override
    public Cliente getClienteById(int id) {
        return null;
    }

    @Override
    public Optional<Cliente> getClienteByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Cliente actualizarCliente(int id) {
        return null;
    }

    @Override
    public void borrarCliente(int id) {

    }
}
