package mk.ukim.finki.emt.forum.forummanagement.aplication.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class SubscribeDTO {

    private UUID forumId;

    private UUID discussionId;

    private UUID subscriberId;

}
