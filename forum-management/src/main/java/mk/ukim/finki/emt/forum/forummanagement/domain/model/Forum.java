package mk.ukim.finki.emt.forum.forummanagement.domain.model;

import lombok.Getter;
import mk.ukim.finki.emt.forum.forummanagement.domain.value.ForumId;
import mk.ukim.finki.emt.forum.forummanagement.domain.value.Title;
import mk.ukim.finki.emt.forum.sharedkernel.domain.base.AbstractEntity;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "forums")
public class Forum extends AbstractEntity<ForumId> {

    @Embedded
    @AttributeOverride(name = "title", column = @Column(name = "title", nullable = false))
    private Title title;

    @Column(name = "description", nullable = false)
    private String description;

    //TODO: Add relations
}
