package mk.ukim.finki.emt.forum.usermanagement.domain.exception;

public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException() {
    }

    public RoleNotFoundException(String message) {
        super(message);
    }
}
