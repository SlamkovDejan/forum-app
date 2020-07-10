package mk.ukim.finki.emt.forum.forummanagement.domain.repository;

import mk.ukim.finki.emt.forum.forummanagement.domain.model.Discussion;
import mk.ukim.finki.emt.forum.forummanagement.domain.model.Subscription;
import mk.ukim.finki.emt.forum.forummanagement.domain.value.DiscussionId;
import mk.ukim.finki.emt.forum.forummanagement.domain.value.SubscriptionId;
import mk.ukim.finki.emt.forum.forummanagement.domain.value.UserId;
import mk.ukim.finki.emt.forum.sharedkernel.domain.base.DomainObjectId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionId> {

    Optional<Subscription> findFirstByDiscussion_IdAndSubscriber(DomainObjectId discussion_id, UserId subscriber);

}
