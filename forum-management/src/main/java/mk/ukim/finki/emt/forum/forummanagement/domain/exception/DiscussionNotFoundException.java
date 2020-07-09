package mk.ukim.finki.emt.forum.forummanagement.domain.exception;

public class DiscussionNotFoundException extends RuntimeException {

    public DiscussionNotFoundException() {
    }

    public DiscussionNotFoundException(String message) {
        super(message);
    }

}
