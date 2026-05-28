package astrobankapp.session;

import astrobankapp.domain.Cliente;
import astrobankapp.services.outputport.SessionPersistencePort;

import java.util.Optional;
import java.util.UUID;

public class Session {
    private Cliente cliente;
    private Integer sessionId;
    private final SessionPersistencePort sessionRepository;

    public Session(SessionPersistencePort sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void start(Cliente cliente) {
        this.cliente = cliente;
        String token = UUID.randomUUID().toString();
        int id = sessionRepository.createSession(cliente.getId(), token);
        this.sessionId = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public boolean isActive() {
        return cliente != null;
    }

    public void end() {
        if (sessionId != null) {
            sessionRepository.endSession(sessionId);
        }
        this.cliente = null;
        this.sessionId = null;
    }

    public Optional<Integer> findActiveSessionForCurrentClient() {
        if (cliente == null) return Optional.empty();
        return sessionRepository.findActiveSessionByClienteId(cliente.getId());
    }
}
