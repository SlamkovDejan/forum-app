package mk.ukim.finki.emt.forum.forummanagement.aplication.form;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ForumForm implements Serializable {

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private boolean autoSubscribeStudents;

    @NotNull
    private boolean canStudentsReply;

}
