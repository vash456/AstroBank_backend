package astrobankapp.services;

import astrobankapp.domain.*;
import astrobankapp.exception.AuthenticationException;
import astrobankapp.exception.UserAlreadyExistsException;
import astrobankapp.exception.UserNotFoundException;
import astrobankapp.repository.ClienteRepository;
import astrobankapp.utils.ClienteFormularioValidacion;

import java.time.Instant;
import java.util.Optional;

public class ClienteServiceImpl implements ClienteService{

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository){
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
        String usuario = ClienteFormularioValidacion.validateString("Ingrese el usuario:");
        String contrasena = ClienteFormularioValidacion.validateString("Ingrese la Contraseña:");
        ClienteFormularioValidacion.validateLoginForm(usuario,contrasena);
        Cliente cliente = clienteRepository.buscarPorUsuario(usuario);
        if (!cliente.autenticar(usuario,contrasena))
            throw new AuthenticationException("Usuario o contraseña incorrectos.");
        System.out.println("Login Exitoso ✅");
        return cliente;
    }


    @Override
    public Cliente crearCliente() {

        Cliente cliente = new Cliente();
        cliente.setId(clienteRepository.cantidadListaCliente()+1);

        String identificacion = ClienteFormularioValidacion.validateString("Ingrese la Identificacion:");

        String nombreCompleto = ClienteFormularioValidacion.validateString("Ingrese su nombre completo:");

        String usuario = ClienteFormularioValidacion.validateString("Ingrese el Usuario");

        try {
            clienteRepository.buscarPorUsuario(usuario);
            throw new UserAlreadyExistsException(usuario);
        }catch (UserNotFoundException e) {
            // si pasa aca, el usuario está disponible.
            System.out.println("El usuario está disponible.");
        }

        String celular = ClienteFormularioValidacion.validateString("Ingrese su celular:");

        String correo = ClienteFormularioValidacion.validateString("Ingrese su correo:");

        String contrasena = ClienteFormularioValidacion.validateString("Ingrese la contraseña:");

        String confirmarContrasena = ClienteFormularioValidacion.validateString("Confirme la contraseña:");

        ClienteFormularioValidacion.validateCustomerForm(
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
        return clienteRepository.guardarCliente(cliente);
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
