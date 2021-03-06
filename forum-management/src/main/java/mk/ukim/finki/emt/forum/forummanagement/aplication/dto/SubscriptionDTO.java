package mk.ukim.finki.emt.forum.forummanagement.aplication.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class SubscriptionDTO implements Serializable {

    @NotNull
    private UUID forumId;

    @NotNull
    private UUID discussionId;

    @NotNull
    private UUID subscriberId;

}
