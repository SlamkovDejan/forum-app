package mk.ukim.finki.emt.forum.forummanagement.domain.exception;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException() {
    }

    public PostNotFoundException(String message) {
        super(message);
    }

}
