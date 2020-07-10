package mk.ukim.finki.emt.forum.forummanagement.domain.exception;

public class UserNotAuthorizedException extends RuntimeException {

    public UserNotAuthorizedException() {
    }

    public UserNotAuthorizedException(String message) {
        super(message);
    }
}
