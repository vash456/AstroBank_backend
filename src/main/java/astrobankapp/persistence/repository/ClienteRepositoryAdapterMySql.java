package astrobankapp.persistence.repository;

import astrobankapp.domain.Cliente;
import astrobankapp.persistence.mapper.ClienteRowMapper;
import astrobankapp.services.outputport.ClientePersistencePort;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteRepositoryAdapterMySql implements ClientePersistencePort {

    private final Connection connection;
    private final ClienteRowMapper rowMapper;

    public ClienteRepositoryAdapterMySql(Connection connection, ClienteRowMapper rowMapper) {
        this.connection = connection;
        this.rowMapper = rowMapper;
    }



    @Override
    public Cliente saveCliente(Cliente cliente) {
       String sql = "INSERT INTO cliente " +
               "(identificacion, nombre_completo, celular, usuario, correo, contrasena_hash, intentos_fallidos, bloqueado)" +
               "VALUES (?,?,?,?,?,?,?,?)";
        try(PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){
            setClienteParams(ps, cliente);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()){
                cliente.setId(keys.getInt(1));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException("Error al guardar cliente", e);
        }
        return cliente;
    }

    private void setClienteParams(PreparedStatement ps, Cliente cliente) throws SQLException{
        ps.setString(1, cliente.getIdentificacion());
        ps.setString(2, cliente.getNombreCompleto());
        ps.setString(3,cliente.getCelular());
        ps.setString(4, cliente.getUsuario());
        ps.setString(5, cliente.getCorreo());
        ps.setString(6, cliente.getContrasena());
        ps.setInt(7, cliente.getIntentosFallidos());
        int bloqueado = cliente.isBloqueado() ? 1 : 0;
        ps.setInt(8, bloqueado);
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
    public Optional<Cliente> findClienteById(int id) {
        String sql = "SELECT * FROM cliente WHERE id = ? ";
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, id );
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(rowMapper.mapRow(rs));
            }
        }catch (SQLException e ){
            throw new RuntimeException("Cliente con id " + id + " no existe");
        }
        return Optional.empty();
    }

    @Override
    public Cliente updateCliente(Cliente cliente) {
        String sql = "UPDATE cliente SET identificacion = ?, nombre_completo = ?, celular = ?, usuario = ?, correo = ?, contrasena_hash = ?, intentos_fallidos = ?, bloqueado = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            setClienteParams(ps, cliente);
            ps.setInt(9, cliente.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar cliente", e);
        }
        return cliente;
    }

    @Override
    public void deleteCliente(int id) {
        String sql = "DELETE FROM cliente WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar cliente", e);
        }
    }


    @Override
    public Optional<Cliente> findClienteByUsername(String username) {
        String sql = "SELECT * FROM cliente WHERE usuario = ? ";
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, username );
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(rowMapper.mapRow(rs));
            }
        }catch (SQLException e ){
            throw new RuntimeException("Cliente con usuario " + username + " no existe");
        }
        return Optional.empty();
    }
}
