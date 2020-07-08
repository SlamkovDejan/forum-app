package mk.ukim.finki.emt.forum.forummanagement.domain.value;

import lombok.Getter;
import mk.ukim.finki.emt.forum.sharedkernel.domain.base.ValueObject;
import mk.ukim.finki.emt.forum.sharedkernel.domain.user.Username;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
@Getter
public class LastPostInfo implements ValueObject {

    @Embedded
    @AttributeOverride(name = "username", column = @Column(name = "last_post_info_username", nullable = false))
    private final Username username;

    @Column(name = "last_post_info_timestamp", nullable = false)
    private final LocalDateTime timestampCreated;

    public LastPostInfo(@NonNull Username username, @NonNull LocalDateTime timestampCreated) {
        this.username = username;
        this.timestampCreated = timestampCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LastPostInfo lastPostInfo = (LastPostInfo) o;
        return username.equals(lastPostInfo.username) &&
                timestampCreated.equals(lastPostInfo.timestampCreated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, timestampCreated);
    }

}
