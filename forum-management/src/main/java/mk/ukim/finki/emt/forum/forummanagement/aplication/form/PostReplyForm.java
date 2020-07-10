package mk.ukim.finki.emt.forum.forummanagement.aplication.form;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Data
public class PostReplyForm implements Serializable {

    @NotNull
    private UUID ForumId;

    @NotNull
    private UUID discussionId;

    @NotNull
    private UUID parentPostId;

    @NotNull
    private UUID authorId;

    @NotNull
    private String content;

}
