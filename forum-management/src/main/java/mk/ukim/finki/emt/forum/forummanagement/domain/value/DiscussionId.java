package mk.ukim.finki.emt.forum.forummanagement.domain.value;

import lombok.Getter;
import mk.ukim.finki.emt.forum.sharedkernel.domain.base.DomainObjectId;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class DiscussionId extends DomainObjectId {

    private DiscussionId() {
        super();
    }

}
