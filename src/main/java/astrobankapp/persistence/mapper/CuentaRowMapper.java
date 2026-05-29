package astrobankapp.persistence.mapper;

import astrobankapp.domain.Cliente;
import astrobankapp.domain.Cuenta;
import astrobankapp.domain.CuentaAhorros;
import astrobankapp.domain.CuentaCorriente;
import astrobankapp.domain.TarjetaCredito;
import astrobankapp.domain.Movimiento;
import astrobankapp.domain.enums.EstadoCuenta;
import astrobankapp.exception.AuthenticationException;
import astrobankapp.services.outputport.ClientePersistencePort;
import astrobankapp.services.outputport.CuentaPersistensePort;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CuentaRowMapper implements RowMapper<Cuenta>{
    private final Connection connection;
    private final ClientePersistencePort clienteRepository;

    public CuentaRowMapper(Connection connection, ClientePersistencePort clienteRepository) {
        this.connection = connection;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Cuenta mapRow(ResultSet rs) throws SQLException {
        int cuentaId = rs.getInt("id");
        String numeroCuenta = rs.getString("numero_cuenta");
        double saldo = rs.getDouble("saldo");
        String fechaApertura = rs.getString("fecha_apertura");
        EstadoCuenta estadoCuenta = EstadoCuenta.valueOf(rs.getString("estado_nombre"));
        String tipoCuenta = rs.getString("tipo_cuenta");
        int clienteId = rs.getInt("cliente_id");

        // Obtener el cliente propietario
        Cliente propietario = clienteRepository.findClienteById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente Id no encontrado para la cuenta")); ;
        if (propietario == null) {
            throw new RuntimeException("Cliente propietario no encontrado para la cuenta " + numeroCuenta);
        }

        // Crear instancia según el tipo de cuenta
        Cuenta cuenta;
        switch (tipoCuenta) {
            case "AHORROS" -> cuenta = loadCuentaAhorros(cuentaId, numeroCuenta, propietario, estadoCuenta, saldo);
            case "CORRIENTE" -> cuenta = loadCuentaCorriente(cuentaId, numeroCuenta, propietario, estadoCuenta, saldo);
            case "TARJETA_CREDITO" -> cuenta = loadTarjetaCredito(cuentaId, numeroCuenta, propietario, estadoCuenta, saldo);
            default -> throw new RuntimeException("Tipo de cuenta desconocido: " + tipoCuenta);
        }

        cuenta.setFechaApertura(fechaApertura);
        cuenta.setMovimientos(loadMovimientos(cuentaId));
        return cuenta;
    }

    private Cuenta loadCuentaAhorros(int cuentaId, String numeroCuenta, Cliente propietario, EstadoCuenta estadoCuenta, double saldo) throws SQLException {
        String sql = "SELECT tasa_interes FROM cuenta_ahorros WHERE cuenta_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, cuentaId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new CuentaAhorros(numeroCuenta, propietario, estadoCuenta, saldo, rs.getDouble("tasa_interes"));
            }
        }
        throw new RuntimeException("No se encontró la entrada de cuenta ahorros para cuenta_id=" + cuentaId);
    }

    private Cuenta loadCuentaCorriente(int cuentaId, String numeroCuenta, Cliente propietario, EstadoCuenta estadoCuenta, double saldo) throws SQLException {
        String sql = "SELECT porcentaje_sobregiro, limite_sobregiro FROM cuenta_corriente WHERE cuenta_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, cuentaId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new CuentaCorriente(numeroCuenta, propietario, estadoCuenta, saldo, rs.getDouble("porcentaje_sobregiro"), rs.getDouble("limite_sobregiro"));
            }
        }
        throw new RuntimeException("No se encontró la entrada de cuenta corriente para cuenta_id=" + cuentaId);
    }

    private Cuenta loadTarjetaCredito(int cuentaId, String numeroCuenta, Cliente propietario, EstadoCuenta estadoCuenta, double saldo) throws SQLException {
        String sql = "SELECT cupo, deuda, numero_cuotas FROM tarjeta_credito WHERE cuenta_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, cuentaId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new TarjetaCredito(numeroCuenta, propietario, estadoCuenta, saldo, rs.getDouble("cupo"), rs.getDouble("deuda"), rs.getInt("numero_cuotas"));
            }
        }
        throw new RuntimeException("No se encontró la entrada de tarjeta de crédito para cuenta_id=" + cuentaId);
    }

    private ArrayList<Movimiento> loadMovimientos(int cuentaId) throws SQLException {
        String sql = "SELECT m.id, tm.nombre AS tipo_nombre, m.valor, m.saldo_posterior, m.descripcion, m.fecha_hora " +
                     "FROM movimiento m INNER JOIN tipo_movimiento tm ON m.tipo_movimiento_id = tm.id " +
                     "WHERE m.cuenta_id = ? ORDER BY m.fecha_hora ASC";
        ArrayList<Movimiento> movimientos = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, cuentaId);
            ResultSet rs = ps.executeQuery();
            MovimientoRowMapper movimientoRowMapper = new MovimientoRowMapper();
            while (rs.next()) {
                movimientos.add(movimientoRowMapper.mapRow(rs));
            }
        }
        return movimientos;
    }
}
