package astrobankapp.exception;

public class AccountMismatchException extends ValidationException{
    public AccountMismatchException(String message) {
        super(message);
    }
}
