package astrobankapp.persistence.mapper;

import astrobankapp.domain.Movimiento;
import astrobankapp.domain.enums.TipoMovimiento;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MovimientoRowMapper implements RowMapper<Movimiento> {
    @Override
    public Movimiento mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        TipoMovimiento tipo = TipoMovimiento.valueOf(rs.getString("tipo_nombre"));
        double valor = rs.getDouble("valor");
        double saldoPosterior = rs.getDouble("saldo_posterior");
        String descripcion = rs.getString("descripcion");
        String fechaHora = rs.getString("fecha_hora");
        return new Movimiento(id, tipo, valor, saldoPosterior, descripcion, fechaHora);
    }
}
