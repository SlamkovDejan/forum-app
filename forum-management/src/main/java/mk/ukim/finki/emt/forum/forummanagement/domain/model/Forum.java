package mk.ukim.finki.emt.forum.forummanagement.domain.model;

import lombok.Getter;
import mk.ukim.finki.emt.forum.forummanagement.domain.exception.DiscussionNotFoundException;
import mk.ukim.finki.emt.forum.forummanagement.domain.value.*;
import mk.ukim.finki.emt.forum.sharedkernel.domain.base.AbstractEntity;
import mk.ukim.finki.emt.forum.sharedkernel.domain.user.Username;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.*;

@Getter
@Entity
@Table(name = "forums")
public class Forum extends AbstractEntity<ForumId> {

    @Embedded
    @AttributeOverride(name = "title", column = @Column(name = "title", nullable = false))
    private Title title;

    @Embedded
    private Description description;

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

    public Forum(@NonNull Title title, @NonNull Description description){
        this.title = title;
        this.description = description;
        this.discussions = new TreeSet<>(Comparator.comparing(Discussion::getTimestampCreated).reversed());
    }

    private Discussion findDiscussionById(DiscussionId discussionId){
        return discussions.stream()
                .filter(d->d.id().equals(discussionId))
                .findFirst()
                .orElseThrow(DiscussionNotFoundException::new);
    }

    public Post createInitialPostForDiscussion(Title subject, Content content){
        return new Post(subject, content, null);
    }

    public Discussion openDiscussion(Username startedBy, Post initialPost){
        Discussion newDiscussion = new Discussion(startedBy, initialPost, this);
        this.discussions.add(newDiscussion);
        return newDiscussion;
    }

    public Post replyOnDiscussion(DiscussionId discussionId, Content content, Post parentPost){
        Discussion discussion = this.findDiscussionById(discussionId);

        Title postTitle = new Title(String.format("Re: %s", discussion.getTopic().getTitle()));
        Post newPost = new Post(postTitle, content, parentPost);
        discussion.reply(parentPost, newPost);

        return newPost;
    }

    public Set<Discussion> allDiscussions(){
        // The discussions are sorted in descending order by time created, by storing them in a TreeSet.
        return discussions;
    }

    public Post discussion(DiscussionId discussionId){
        /*
            The posts in a discussion are structured like a tree.
            Every node represents a post and every child of that node represents a reply to that post.
            The children of one node are sorted in ascending order by time created (TreeSet).
            The tree structure in the database is implemented with the recurrent relation in the Post entity.
            The returning of the root is sufficient for the
         */
        Discussion discussion = this.findDiscussionById(discussionId);
        return discussion.replies();
    }

}
