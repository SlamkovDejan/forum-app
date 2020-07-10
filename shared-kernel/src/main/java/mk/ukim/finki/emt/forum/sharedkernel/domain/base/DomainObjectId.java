package mk.ukim.finki.emt.forum.sharedkernel.domain.base;

import lombok.Getter;

import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import java.util.Objects;
import java.util.UUID;

@Getter
@MappedSuperclass
public abstract class DomainObjectId implements ValueObject {

    private final UUID id;

    public DomainObjectId() {
        this.id = UUID.randomUUID();
    }

    public DomainObjectId(UUID id){
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainObjectId)) return false;
        DomainObjectId that = (DomainObjectId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
