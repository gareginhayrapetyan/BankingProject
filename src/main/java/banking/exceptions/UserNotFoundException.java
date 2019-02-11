package banking.exceptions;

public class UserNotFoundException extends Throwable {
    public UserNotFoundException(final String message) {
        super(message);
    }

}
