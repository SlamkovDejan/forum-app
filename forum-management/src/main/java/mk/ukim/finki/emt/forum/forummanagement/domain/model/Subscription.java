package mk.ukim.finki.emt.forum.forummanagement.domain.model;

import lombok.Getter;
import mk.ukim.finki.emt.forum.forummanagement.domain.value.SubscriptionId;
import mk.ukim.finki.emt.forum.forummanagement.domain.value.UserId;
import mk.ukim.finki.emt.forum.sharedkernel.domain.base.AbstractEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "subscriptions")
public class Subscription extends AbstractEntity<SubscriptionId> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discussion_id", nullable = false)
    private Discussion discussion;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "subscriber", nullable = false))
    private UserId subscriber;

    @Column(name = "timestamp_subscribed", nullable = false)
    private LocalDateTime timestampSubscribed;

    public Subscription() {
    }

    public Subscription(Discussion discussion, UserId subscriber){
        this.discussion = discussion;
        this.subscriber = subscriber;
        this.timestampSubscribed = LocalDateTime.now();
    }

}
