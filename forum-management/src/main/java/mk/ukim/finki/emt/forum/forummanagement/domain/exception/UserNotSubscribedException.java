package mk.ukim.finki.emt.forum.forummanagement.domain.exception;

public class UserNotSubscribedException extends RuntimeException {

    public UserNotSubscribedException() {
    }

    public UserNotSubscribedException(String message) {
        super(message);
    }
    
}
