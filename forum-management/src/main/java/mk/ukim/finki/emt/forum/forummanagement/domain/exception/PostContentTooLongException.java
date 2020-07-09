package mk.ukim.finki.emt.forum.forummanagement.domain.exception;

public class PostContentTooLongException extends RuntimeException {

    public PostContentTooLongException() {
    }

    public PostContentTooLongException(String message) {
        super(message);
    }

}
