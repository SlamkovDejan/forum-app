package mk.ukim.finki.emt.forum.usermanagement.domain.exception;

public class InvalidEmailException extends RuntimeException {

    public InvalidEmailException() {
    }

    public InvalidEmailException(String message) {
        super(message);
    }
}
