package mk.ukim.finki.emt.forum.sharedkernel.domain.user;

import lombok.Getter;
import mk.ukim.finki.emt.forum.sharedkernel.domain.base.ValueObject;
import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;


@Embeddable
@Getter
public class Username implements ValueObject {

    @Column(name = "username", nullable = false)
    private final String username;

    public Username(@NonNull String username) {
        //TODO: Implement validity check
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Username username1 = (Username) o;
        return username.equals(username1.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
