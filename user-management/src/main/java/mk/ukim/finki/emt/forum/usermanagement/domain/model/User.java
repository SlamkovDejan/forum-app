package mk.ukim.finki.emt.forum.usermanagement.domain.model;

import lombok.Getter;
import mk.ukim.finki.emt.forum.sharedkernel.domain.base.AbstractEntity;
import mk.ukim.finki.emt.forum.sharedkernel.domain.user.Username;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.Email;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.FullName;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.Password;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.UserId;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "users")
public class User extends AbstractEntity<UserId> {

    @Embedded
    private FullName fullName;

    @Embedded
    private Username username;

    @Embedded
    private Email email;

    @Embedded
    private Password password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

}
