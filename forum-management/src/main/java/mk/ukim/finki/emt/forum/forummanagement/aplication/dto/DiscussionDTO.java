package mk.ukim.finki.emt.forum.forummanagement.aplication.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class DiscussionDTO {

    private UUID forumId;

    private String startedByUsername;

    private InitialPostDTO initialPost;

    @Getter
    public static class InitialPostDTO{

        private String subject;

        private String content;

        private UUID authorId;

    }

}
