package mk.ukim.finki.emt.forum.forummanagement.aplication.form;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Data
public class DiscussionForm implements Serializable {

    @NotNull
    private UUID forumId;

    @NotNull
    private String startedByUsername;

    @NotNull
    private InitialPostDTO initialPost;

    @Data
    public static class InitialPostDTO{

        @NotNull
        private String subject;

        @NotNull
        private String content;

        @NotNull
        private UUID authorId;

    }

}
