package mk.ukim.finki.emt.forum.usermanagement.domain.value;

import lombok.Getter;
import mk.ukim.finki.emt.forum.sharedkernel.domain.base.ValueObject;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Objects;

@Getter
@Embeddable
public class Password implements ValueObject {

    @Transient
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[*.!@$%^&(){}[]:;<>,.?/~_+-=|\\]).{8,32}$";

    @Column(name = "password", nullable = false)
    private final String password;

    public Password(@NonNull String password, @NonNull PasswordEncoder passwordEncoder) {
        if(!password.matches(PASSWORD_REGEX)){
            throw new RuntimeException();
        }

        this.password = passwordEncoder.encode(password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password1 = (Password) o;
        return password.equals(password1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }
}
