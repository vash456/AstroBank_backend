package astrobankapp.exception;

public class FieldRequiredException extends ValidationException {
    public FieldRequiredException(String fieldName) {
        super("El campo '" + fieldName + "' es obligatorio.");
    }
}
