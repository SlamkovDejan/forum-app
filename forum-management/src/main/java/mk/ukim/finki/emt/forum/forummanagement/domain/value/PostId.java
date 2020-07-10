package mk.ukim.finki.emt.forum.forummanagement.domain.value;

import mk.ukim.finki.emt.forum.sharedkernel.domain.base.DomainObjectId;

import javax.persistence.Embeddable;

@Embeddable
public class PostId extends DomainObjectId {

    public PostId() {
        super();
    }

}
