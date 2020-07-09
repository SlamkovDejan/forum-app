package mk.ukim.finki.emt.forum.forummanagement.domain.value;

import mk.ukim.finki.emt.forum.sharedkernel.domain.base.DomainObjectId;

import javax.persistence.Embeddable;

@Embeddable
public class SubscriptionId extends DomainObjectId {

    public SubscriptionId() {
        super();
    }

}
