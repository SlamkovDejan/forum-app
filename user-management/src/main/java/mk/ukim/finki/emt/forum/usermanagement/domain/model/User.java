package mk.ukim.finki.emt.forum.usermanagement.domain.model;

import lombok.Getter;
import mk.ukim.finki.emt.forum.sharedkernel.domain.base.AbstractEntity;
import mk.ukim.finki.emt.forum.sharedkernel.domain.user.Username;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.Email;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.Password;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.UserId;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "users")
public class User extends AbstractEntity<UserId> {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Embedded
    private Username username;

    @Embedded
    private Email email;

    @Embedded
    private Password password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    public User() {
    }

    public User(@NonNull String firstName,@NonNull String lastName,@NonNull Username username,
                @NonNull Email email,@NonNull Password password,@NonNull Role role) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public boolean login(@NonNull Password password){
        return this.password.equals(password);
    }

    public User changeFirstName(@NonNull String firstName){
        this.firstName = firstName;
        return this;
    }

    public User changeLastName(@NonNull String firstName){
        this.lastName = firstName;
        return this;
    }

    public User changeEmail(@NonNull Email email){
        this.email = email;
        return this;
    }

    public static User signUp(@NonNull String firstName,@NonNull String lastName,@NonNull Username username,
                              @NonNull Email email,@NonNull Password password,@NonNull Role role) {

        return new User(firstName, lastName, username, email, password, role);
    }

}
