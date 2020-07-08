package mk.ukim.finki.emt.forum.forummanagement.domain.model;


import lombok.Getter;
import mk.ukim.finki.emt.forum.forummanagement.domain.value.DiscussionId;
import mk.ukim.finki.emt.forum.forummanagement.domain.value.LastPost;
import mk.ukim.finki.emt.forum.forummanagement.domain.value.Title;
import mk.ukim.finki.emt.forum.sharedkernel.domain.base.AbstractEntity;
import mk.ukim.finki.emt.forum.sharedkernel.domain.user.Username;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "discussions")
public class Discussion extends AbstractEntity<DiscussionId> {


    @Embedded
    @Column(name = "started_by", nullable = false)
    private Username startedBy;

    @Column(name = "number_of_posts", nullable = false)
    private Integer numberOfPosts;

    @Embedded
    @AttributeOverride(name = "timestampCreated", column = @Column(name = "timestamp_last_post", nullable = false))
    private LastPost lastPost;

    @Column(name="timestamp_created", nullable = false)
    private  LocalDateTime timestampCreated;

    @Embedded
    @AttributeOverride(name = "title", column = @Column(name = "topic", nullable = false))
    private Title topic;

    //TODO: Add relations

}
