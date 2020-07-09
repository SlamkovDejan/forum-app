package mk.ukim.finki.emt.forum.sharedkernel.domain.user;

import lombok.Getter;
import mk.ukim.finki.emt.forum.sharedkernel.domain.base.ValueObject;
import mk.ukim.finki.emt.forum.sharedkernel.domain.exception.UsernameNotValidException;
import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Objects;


@Embeddable
@Getter
public class Username implements ValueObject {

    @Transient
    private static final String USERNAME_REGEX = "^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$";

    @Column(name = "username", nullable = false)
    private final String username;

    public Username(@NonNull String username) {
        if(!username.matches(USERNAME_REGEX)){
           throw new UsernameNotValidException("Username not valid: " + username);
        }
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
