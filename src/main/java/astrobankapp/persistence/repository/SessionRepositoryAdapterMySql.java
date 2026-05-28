package astrobankapp.persistence.repository;

import astrobankapp.services.outputport.SessionPersistencePort;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.UUID;

public class SessionRepositoryAdapterMySql implements SessionPersistencePort {
    private final Connection connection;

    public SessionRepositoryAdapterMySql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int createSession(int clienteId, String token) {
        String sql = "INSERT INTO sesion (cliente_id, token, creada_en) VALUES (?,?,NOW())";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, clienteId);
            ps.setString(2, token != null ? token : UUID.randomUUID().toString());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear sesión", e);
        }
        throw new RuntimeException("No se pudo crear la sesión");
    }

    @Override
    public void endSession(int sessionId) {
        String sql = "UPDATE sesion SET cerrada_en = NOW() WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, sessionId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al cerrar la sesión", e);
        }
    }

    @Override
    public Optional<Integer> findActiveSessionByClienteId(int clienteId) {
        String sql = "SELECT id FROM sesion WHERE cliente_id = ? AND cerrada_en IS NULL LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(rs.getInt("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar sesión activa", e);
        }
        return Optional.empty();
    }
}
