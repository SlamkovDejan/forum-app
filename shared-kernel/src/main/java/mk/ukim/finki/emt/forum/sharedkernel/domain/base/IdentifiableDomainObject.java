package mk.ukim.finki.emt.forum.sharedkernel.domain.base;

import org.springframework.lang.Nullable;

import java.io.Serializable;

public interface IdentifiableDomainObject<ID extends Serializable> extends DomainObject {

    ID id();

}
