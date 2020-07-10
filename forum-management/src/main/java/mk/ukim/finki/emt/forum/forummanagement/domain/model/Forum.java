package mk.ukim.finki.emt.forum.forummanagement.domain.model;

import mk.ukim.finki.emt.forum.forummanagement.domain.exception.DiscussionNotFoundException;
import mk.ukim.finki.emt.forum.forummanagement.domain.value.*;
import mk.ukim.finki.emt.forum.sharedkernel.domain.base.AbstractEntity;
import mk.ukim.finki.emt.forum.sharedkernel.domain.user.Username;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.*;

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

    @Column(name = "auto_subscribe_students", nullable = false)
    private boolean autoSubscribeStudents;

    @Column(name = "can_students_reply", nullable = false)
    private boolean canStudentsReply;

    public Forum() {
    }

    public Forum(@NonNull Title title, @NonNull Description description,
                 @NonNull boolean autoSubscribeStudents, @NonNull boolean canStudentsReply){
        this.title = title;
        this.description = description;
        this.discussions = new TreeSet<>(Comparator.comparing(Discussion::getTimestampCreated).reversed());
        this.autoSubscribeStudents = autoSubscribeStudents;
        this.canStudentsReply = canStudentsReply;
    }

    private Discussion findDiscussionById(DiscussionId discussionId){
        // all the discussions are already loaded into 'discussions' set because the fetch type is EAGER
        // that's why we can search the set for the desired discussion and not query the database additionally for that discussion
        return discussions.stream()
                .filter(d->d.id().equals(discussionId))
                .findFirst()
                .orElseThrow(DiscussionNotFoundException::new);
    }

    public Post createInitialPostForDiscussion(Title subject, Content content, UserId author){
        return new Post(subject, content, null, author);
    }

    public Discussion openDiscussion(Username startedBy, Post initialPost){
        Discussion newDiscussion = new Discussion(startedBy, initialPost, this);
        this.discussions.add(newDiscussion);
        return newDiscussion;
    }

    public Discussion updateDiscussionLastPost(DiscussionId discussionId, Post newPost, Username authorUsername){
        Discussion discussion = this.findDiscussionById(discussionId);
        return discussion.updateLastPost(newPost, authorUsername);
    }

    public Post replyOnDiscussion(DiscussionId discussionId, Content content, Post parentPost, UserId author){
        Discussion discussion = this.findDiscussionById(discussionId);

        Title postTitle = new Title(String.format("Re: %s", discussion.getTopic().getTitle()));
        Post newPost = new Post(postTitle, content, parentPost, author);
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
            The returning of the root post of a discussion implies returning all associated posts for that discussion
         */
        Discussion discussion = this.findDiscussionById(discussionId);
        return discussion.replies();
    }

    public Subscription subscribeToDiscussion(DiscussionId discussionId, UserId subscriber){
        Discussion discussion = this.findDiscussionById(discussionId);
        return discussion.subscribe(subscriber);
    }

    public boolean unsubscribeFromDiscussion(DiscussionId discussionId, Subscription subscription){
        Discussion discussion = this.findDiscussionById(discussionId);
        return discussion.unsubscribe(subscription);
    }

    public boolean autoSubscribe(){
        return this.autoSubscribeStudents;
    }

    public boolean canStudentsReply(){
        return this.canStudentsReply;
    }

}
