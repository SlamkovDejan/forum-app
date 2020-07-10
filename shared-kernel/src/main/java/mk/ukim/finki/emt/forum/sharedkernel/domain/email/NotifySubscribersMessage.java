package mk.ukim.finki.emt.forum.sharedkernel.domain.email;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
public class NotifySubscribersMessage implements Serializable {

    @NotNull
    private List<UUID> userIds;

    @NotNull
    private String subject;

    @NotNull
    private String text;

}
