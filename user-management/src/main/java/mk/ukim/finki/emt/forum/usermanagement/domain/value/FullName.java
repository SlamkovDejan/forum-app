package mk.ukim.finki.emt.forum.usermanagement.domain.value;

import lombok.Getter;
import mk.ukim.finki.emt.forum.sharedkernel.domain.base.ValueObject;
import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Getter
@Embeddable
public class FullName implements ValueObject {

    @Column(name = "first_name", nullable = false)
    private final String firstName;

    @Column(name = "last_name", nullable = false)
    private final String lastName;

    public FullName(@NonNull String firstName, @NonNull String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FullName)) return false;
        FullName fullName = (FullName) o;
        return firstName.equals(fullName.firstName) &&
                lastName.equals(fullName.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

}
