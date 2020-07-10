package mk.ukim.finki.emt.forum.usermanagement.port.rest;

import mk.ukim.finki.emt.forum.sharedkernel.domain.user.Username;
import mk.ukim.finki.emt.forum.usermanagement.domain.model.User;
import mk.ukim.finki.emt.forum.usermanagement.domain.repository.UserRepository;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.RoleId;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.UserId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/role/{roleId}/ids")
    public List<UUID> findUserIdsByRole(@PathVariable("roleId") UUID roleId){
        List<User> usersWithRole = userRepository.findAllByRole_Id(new RoleId(roleId));
        return usersWithRole.stream()
                .map(user -> user.id().getId())
                .collect(Collectors.toList());
    }

    @GetMapping
    public List<User> findAll(){
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable("id") UUID userId) {
        return userRepository.findById(new UserId(userId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/username")
    public ResponseEntity<Username> findUsernameByUserId(@PathVariable("id") UUID userId) {
        return userRepository.findUsernameById(new UserId(userId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
