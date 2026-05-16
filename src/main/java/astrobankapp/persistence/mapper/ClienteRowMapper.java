package astrobankapp.persistence.mapper;

import astrobankapp.domain.Cliente;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteRowMapper implements RowMapper<Cliente>{


    @Override
    public Cliente mapRow(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();

        cliente.setId(rs.getInt("id"));
        cliente.setIdentificacion(rs.getString( "identificacion");
        cliente.setNombreCompleto(rs.getString ("nombre_completo");
        cliente.setCelular(rs.getString ("celular");
        cliente.setUsuario(rs.getString ("usuario");
        cliente.setContrasena(rs.getString ("contrasena_hash");
        cliente.setIntentosFallidos(rs.getInt ("intentos_fallidos");
        cliente.setBloqueado(rs.getInt ("bloqueado");

        return cliente;


    }
}
