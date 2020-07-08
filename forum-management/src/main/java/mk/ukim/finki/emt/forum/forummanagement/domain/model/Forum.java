package mk.ukim.finki.emt.forum.forummanagement.domain.model;

import lombok.Getter;
import mk.ukim.finki.emt.forum.forummanagement.domain.value.ForumId;
import mk.ukim.finki.emt.forum.forummanagement.domain.value.Title;
import mk.ukim.finki.emt.forum.sharedkernel.domain.base.AbstractEntity;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Entity
@Table(name = "forums")
public class Forum extends AbstractEntity<ForumId> {

    @Embedded
    @AttributeOverride(name = "title", column = @Column(name = "title", nullable = false))
    private Title title;

    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(
            targetEntity = Discussion.class,
            mappedBy = "forum",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private Set<Discussion> discussions;

    public Forum() {
    }

    public Forum(@NonNull Title title, @NonNull String description){
        this.title = title;
        this.description = description;
        this.discussions = new TreeSet<>(Comparator.comparing(Discussion::getTimestampCreated).reversed());
    }

}
