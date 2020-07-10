package mk.ukim.finki.emt.forum.forummanagement.aplication;

import mk.ukim.finki.emt.forum.forummanagement.aplication.dto.DiscussionDTO;
import mk.ukim.finki.emt.forum.forummanagement.aplication.dto.ForumDTO;
import mk.ukim.finki.emt.forum.forummanagement.aplication.dto.PostReplyDTO;
import mk.ukim.finki.emt.forum.forummanagement.aplication.dto.SubscribeDTO;
import mk.ukim.finki.emt.forum.forummanagement.domain.model.Discussion;
import mk.ukim.finki.emt.forum.forummanagement.domain.model.Forum;
import mk.ukim.finki.emt.forum.forummanagement.domain.model.Post;
import mk.ukim.finki.emt.forum.forummanagement.domain.model.Subscription;
import mk.ukim.finki.emt.forum.forummanagement.domain.repository.DiscussionRepository;
import mk.ukim.finki.emt.forum.forummanagement.domain.repository.ForumRepository;
import mk.ukim.finki.emt.forum.forummanagement.domain.repository.PostRepository;
import mk.ukim.finki.emt.forum.forummanagement.domain.repository.SubscriptionRepository;
import mk.ukim.finki.emt.forum.forummanagement.domain.value.*;
import mk.ukim.finki.emt.forum.sharedkernel.domain.user.Username;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class ForumService {

    private final ForumRepository forumRepository;
    private final PostRepository postRepository;
    private final DiscussionRepository discussionRepository;
    private final SubscriptionRepository subscriptionRepository;

    public ForumService(ForumRepository forumRepository,
                        PostRepository postRepository,
                        DiscussionRepository discussionRepository,
                        SubscriptionRepository subscriptionRepository) {
        this.forumRepository = forumRepository;
        this.postRepository = postRepository;
        this.discussionRepository = discussionRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    private Subscription createSubscriptionOnForumDiscussion(Forum forum, UUID discussionId, UUID subscriberId){
        DiscussionId discussion = new DiscussionId(discussionId);
        UserId subscriber = new UserId(subscriberId);
        return forum.subscribeToDiscussion(discussion, subscriber);
    }

    // TODO: discuss method names: suffix Forum
    // TODO: discuss flush

    public Forum createForum(@NonNull ForumDTO forumDTO){
        Title forumTitle = new Title(forumDTO.getTitle());
        Description forumDescription = new Description(forumDTO.getDescription());

        Forum newForum = new Forum(forumTitle, forumDescription);
        return forumRepository.save(newForum);
    }

    public Discussion openNewDiscussionOnForum(DiscussionDTO discussionDTO){
        // TODO: custom exception
        Forum forum = this.forumRepository.findById(new ForumId(discussionDTO.getForumId()))
                .orElseThrow(RuntimeException::new);

        Title initialPostSubject = new Title(discussionDTO.getInitialPost().getSubject());
        Content initialPostContent = new Content(discussionDTO.getInitialPost().getContent());
        UserId initialPostAuthor = new UserId(discussionDTO.getInitialPost().getAuthorId());
        Post initialPostOnDiscussion = forum.createInitialPostForDiscussion(initialPostSubject, initialPostContent,initialPostAuthor);
        this.postRepository.save(initialPostOnDiscussion);
        // TODO: fire event ?

        Username startedBy = new Username(discussionDTO.getStartedByUsername());

        Discussion newDiscussion = forum.openDiscussion(startedBy, initialPostOnDiscussion);
        this.discussionRepository.save(newDiscussion);

        // auto subscribe the starter of the discussion
        Subscription newSubscription = this.createSubscriptionOnForumDiscussion(forum, newDiscussion.id().getId(), initialPostAuthor.getId());
        this.subscriptionRepository.save(newSubscription);

        return newDiscussion;
    }

    public Post replyOnForumDiscussion(PostReplyDTO postReplyDTO){
        // TODO: custom exception
        Forum forum = this.forumRepository.findById(new ForumId(postReplyDTO.getForumId()))
                .orElseThrow(RuntimeException::new);

        DiscussionId discussionId = new DiscussionId(postReplyDTO.getDiscussionId());
        Content postContent = new Content(postReplyDTO.getContent());
        // TODO: custom exception
        Post parentPost = this.postRepository.findById(new PostId(postReplyDTO.getParentPostId()))
                .orElseThrow(RuntimeException::new);
        UserId author = new UserId(postReplyDTO.getAuthorId());

        // auto subscribe the author of the post to the discussion (if not already subscribed)
        Optional<Subscription> subscription = this.subscriptionRepository.findFirstByDiscussion_IdAndSubscriber(discussionId, author);
        boolean isSubscribed = subscription.isPresent();
        if(!isSubscribed){
            Subscription newSubscription = this.createSubscriptionOnForumDiscussion(forum, discussionId.getId(), author.getId());
            this.subscriptionRepository.save(newSubscription);
        }

        // TODO: fire event
        Post newPostReply = forum.replyOnDiscussion(discussionId, postContent, parentPost, author);
        return this.postRepository.save(newPostReply);
    }

    public Set<Discussion> allDiscussionsOnForum(UUID forumId){
        // TODO: custom exception
        Forum forum = this.forumRepository.findById(new ForumId(forumId))
                .orElseThrow(RuntimeException::new);
        return forum.allDiscussions();
    }

    // TODO: naming here
    public Post getDiscussionOnForum(UUID forumId, UUID discussionId){
        // TODO: custom exception
        Forum forum = this.forumRepository.findById(new ForumId(forumId))
                .orElseThrow(RuntimeException::new);
        return forum.discussion(new DiscussionId(discussionId));
    }

    public Subscription subscribeOnForumDiscussion(SubscribeDTO subscribeDTO){
        // TODO: custom exception
        Forum forum = this.forumRepository.findById(new ForumId(subscribeDTO.getForumId()))
                .orElseThrow(RuntimeException::new);

        Subscription newSubscription = createSubscriptionOnForumDiscussion(forum, subscribeDTO.getDiscussionId(), subscribeDTO.getSubscriberId());
        return this.subscriptionRepository.save(newSubscription);
    }

    // TODO: the name of the DTO; they hold same things
    public boolean UnsubscribeFromForumDiscussion(SubscribeDTO subscribeDTO){
        // TODO: custom exception
        Forum forum = this.forumRepository.findById(new ForumId(subscribeDTO.getForumId()))
                .orElseThrow(RuntimeException::new);

        DiscussionId discussionId = new DiscussionId(subscribeDTO.getDiscussionId());
        UserId subscriber = new UserId(subscribeDTO.getSubscriberId());
        Subscription subscription = this.subscriptionRepository.findFirstByDiscussion_IdAndSubscriber(discussionId, subscriber)
                .orElseThrow(RuntimeException::new); // TODO: custom exception

        boolean unsubscribed = forum.unsubscribeFromDiscussion(discussionId, subscription);
        if(!unsubscribed)
            throw new RuntimeException(); // TODO: custom exception
        this.subscriptionRepository.deleteById(subscription.id());
        return true;
    }

}
