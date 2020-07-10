package mk.ukim.finki.emt.forum.usermanagement.domain.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
