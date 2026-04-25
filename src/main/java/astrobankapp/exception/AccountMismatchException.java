package astrobankapp.exception;

public class AccountMismatchException extends ValidationException{
    public AccountMismatchException(String numberAccount) {
        super("Cuenta destino '" + numberAccount + "' invalida");
    }
}
