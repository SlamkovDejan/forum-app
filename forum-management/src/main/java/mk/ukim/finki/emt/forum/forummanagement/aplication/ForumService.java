package mk.ukim.finki.emt.forum.forummanagement.aplication;

import mk.ukim.finki.emt.forum.forummanagement.aplication.dto.DiscussionDTO;
import mk.ukim.finki.emt.forum.forummanagement.aplication.dto.ForumDTO;
import mk.ukim.finki.emt.forum.forummanagement.aplication.dto.PostReplyDTO;
import mk.ukim.finki.emt.forum.forummanagement.aplication.dto.SubscribeDTO;
import mk.ukim.finki.emt.forum.forummanagement.domain.exception.ForumNotFoundException;
import mk.ukim.finki.emt.forum.forummanagement.domain.exception.PostNotFoundException;
import mk.ukim.finki.emt.forum.forummanagement.domain.exception.UserNotSubscribedException;
import mk.ukim.finki.emt.forum.forummanagement.domain.model.Discussion;
import mk.ukim.finki.emt.forum.forummanagement.domain.model.Forum;
import mk.ukim.finki.emt.forum.forummanagement.domain.model.Post;
import mk.ukim.finki.emt.forum.forummanagement.domain.model.Subscription;
import mk.ukim.finki.emt.forum.forummanagement.domain.repository.DiscussionRepository;
import mk.ukim.finki.emt.forum.forummanagement.domain.repository.ForumRepository;
import mk.ukim.finki.emt.forum.forummanagement.domain.repository.PostRepository;
import mk.ukim.finki.emt.forum.forummanagement.domain.repository.SubscriptionRepository;
import mk.ukim.finki.emt.forum.forummanagement.domain.value.*;
import mk.ukim.finki.emt.forum.sharedkernel.domain.role.RoleName;
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
    private final UserService userService;
    private final RoleService roleService;

    public ForumService(ForumRepository forumRepository,
                        PostRepository postRepository,
                        DiscussionRepository discussionRepository,
                        SubscriptionRepository subscriptionRepository,
                        UserService userService,
                        RoleService roleService) {
        this.forumRepository = forumRepository;
        this.postRepository = postRepository;
        this.discussionRepository = discussionRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.userService = userService;
        this.roleService = roleService;
    }

    //TODO: change who can create discussion

    private Subscription createSubscriptionOnForumDiscussion(Forum forum, UUID discussionId, UUID subscriberId){
        DiscussionId discussion = new DiscussionId(discussionId);
        UserId subscriber = new UserId(subscriberId);
        return forum.subscribeToDiscussion(discussion, subscriber);
    }

    private void autoSubscribe(Forum forum, DiscussionId discussionId, UserId subscriber){
        Optional<Subscription> subscription = this.subscriptionRepository.findFirstByDiscussion_IdAndSubscriber(discussionId, subscriber);
        boolean isSubscribed = subscription.isPresent();
        if(!isSubscribed){
            Subscription newSubscription = this.createSubscriptionOnForumDiscussion(forum, discussionId.getId(), subscriber.getId());
            this.subscriptionRepository.saveAndFlush(newSubscription);
        }
    }

    public Forum createForum(@NonNull ForumDTO forumDTO){
        Title forumTitle = new Title(forumDTO.getTitle());
        Description forumDescription = new Description(forumDTO.getDescription());
        boolean autoSubscribeStudents = forumDTO.isAutoSubscribeStudents();
        boolean canStudentsReply = forumDTO.isCanStudentsReply();

        Forum newForum = new Forum(forumTitle, forumDescription, autoSubscribeStudents, canStudentsReply);
        return forumRepository.saveAndFlush(newForum);
    }

    public Discussion openNewDiscussionOnForum(DiscussionDTO discussionDTO){
        Forum forum = this.forumRepository.findById(new ForumId(discussionDTO.getForumId()))
                .orElseThrow(ForumNotFoundException::new);

        Title initialPostSubject = new Title(discussionDTO.getInitialPost().getSubject());
        Content initialPostContent = new Content(discussionDTO.getInitialPost().getContent());
        UserId initialPostAuthor = new UserId(discussionDTO.getInitialPost().getAuthorId());
        Post initialPostOnDiscussion = forum.createInitialPostForDiscussion(initialPostSubject, initialPostContent,initialPostAuthor);
        this.postRepository.saveAndFlush(initialPostOnDiscussion);
        // TODO: fire event ?

        Username startedBy = new Username(discussionDTO.getStartedByUsername());

        Discussion newDiscussion = forum.openDiscussion(startedBy, initialPostOnDiscussion);
        this.discussionRepository.saveAndFlush(newDiscussion);

        // auto subscribe the starter of the discussion (he's not subscribed because it's the first post of the discussion)
        Subscription newSubscription = this.createSubscriptionOnForumDiscussion(forum, newDiscussion.id().getId(), initialPostAuthor.getId());
        this.subscriptionRepository.saveAndFlush(newSubscription);

        // auto subscribe other students
        if(forum.autoSubscribe()){
            UUID roleId = this.roleService.findRoleIdByRoleName(RoleName.STUDENT);
            this.userService.findAllUserIdsByRoleId(roleId).forEach((userId) -> this.autoSubscribe(forum, newDiscussion.id(), userId));
        }
        return newDiscussion;
    }

    public Post replyOnForumDiscussion(PostReplyDTO postReplyDTO){
        Forum forum = this.forumRepository.findById(new ForumId(postReplyDTO.getForumId()))
                .orElseThrow(ForumNotFoundException::new);

        DiscussionId discussionId = new DiscussionId(postReplyDTO.getDiscussionId());
        Content postContent = new Content(postReplyDTO.getContent());
        Post parentPost = this.postRepository.findById(new PostId(postReplyDTO.getParentPostId()))
                .orElseThrow(PostNotFoundException::new);
        UserId author = new UserId(postReplyDTO.getAuthorId());

        // auto subscribe the author of the post to the discussion (if not already subscribed)
        this.autoSubscribe(forum, discussionId, author);

        // TODO: fire event
        Post newPostReply = forum.replyOnDiscussion(discussionId, postContent, parentPost, author);
        return this.postRepository.saveAndFlush(newPostReply);
    }

    public Set<Discussion> allDiscussionsOnForum(UUID forumId){
        Forum forum = this.forumRepository.findById(new ForumId(forumId))
                .orElseThrow(ForumNotFoundException::new);
        return forum.allDiscussions();
    }

    public Post getDiscussionOnForum(UUID forumId, UUID discussionId){
        Forum forum = this.forumRepository.findById(new ForumId(forumId))
                .orElseThrow(ForumNotFoundException::new);
        return forum.discussion(new DiscussionId(discussionId));
    }

    public Subscription subscribeOnForumDiscussion(SubscribeDTO subscribeDTO){
        Forum forum = this.forumRepository.findById(new ForumId(subscribeDTO.getForumId()))
                .orElseThrow(ForumNotFoundException::new);

        Subscription newSubscription = createSubscriptionOnForumDiscussion(forum, subscribeDTO.getDiscussionId(), subscribeDTO.getSubscriberId());
        return this.subscriptionRepository.saveAndFlush(newSubscription);
    }

    public boolean UnsubscribeFromForumDiscussion(SubscribeDTO subscribeDTO){
        Forum forum = this.forumRepository.findById(new ForumId(subscribeDTO.getForumId()))
                .orElseThrow(ForumNotFoundException::new);

        DiscussionId discussionId = new DiscussionId(subscribeDTO.getDiscussionId());
        UserId subscriber = new UserId(subscribeDTO.getSubscriberId());
        Subscription subscription = this.subscriptionRepository.findFirstByDiscussion_IdAndSubscriber(discussionId, subscriber)
                .orElseThrow(UserNotSubscribedException::new);

        boolean unsubscribed = forum.unsubscribeFromDiscussion(discussionId, subscription);
        if(!unsubscribed)
            throw new UserNotSubscribedException();
        this.subscriptionRepository.deleteById(subscription.id());
        return true;
    }

}
