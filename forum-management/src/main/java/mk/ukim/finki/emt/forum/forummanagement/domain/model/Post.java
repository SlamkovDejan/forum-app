package mk.ukim.finki.emt.forum.forummanagement.domain.model;

import lombok.Getter;
import mk.ukim.finki.emt.forum.forummanagement.domain.value.PostId;
import mk.ukim.finki.emt.forum.forummanagement.domain.value.Title;
import mk.ukim.finki.emt.forum.sharedkernel.domain.base.AbstractEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Entity
@Table(name = "posts")
public class Post extends AbstractEntity<PostId> {

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "timestamp_posted", nullable = false)
    private LocalDateTime timestampPosted;

    @Embedded
    @AttributeOverride(name = "title", column = @Column(name = "subject", nullable = false))
    private Title subject;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Post parent;

    @OneToMany(
            targetEntity = Post.class,
            mappedBy = "parent",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Post> children;

    @ManyToOne
    @JoinColumn(name = "discussion_id")
    private Discussion discussion;

}