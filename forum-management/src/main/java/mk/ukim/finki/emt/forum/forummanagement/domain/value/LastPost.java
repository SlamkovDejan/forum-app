package mk.ukim.finki.emt.forum.forummanagement.domain.value;

import lombok.Getter;
import mk.ukim.finki.emt.forum.sharedkernel.domain.base.ValueObject;
import mk.ukim.finki.emt.forum.sharedkernel.domain.user.Username;
import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
@Getter
public class LastPost implements ValueObject {

    @Embedded
    private final Username username;

    @Column(name="timestamp_created", nullable = false)
    private final LocalDateTime timestampCreated;

    public LastPost(@NonNull Username username, @NonNull LocalDateTime timestampCreated) {
        this.username = username;
        this.timestampCreated = timestampCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LastPost lastPost = (LastPost) o;
        return username.equals(lastPost.username) &&
                timestampCreated.equals(lastPost.timestampCreated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, timestampCreated);
    }
}
