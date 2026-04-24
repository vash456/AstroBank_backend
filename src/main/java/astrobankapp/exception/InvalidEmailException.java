package astrobankapp.exception;

// Excepciones específicas
public class InvalidEmailException extends ValidationException {
    public InvalidEmailException(String email) {
        super("El formato del correo '" + email + "' no es válido.");
    }
}
