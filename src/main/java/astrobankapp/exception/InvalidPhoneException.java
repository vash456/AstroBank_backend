package astrobankapp.exception;

public class InvalidPhoneException extends ValidationException {
    public InvalidPhoneException(String phone) {
        super("El teléfono '" + phone + "' debe tener exactamente 10 dígitos.");
    }
}
