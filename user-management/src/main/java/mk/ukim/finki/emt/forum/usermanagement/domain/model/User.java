package mk.ukim.finki.emt.forum.usermanagement.domain.model;

import lombok.Getter;
import mk.ukim.finki.emt.forum.sharedkernel.domain.base.AbstractEntity;
import mk.ukim.finki.emt.forum.sharedkernel.domain.user.Username;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.Email;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.FullName;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.Password;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.UserId;
import org.springframework.lang.NonNull;

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

    public User() {
    }

    public User(@NonNull FullName fullName,@NonNull Username username,
                @NonNull Email email,@NonNull Password password,@NonNull Role role) {

        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public boolean login(@NonNull Password password){
        return this.password.equals(password);
    }

    public User changeFirstName(@NonNull String firstName){
        this.fullName = new FullName(firstName, this.fullName.getLastName());
        return this;
    }

    public User changeLastName(@NonNull String lastName){
        this.fullName = new FullName(this.fullName.getFirstName(), lastName);
        return this;
    }

    public User changeEmail(@NonNull Email email){
        this.email = email;
        return this;
    }

    public static User signUp(@NonNull FullName fullName,@NonNull Username username,
                              @NonNull Email email,@NonNull Password password,@NonNull Role role) {

        return new User(fullName, username, email, password, role);
    }

}
