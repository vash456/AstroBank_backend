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
        return null;
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
