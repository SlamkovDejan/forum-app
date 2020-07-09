package mk.ukim.finki.emt.forum.usermanagement.domain.value;

import lombok.Getter;
import mk.ukim.finki.emt.forum.sharedkernel.domain.base.DomainObjectId;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class UserId extends DomainObjectId {

    public UserId() {
        super();
    }

}
