package mk.ukim.finki.emt.forum.forummanagement.domain.model;


import lombok.Getter;
import mk.ukim.finki.emt.forum.forummanagement.domain.value.DiscussionId;
import mk.ukim.finki.emt.forum.forummanagement.domain.value.LastPostInfo;
import mk.ukim.finki.emt.forum.forummanagement.domain.value.Title;
import mk.ukim.finki.emt.forum.sharedkernel.domain.base.AbstractEntity;
import mk.ukim.finki.emt.forum.sharedkernel.domain.user.Username;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Entity
@Table(name = "discussions")
public class Discussion extends AbstractEntity<DiscussionId> {

    @Embedded
    @AttributeOverride(name = "username", column = @Column(name = "started_by", nullable = false))
    private Username startedBy;

    @Column(name = "number_of_posts", nullable = false)
    private Integer numberOfPosts;

    @Embedded
    private LastPostInfo lastPostInfo;

    @Column(name="timestamp_created", nullable = false)
    private  LocalDateTime timestampCreated;

    @Embedded
    @AttributeOverride(name = "title", column = @Column(name = "topic", nullable = false))
    private Title topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forum_id", nullable = false)
    private Forum forum;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "initial_post_id", nullable = false)
    private Post initialPost;

    public Discussion() {
        super();
    }

    public Discussion(@NonNull Username startedBy, @NonNull Post initialPost, @NonNull Forum forum){
        super(new DiscussionId());
        this.startedBy = startedBy;
        this.initialPost = initialPost;
        this.forum = forum;
        this.topic = initialPost.getSubject();
        this.timestampCreated = initialPost.getTimestampPosted();
        this.lastPostInfo = new LastPostInfo(startedBy, this.timestampCreated);
        this.numberOfPosts = 1;
    }

    void reply(Post parentPost, @NonNull Post newPost){
        parentPost.addReply(newPost);
    }

    Post replies(){
        /*
            The posts in a discussion are structured like a tree.
            Every node represents a post and every child of that node represents a reply to that post.
            The children of one node are sorted in ascending order by time created (TreeSet).
            The tree structure in the database is implemented with the recurrent relation in the Post entity.
         */
        return this.initialPost;
    }

}
