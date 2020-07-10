package mk.ukim.finki.emt.forum.usermanagement.domain.repository;

import mk.ukim.finki.emt.forum.usermanagement.domain.model.Role;
import mk.ukim.finki.emt.forum.usermanagement.domain.model.RoleName;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.RoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, RoleId> {

    Role findRoleByRoleName(RoleName roleName);
}
