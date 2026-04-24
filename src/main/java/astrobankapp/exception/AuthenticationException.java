package astrobankapp.exception;

public class AuthenticationException extends ValidationException {
    public AuthenticationException(String message) {
        super(message);
    }
}