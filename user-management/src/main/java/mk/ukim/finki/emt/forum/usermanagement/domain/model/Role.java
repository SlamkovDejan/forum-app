package mk.ukim.finki.emt.forum.usermanagement.domain.model;

import lombok.Getter;
import mk.ukim.finki.emt.forum.sharedkernel.domain.base.AbstractEntity;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.RoleId;

import javax.persistence.*;
import java.util.Set;

@Getter
@Entity
@Table(name = "roles")
public class Role extends AbstractEntity<RoleId> {

    @Column(name = "role_name", nullable = false)
    private RoleName roleName;

    @OneToMany(
            targetEntity = User.class,
            mappedBy = "role",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY //TODO: discuss
    )
    private Set<User> users;

}
