package astrobankapp.services.outputport;

import java.util.Optional;

public interface SessionPersistencePort {
    int createSession(int clienteId, String token);
    void endSession(int sessionId);
    Optional<Integer> findActiveSessionByClienteId(int clienteId);
}
