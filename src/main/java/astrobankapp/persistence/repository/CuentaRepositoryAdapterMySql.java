package astrobankapp.persistence.repository;

import astrobankapp.domain.Cliente;
import astrobankapp.domain.Cuenta;
import astrobankapp.domain.CuentaAhorros;
import astrobankapp.domain.CuentaCorriente;
import astrobankapp.domain.TarjetaCredito;
import astrobankapp.domain.enums.EstadoCuenta;
import astrobankapp.persistence.database.DataBaseConnectionMySql;
import astrobankapp.persistence.mapper.ClienteRowMapper;
import astrobankapp.persistence.mapper.CuentaRowMapper;
import astrobankapp.services.outputport.CuentaPersistensePort;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CuentaRepositoryAdapterMySql implements CuentaPersistensePort {
    private final Connection connection;
    private final CuentaRowMapper cuentaRowMapper;

    public CuentaRepositoryAdapterMySql(Connection connection, CuentaRowMapper cuentaRowMapper) {
        this.connection = connection;
        this.cuentaRowMapper = cuentaRowMapper;
    }

    @Override
    public Cuenta saveCuenta(Cuenta cuenta) {
        String sql = "INSERT INTO cuenta (numero_cuenta, saldo, fecha_apertura, estado_cuenta_id, tipo_cuenta, cliente_id) VALUES (?,?,?,?,?,?)";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, cuenta.getNumeroCuenta());
                ps.setDouble(2, cuenta.consultarSaldo());
                ps.setString(3, cuenta.getFechaApertura());
                ps.setInt(4, mapEstadoCuentaId(cuenta.getEstadoCuenta()));
                ps.setString(5, mapTipoCuenta(cuenta));
                ps.setInt(6, cuenta.getPropietario().getId());
                ps.executeUpdate();
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int cuentaId = generatedKeys.getInt(1);
                    insertSubclassData(cuenta, cuentaId);

                } else {
                    throw new SQLException("No se pudo obtener el id generado para la cuenta");
                }
            }
            connection.commit();
        } catch (SQLException e) {
            rollbackSilently();
            throw new RuntimeException("Error al guardar la cuenta", e);
        } finally {
            restoreAutoCommit();
        }
        return cuenta;
    }

    @Override
    public List<Cuenta> findAllCuentas() {
        List<Cuenta> cuentas = new ArrayList<>();
        String sql = "SELECT c.id, c.numero_cuenta, c.saldo, c.fecha_apertura, e.nombre AS estado_nombre, c.tipo_cuenta, c.cliente_id FROM cuenta c INNER JOIN estado_cuenta e ON c.estado_cuenta_id = e.id";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cuentas.add(cuentaRowMapper.mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al cargar las cuentas", e);
        }
        return cuentas;
    }

    @Override
    public Optional<Cuenta> findCuentaById(int id) {
        String sql = "SELECT c.id, c.numero_cuenta, c.saldo, c.fecha_apertura, e.nombre AS estado_nombre, c.tipo_cuenta, c.cliente_id FROM cuenta c INNER JOIN estado_cuenta e ON c.estado_cuenta_id = e.id WHERE c.id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(cuentaRowMapper.mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar la cuenta por id", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Cuenta> findCuentaByNumeroCuenta(String numeroCuenta) {
        String sql = "SELECT c.id, c.numero_cuenta, c.saldo, c.fecha_apertura, e.nombre AS estado_nombre, c.tipo_cuenta, c.cliente_id FROM cuenta c INNER JOIN estado_cuenta e ON c.estado_cuenta_id = e.id WHERE c.numero_cuenta = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, numeroCuenta.trim());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(cuentaRowMapper.mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar la cuenta por numero de cuenta", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Cuenta> findCuentasByClienteId(int clienteId) {
        List<Cuenta> cuentas = new ArrayList<>();
        String sql = "SELECT c.id, c.numero_cuenta, c.saldo, c.fecha_apertura, e.nombre AS estado_nombre, c.tipo_cuenta, c.cliente_id FROM cuenta c INNER JOIN estado_cuenta e ON c.estado_cuenta_id = e.id WHERE c.cliente_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cuentas.add(cuentaRowMapper.mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar las cuentas del cliente", e);
        }
        return cuentas;
    }

    @Override
    public Cuenta updateCuenta(Cuenta cuenta) {
        String sql = "UPDATE cuenta SET saldo = ?, estado_cuenta_id = ? WHERE numero_cuenta = ?";
        boolean originalAutoCommit;
        try {
            originalAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setDouble(1, cuenta.consultarSaldo());
                ps.setInt(2, mapEstadoCuentaId(cuenta.getEstadoCuenta()));
                ps.setString(3, cuenta.getNumeroCuenta());
                int updatedRows = ps.executeUpdate();
                if (updatedRows == 0) {
                    throw new SQLException("No se encontró la cuenta para actualizar");
                }
                updateSubclassData(cuenta);
            }
            connection.commit();
        } catch (SQLException e) {
            rollbackSilently();
            throw new RuntimeException("Error al actualizar la cuenta", e);
        } finally {
            restoreAutoCommit();
        }
        return cuenta;
    }

    @Override
    public void actualizarCuentas(Cuenta cuentaOrigen, Cuenta cuentaDestino) {
        boolean originalAutoCommit;
        try {
            originalAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            updateCuentaFields(cuentaOrigen);
            updateSubclassData(cuentaOrigen);
            updateCuentaFields(cuentaDestino);
            updateSubclassData(cuentaDestino);
            connection.commit();
        } catch (SQLException e) {
            rollbackSilently();
            throw new RuntimeException("Error al actualizar cuentas en transferencia", e);
        } finally {
            restoreAutoCommit();
        }
    }

    private void updateCuentaFields(Cuenta cuenta) throws SQLException {
        String sql = "UPDATE cuenta SET saldo = ?, estado_cuenta_id = ? WHERE numero_cuenta = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, cuenta.consultarSaldo());
            ps.setInt(2, mapEstadoCuentaId(cuenta.getEstadoCuenta()));
            ps.setString(3, cuenta.getNumeroCuenta());
            int updatedRows = ps.executeUpdate();
            if (updatedRows == 0) {
                throw new SQLException("No se encontró la cuenta para actualizar");
            }
        }
    }

    @Override
    public void deleteCuenta(int id) {
        String sql = "DELETE FROM cuenta WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la cuenta", e);
        }
    }

    private void insertSubclassData(Cuenta cuenta, int cuentaId) throws SQLException {
        if (cuenta instanceof CuentaAhorros ahorros) {
            String sql = "INSERT INTO cuenta_ahorros (cuenta_id, tasa_interes) VALUES (?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, cuentaId);
                ps.setDouble(2, ahorros.getTasaInteres());
                ps.executeUpdate();
            }
            return;
        }
        if (cuenta instanceof CuentaCorriente corriente) {
            String sql = "INSERT INTO cuenta_corriente (cuenta_id, porcentaje_sobregiro, limite_sobregiro) VALUES (?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, cuentaId);
                ps.setDouble(2, corriente.getPorcentajeSobregiro());
                ps.setDouble(3, corriente.getLimiteSobregiro());
                ps.executeUpdate();
            }
            return;
        }
        if (cuenta instanceof TarjetaCredito tarjetaCredito) {
            String sql = "INSERT INTO tarjeta_credito (cuenta_id, cupo, deuda, numero_cuotas) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, cuentaId);
                ps.setDouble(2, tarjetaCredito.getCupo());
                ps.setDouble(3, tarjetaCredito.getDeuda());
                ps.setInt(4, tarjetaCredito.getNumeroCuotas());
                ps.executeUpdate();
            }
            return;
        }
        throw new IllegalArgumentException("No se puede guardar el tipo de cuenta: " + cuenta.getClass().getSimpleName());
    }

    private void updateSubclassData(Cuenta cuenta) throws SQLException {
        if (cuenta instanceof CuentaAhorros ahorros) {
            String sql = "UPDATE cuenta_ahorros SET tasa_interes = ? WHERE cuenta_id = (SELECT id FROM cuenta WHERE numero_cuenta = ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setDouble(1, ahorros.getTasaInteres());
                ps.setString(2, cuenta.getNumeroCuenta());
                ps.executeUpdate();
            }
            return;
        }
        if (cuenta instanceof CuentaCorriente corriente) {
            String sql = "UPDATE cuenta_corriente SET porcentaje_sobregiro = ?, limite_sobregiro = ? WHERE cuenta_id = (SELECT id FROM cuenta WHERE numero_cuenta = ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setDouble(1, corriente.getPorcentajeSobregiro());
                ps.setDouble(2, corriente.getLimiteSobregiro());
                ps.setString(3, cuenta.getNumeroCuenta());
                ps.executeUpdate();
            }
            return;
        }
        if (cuenta instanceof TarjetaCredito tarjetaCredito) {
            String sql = "UPDATE tarjeta_credito SET cupo = ?, deuda = ?, numero_cuotas = ? WHERE cuenta_id = (SELECT id FROM cuenta WHERE numero_cuenta = ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setDouble(1, tarjetaCredito.getCupo());
                ps.setDouble(2, tarjetaCredito.getDeuda());
                ps.setInt(3, tarjetaCredito.getNumeroCuotas());
                ps.setString(4, cuenta.getNumeroCuenta());
                ps.executeUpdate();
            }
            return;
        }
        throw new IllegalArgumentException("No se puede actualizar el tipo de cuenta: " + cuenta.getClass().getSimpleName());
    }

    private int mapEstadoCuentaId(EstadoCuenta estadoCuenta) {
        return switch (estadoCuenta) {
            case ACTIVA -> 1;
            case INACTIVA -> 2;
            case BLOQUEADA -> 3;
            case CERRADA -> 4;
        };
    }

    private String mapTipoCuenta(Cuenta cuenta) {
        if (cuenta instanceof CuentaAhorros) {
            return "AHORROS";
        }
        if (cuenta instanceof CuentaCorriente) {
            return "CORRIENTE";
        }
        if (cuenta instanceof TarjetaCredito) {
            return "TARJETA_CREDITO";
        }
        throw new IllegalArgumentException("Tipo de cuenta desconocido: " + cuenta.getClass().getSimpleName());
    }



    private void rollbackSilently() {
        try {
            if (connection != null && !connection.getAutoCommit()) {
                connection.rollback();
            }
        } catch (SQLException ignored) {
        }
    }

    private void restoreAutoCommit() {
        try {
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        } catch (SQLException ignored) {
        }
    }
}
