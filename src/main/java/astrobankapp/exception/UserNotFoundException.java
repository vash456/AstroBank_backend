package astrobankapp.exception;

public class UserNotFoundException extends ValidationException{
    public UserNotFoundException(String username) {
        super("El usuario '" + username + "' no existe en el sistema.");
    }
}
