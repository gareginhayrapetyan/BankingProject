package banking.exceptions;

public class UserNameExistException extends Throwable {

    public UserNameExistException(final String message) {
        super(message);
    }

}
