package mk.ukim.finki.emt.forum.forummanagement.domain.value;

import lombok.Getter;
import mk.ukim.finki.emt.forum.sharedkernel.domain.base.DomainObjectId;

import javax.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public class DiscussionId extends DomainObjectId {

    public DiscussionId() {
        super();
    }

    public DiscussionId(UUID id) {
        super(id);
    }

}
