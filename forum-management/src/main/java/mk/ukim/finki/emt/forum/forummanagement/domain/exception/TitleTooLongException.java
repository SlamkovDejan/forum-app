package mk.ukim.finki.emt.forum.forummanagement.domain.exception;

public class TitleTooLongException extends RuntimeException {

    public TitleTooLongException() {
    }

    public TitleTooLongException(String message) {
        super(message);
    }

}
