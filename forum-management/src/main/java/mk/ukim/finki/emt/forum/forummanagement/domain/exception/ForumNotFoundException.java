package mk.ukim.finki.emt.forum.forummanagement.domain.exception;

public class ForumNotFoundException extends RuntimeException{

    public ForumNotFoundException() {
    }

    public ForumNotFoundException(String message) {
        super(message);
    }

}
