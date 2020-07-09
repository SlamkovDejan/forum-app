package mk.ukim.finki.emt.forum.forummanagement.domain.model;

import lombok.Getter;
import mk.ukim.finki.emt.forum.forummanagement.domain.value.Content;
import mk.ukim.finki.emt.forum.forummanagement.domain.value.PostId;
import mk.ukim.finki.emt.forum.forummanagement.domain.value.Title;
import mk.ukim.finki.emt.forum.sharedkernel.domain.base.AbstractEntity;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Entity
@Table(name = "posts")
public class Post extends AbstractEntity<PostId> {

    @Embedded
    private Content content;

    @Column(name = "timestamp_posted", nullable = false)
    private LocalDateTime timestampPosted;

    @Embedded
    @AttributeOverride(name = "title", column = @Column(name = "subject", nullable = false))
    private Title subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_post_id")
    private Post parentPost;

    @OneToMany(
            targetEntity = Post.class,
            mappedBy = "parentPost",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Post> subPosts;

    public Post() {
        super();
    }

    public Post(@NonNull Title subject, @NonNull Content content, Post parentPost){
        super(new PostId());
        this.subPosts = new TreeSet<>(Comparator.comparing(Post::getTimestampPosted));
        this.timestampPosted = LocalDateTime.now();
        this.subject = subject;
        this.content = content;
        this.parentPost = parentPost;
    }

    void addReply(@NonNull Post post) {
        subPosts.add(post);
    }

}
