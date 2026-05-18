package astrobankapp.persistence.repository;

import astrobankapp.domain.Cliente;
import astrobankapp.services.outputport.ClientePersistencePort;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepositoryAdapterMySql implements ClientePersistencePort {

    private final Connection connection;

    public ClienteRepositoryAdapterMySql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Cliente saveCliente(Cliente cliente) {
       String sql = "INSERT INTO cliente " +
               "(id, identificacion, nombre_completo,celular, usuario, contrasena_hash, intentos fallidos, bloqueado)" +
               "VALUES (?,?,?,?,?,?,?,?)";
        try(PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){
            setClienteParams(ps, cliente);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()){
                cliente.setId(keys.getInt(1));
            }
        }catch (SQLException e){
            throw new RuntimeException("Error al guardar cliente", e);
        }
        return cliente;
    }

    private void setClienteParams(PreparedStatement ps, Cliente cliente) throws SQLException{
        ps.setInt(1, cliente.getId());
        ps.setString(2, cliente.getIdentificacion());
        ps.setString(3, cliente.getNombreCompleto());
        ps.setString(4,cliente.getCelular());
        ps.setString(5, cliente.getUsuario());
        ps.setString(6, cliente.getContrasena());
        ps.setInt(7, cliente.getIntentosFallidos());
        ps.setBoolean(8, cliente.isBloqueado());

    }


    @Override
    public List<Cliente> findAllClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "Select * from cliente";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
            while (rs.next()){

            }
        }catch (SQLException e){
            throw new RuntimeException("Error al buscar los datos: " + e);
        }
        return clientes;
    }

    @Override
    public Cliente findClienteById(int id) {
        return null;
    }

    @Override
    public Cliente updateCliente(Cliente cliente) {
        return null;
    }

    @Override
    public void deleteCliente(int id) {

    }


}
