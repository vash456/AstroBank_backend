package astrobankapp.exception;

public class IllegalStateException extends ValidationException {
    public IllegalStateException() {
        super("La cuenta no está activa");
    }
}