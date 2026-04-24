package astrobankapp.exception;

public class UserAlreadyExistsException extends ValidationException {
    public UserAlreadyExistsException(String username) {
        super("El nombre de usuario '" + username + "' ya se encuentra registrado.");
    }
}
