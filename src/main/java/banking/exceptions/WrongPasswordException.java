package banking.exceptions;

public class WrongPasswordException extends Throwable {
    public WrongPasswordException(final String message) {
        super(message);
    }

}
