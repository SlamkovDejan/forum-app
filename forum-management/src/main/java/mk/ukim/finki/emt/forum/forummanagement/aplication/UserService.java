package mk.ukim.finki.emt.forum.forummanagement.aplication;

import mk.ukim.finki.emt.forum.forummanagement.domain.value.UserId;
import mk.ukim.finki.emt.forum.sharedkernel.domain.user.Username;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public interface UserService {

    Stream<UserId> findAllUserIdsByRoleId(UUID roleId);

    Username findUsernameByUserId(UUID userId);

}
