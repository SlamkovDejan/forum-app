package mk.ukim.finki.emt.forum.forummanagement.aplication.dto;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class ForumDTO implements Serializable {

    private String title;

    private String description;

    private boolean autoSubscribeStudents;

    private boolean canStudentsReply;

}
