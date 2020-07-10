package mk.ukim.finki.emt.forum.forummanagement.aplication;

import mk.ukim.finki.emt.forum.forummanagement.aplication.form.DiscussionForm;
import mk.ukim.finki.emt.forum.forummanagement.aplication.form.ForumForm;
import mk.ukim.finki.emt.forum.forummanagement.aplication.form.PostReplyForm;
import mk.ukim.finki.emt.forum.forummanagement.aplication.dto.SubscriptionDTO;
import mk.ukim.finki.emt.forum.forummanagement.domain.exception.*;
import mk.ukim.finki.emt.forum.forummanagement.domain.model.Discussion;
import mk.ukim.finki.emt.forum.forummanagement.domain.model.Forum;
import mk.ukim.finki.emt.forum.forummanagement.domain.model.Post;
import mk.ukim.finki.emt.forum.forummanagement.domain.model.Subscription;
import mk.ukim.finki.emt.forum.forummanagement.domain.repository.DiscussionRepository;
import mk.ukim.finki.emt.forum.forummanagement.domain.repository.ForumRepository;
import mk.ukim.finki.emt.forum.forummanagement.domain.repository.PostRepository;
import mk.ukim.finki.emt.forum.forummanagement.domain.repository.SubscriptionRepository;
import mk.ukim.finki.emt.forum.forummanagement.domain.value.*;
import mk.ukim.finki.emt.forum.sharedkernel.domain.email.NotifySubscribersMessage;
import mk.ukim.finki.emt.forum.sharedkernel.domain.role.RoleName;
import mk.ukim.finki.emt.forum.sharedkernel.domain.user.Username;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ForumService {

    private final ForumRepository forumRepository;
    private final PostRepository postRepository;
    private final DiscussionRepository discussionRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;
    private final RoleService roleService;
    private final KafkaTemplate<String, NotifySubscribersMessage> kafkaTemplate;

    public ForumService(ForumRepository forumRepository,
                        PostRepository postRepository,
                        DiscussionRepository discussionRepository,
                        SubscriptionRepository subscriptionRepository,
                        UserService userService,
                        RoleService roleService,
                        KafkaTemplate<String, NotifySubscribersMessage> kafkaTemplate) {
        this.forumRepository = forumRepository;
        this.postRepository = postRepository;
        this.discussionRepository = discussionRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.userService = userService;
        this.roleService = roleService;
        this.kafkaTemplate = kafkaTemplate;
    }

    private boolean isStudent(@NonNull UserId userId){
        UUID roleId = userService.findRoleIdByUserId(userId.getId());
        RoleName roleName = roleService.findRoleNameByRoleId(roleId);
        return roleName.equals(RoleName.STUDENT);
    }

    private Subscription createSubscriptionOnForumDiscussion(
            @NonNull Forum forum,@NonNull UUID discussionId,@NonNull UUID subscriberId){
        DiscussionId discussion = new DiscussionId(discussionId);
        UserId subscriber = new UserId(subscriberId);
        return forum.subscribeToDiscussion(discussion, subscriber);
    }

    private void autoSubscribe(@NonNull Forum forum,@NonNull DiscussionId discussionId,@NonNull UserId subscriber){
        Optional<Subscription> subscription = this.subscriptionRepository.findFirstByDiscussion_IdAndSubscriber(discussionId, subscriber);
        boolean isSubscribed = subscription.isPresent();
        if(!isSubscribed){
            Subscription newSubscription = this.createSubscriptionOnForumDiscussion(forum, discussionId.getId(), subscriber.getId());
            this.subscriptionRepository.saveAndFlush(newSubscription);
        }
    }

    private NotifySubscribersMessage constructMessage(DiscussionId discussionId, Content contentFromPost){
        NotifySubscribersMessage notifySubscribersMessage = new NotifySubscribersMessage();

        List<UUID> usersToNotify = this.subscriptionRepository.findAllByDiscussion_Id(discussionId)
                .map(subscription -> subscription.id().getId())
                .collect(Collectors.toList());
        String text = contentFromPost.getContent();

        Title discussionTopic = this.discussionRepository.findById(discussionId)
                .orElseThrow(DiscussionNotFoundException::new).getTopic();

        String subject = String.format("New reply on discussion: %s", discussionTopic.getTitle());

        notifySubscribersMessage.setSubject(subject);
        notifySubscribersMessage.setText(text);
        notifySubscribersMessage.setUserIds(usersToNotify);

        return notifySubscribersMessage;
    }

    public Forum createForum(@NonNull UserId loggedUser, @NonNull ForumForm forumForm){
        if(isStudent(loggedUser)){
            throw new UserNotAuthorizedException();
        }
        Title forumTitle = new Title(forumForm.getTitle());
        Description forumDescription = new Description(forumForm.getDescription());
        boolean autoSubscribeStudents = forumForm.isAutoSubscribeStudents();
        boolean canStudentsReply = forumForm.isCanStudentsReply();

        Forum newForum = new Forum(forumTitle, forumDescription, autoSubscribeStudents, canStudentsReply);
        return forumRepository.saveAndFlush(newForum);
    }

    public Forum enableStudentReplies(@NonNull UUID loggedUserId, @NonNull UUID forumId){
        if(isStudent(new UserId(loggedUserId))) {
            throw new UserNotAuthorizedException();
        }

        Forum forum = this.forumRepository.findById(new ForumId(forumId))
                .orElseThrow(ForumNotFoundException::new);

        forum.updateCanStudentsReply(true);
        forumRepository.saveAndFlush(forum);
        return forum;
    }

    public Forum disableStudentReplies(@NonNull UUID loggedUserId, @NonNull UUID forumId){
        if(isStudent(new UserId(loggedUserId))) {
            throw new UserNotAuthorizedException();
        }

        Forum forum = this.forumRepository.findById(new ForumId(forumId))
                .orElseThrow(ForumNotFoundException::new);

        forum.updateCanStudentsReply(false);
        forumRepository.saveAndFlush(forum);
        return forum;
    }

    public Discussion openNewDiscussionOnForum(@NonNull UUID loggedUserId,@NonNull DiscussionForm discussionForm){
        Forum forum = this.forumRepository.findById(new ForumId(discussionForm.getForumId()))
                .orElseThrow(ForumNotFoundException::new);

        if(!forum.canStudentsReply() && isStudent(new UserId(loggedUserId))) {
            throw new UserNotAuthorizedException();
        }

        Title initialPostSubject = new Title(discussionForm.getInitialPost().getSubject());
        Content initialPostContent = new Content(discussionForm.getInitialPost().getContent());
        UserId initialPostAuthor = new UserId(discussionForm.getInitialPost().getAuthorId());
        Post initialPostOnDiscussion = forum.createInitialPostForDiscussion(initialPostSubject, initialPostContent,initialPostAuthor);
        this.postRepository.saveAndFlush(initialPostOnDiscussion);

        Username startedBy = new Username(discussionForm.getStartedByUsername());

        Discussion newDiscussion = forum.openDiscussion(startedBy, initialPostOnDiscussion);
        this.discussionRepository.saveAndFlush(newDiscussion);

        // auto subscribe the starter of the discussion (he's not subscribed because it's the first post of the discussion)
        Subscription newSubscription = this.createSubscriptionOnForumDiscussion(forum, newDiscussion.id().getId(), initialPostAuthor.getId());
        this.subscriptionRepository.saveAndFlush(newSubscription);

        // auto subscribe other students
        if(forum.autoSubscribe()){
            UUID roleId = this.roleService.findRoleIdByRoleName(RoleName.STUDENT);
            this.userService.findAllUserIdsByRoleId(roleId).forEach((userId) -> this.autoSubscribe(forum, newDiscussion.id(), userId));

            // if the auto subscribe feature is on, then notify the students
            NotifySubscribersMessage message = this.constructMessage(newDiscussion.id(), initialPostContent);
            this.kafkaTemplate.send("notifySubscribers", message);
            this.kafkaTemplate.flush();
        }
        return newDiscussion;
    }

    public Post replyOnForumDiscussion(@NonNull UUID loggedUserId, @NonNull PostReplyForm postReplyForm){
        Forum forum = this.forumRepository.findById(new ForumId(postReplyForm.getForumId()))
                .orElseThrow(ForumNotFoundException::new);

        if(!forum.canStudentsReply() && isStudent(new UserId(loggedUserId))){
            throw new UserNotAuthorizedException();
        }

        DiscussionId discussionId = new DiscussionId(postReplyForm.getDiscussionId());
        Content postContent = new Content(postReplyForm.getContent());
        Post parentPost = this.postRepository.findById(new PostId(postReplyForm.getParentPostId()))
                .orElseThrow(PostNotFoundException::new);
        UserId author = new UserId(postReplyForm.getAuthorId());

        // auto subscribe the author of the post to the discussion (if not already subscribed)
        this.autoSubscribe(forum, discussionId, author);

        Post newPostReply = forum.replyOnDiscussion(discussionId, postContent, parentPost, author);
        this.postRepository.saveAndFlush(newPostReply);

        // sending message with kafka
        NotifySubscribersMessage message = this.constructMessage(discussionId, postContent);
        this.kafkaTemplate.send("notifySubscribers", message);
        this.kafkaTemplate.flush();

        Username authorUsername = userService.findUsernameByUserId(postReplyForm.getAuthorId());
        this.discussionRepository.saveAndFlush(forum.updateDiscussionLastPost(discussionId, newPostReply, authorUsername));
        return newPostReply;
    }

    public Set<Discussion> allDiscussionsOnForum(@NonNull UUID forumId){
        Forum forum = this.forumRepository.findById(new ForumId(forumId))
                .orElseThrow(ForumNotFoundException::new);
        return forum.allDiscussions();
    }

    public Post getDiscussionOnForum(@NonNull UUID forumId,@NonNull UUID discussionId){
        Forum forum = this.forumRepository.findById(new ForumId(forumId))
                .orElseThrow(ForumNotFoundException::new);
        return forum.discussion(new DiscussionId(discussionId));
    }

    public Subscription subscribeOnForumDiscussion(@NonNull SubscriptionDTO subscriptionDTO){
        Forum forum = this.forumRepository.findById(new ForumId(subscriptionDTO.getForumId()))
                .orElseThrow(ForumNotFoundException::new);

        Subscription newSubscription = createSubscriptionOnForumDiscussion(forum, subscriptionDTO.getDiscussionId(), subscriptionDTO.getSubscriberId());
        return this.subscriptionRepository.saveAndFlush(newSubscription);
    }

    public boolean UnsubscribeFromForumDiscussion(@NonNull SubscriptionDTO subscriptionDTO){
        Forum forum = this.forumRepository.findById(new ForumId(subscriptionDTO.getForumId()))
                .orElseThrow(ForumNotFoundException::new);

        DiscussionId discussionId = new DiscussionId(subscriptionDTO.getDiscussionId());
        UserId subscriber = new UserId(subscriptionDTO.getSubscriberId());
        Subscription subscription = this.subscriptionRepository.findFirstByDiscussion_IdAndSubscriber(discussionId, subscriber)
                .orElseThrow(UserNotSubscribedException::new);

        boolean unsubscribed = forum.unsubscribeFromDiscussion(discussionId, subscription);
        if(!unsubscribed)
            throw new UserNotSubscribedException();
        this.subscriptionRepository.deleteById(subscription.id());
        return true;
    }

}
