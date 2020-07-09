package mk.ukim.finki.emt.forum.forummanagement.domain.value;

import mk.ukim.finki.emt.forum.sharedkernel.domain.base.DomainObjectId;

import javax.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public class UserId extends DomainObjectId {

    public UserId() {
        super();
    }

    public UserId(UUID id){
        super(id);
    }

}
