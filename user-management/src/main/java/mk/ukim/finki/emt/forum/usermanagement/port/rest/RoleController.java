package mk.ukim.finki.emt.forum.usermanagement.port.rest;

import mk.ukim.finki.emt.forum.usermanagement.domain.model.Role;
import mk.ukim.finki.emt.forum.usermanagement.domain.model.RoleName;
import mk.ukim.finki.emt.forum.usermanagement.domain.repository.RoleRepository;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.RoleId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping("/{roleName}/id")
    public UUID findRoleIdByRoleName(@PathVariable("roleName") RoleName roleName){
        Role role = roleRepository.findRoleByRoleName(roleName);
        return role.id().getId();
    }

    @GetMapping
    public List<Role> findAll(){
        return roleRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> findById(@PathVariable("id") UUID roleId) {
        return roleRepository.findById(new RoleId(roleId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
