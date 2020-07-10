package mk.ukim.finki.emt.forum.forummanagement.aplication.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class PostReplyDTO {

    private UUID ForumId;

    private UUID discussionId;

    private UUID parentPostId;

    private UUID authorId;

    private String content;

}
