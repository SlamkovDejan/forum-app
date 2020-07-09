package mk.ukim.finki.emt.forum.forummanagement.domain.value;

import lombok.Getter;
import mk.ukim.finki.emt.forum.forummanagement.domain.exception.DescriptionTooLongException;
import mk.ukim.finki.emt.forum.sharedkernel.domain.base.ValueObject;
import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Objects;


@Getter
@Embeddable
public class Description implements ValueObject {

    @Transient
    private static final int MAX_WORD_COUNT = 250;

    @Column(name = "description", nullable = false)
    private final String description;

    public Description(@NonNull String description) {
        description = description.trim();
        int wordCount = description.split("\\s+").length;
        if(wordCount > MAX_WORD_COUNT) {
            throw new DescriptionTooLongException(String.format("Description too long: %s!", description));
        }
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Description that = (Description) o;
        return description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }
}
