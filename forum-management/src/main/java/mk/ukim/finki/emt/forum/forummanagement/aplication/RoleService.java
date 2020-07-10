package mk.ukim.finki.emt.forum.forummanagement.aplication;

import mk.ukim.finki.emt.forum.sharedkernel.domain.role.RoleName;

import java.util.UUID;

public interface RoleService {

    UUID findRoleIdByRoleName(RoleName roleName);

    RoleName findRoleNameByRoleId(UUID roleId);
}
