package astrobankapp.services;

import astrobankapp.domain.*;
import astrobankapp.exception.AuthenticationException;
import astrobankapp.services.input.ClienteService;
import astrobankapp.services.outputport.ClientePersistencePort;
import astrobankapp.utils.FormularioValidacion;

import java.util.Optional;

public class ClienteServiceImpl implements ClienteService {

    private final ClientePersistencePort clienteRepository;


    public ClienteServiceImpl(ClientePersistencePort clienteRepository){
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Cliente authenticate(String username, String password){
        FormularioValidacion.validateLoginForm(username, password);
        Cliente cliente = clienteRepository.findClienteByUsername(username)
                .orElseThrow(() -> new AuthenticationException("Usuario o contraseña incorrectos."));

        if (cliente.isBloqueado()) {
            throw new AuthenticationException("Usuario bloqueado. Contacte al administrador.");
        }

        boolean autenticado = cliente.autenticar(cliente.getUsuario(), password);
        if (!autenticado) {
            int intentos = cliente.getIntentosFallidos();
            if (intentos >= 3) {
                cliente.setBloqueado(true);
            }
            clienteRepository.updateCliente(cliente);
            if (cliente.isBloqueado()) {
                throw new AuthenticationException("Usuario bloqueado tras 3 intentos fallidos.");
            }
            throw new AuthenticationException("Usuario o contraseña incorrectos. Intentos fallidos: " + intentos + "/3.");
        }

        if (cliente.getIntentosFallidos() > 0) {
            cliente.setIntentosFallidos(0);
            clienteRepository.updateCliente(cliente);
        }

        return cliente;
    }


    @Override
    public Cliente createCliente() {

        Cliente cliente = new Cliente();
        //cliente.setId(clienteRepository.cantidadListaCliente()+1);

        String identificacion = FormularioValidacion.validateString("Ingrese la Identificacion:");

        String nombreCompleto = FormularioValidacion.validateString("Ingrese su nombre completo:");

        String usuario = FormularioValidacion.validateString("Ingrese el Usuario");

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
    public Cliente updateCliente(int id) {
        return null;
    }

    @Override
    public void deleteCliente(int id) {

    }

    @Override
    public Optional<Cliente> findClienteByUsername(String username) {
        return clienteRepository.findClienteByUsername(username);
    }

    @Override
    public Cliente cambiarContrasena(Cliente cliente, String actual, String nueva, String confirmar) {
        FormularioValidacion.validatePasswordChange(actual, nueva, confirmar);
        cliente.cambiarContrasena(actual, nueva);
        return clienteRepository.updateCliente(cliente);
    }
}
