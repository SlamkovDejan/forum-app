package mk.ukim.finki.emt.forum.forummanagement.aplication;

import java.util.UUID;

public interface RoleService {

    UUID findRoleIdByRoleName(String roleName);

}
