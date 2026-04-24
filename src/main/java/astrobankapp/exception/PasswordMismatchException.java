package astrobankapp.exception;

public class PasswordMismatchException extends ValidationException {
    public PasswordMismatchException() {
        super("La contraseña y la confirmación no coinciden.");
    }
}
