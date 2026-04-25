package astrobankapp.exception;

public class InvalidAmountException extends ValidationException {
    public InvalidAmountException(String message) {
        super(message);
    }
}