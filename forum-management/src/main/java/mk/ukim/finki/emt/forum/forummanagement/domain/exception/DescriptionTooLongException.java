package mk.ukim.finki.emt.forum.forummanagement.domain.exception;

public class DescriptionTooLongException extends RuntimeException {

    public DescriptionTooLongException() {
    }

    public DescriptionTooLongException(String message) {
        super(message);
    }

}
