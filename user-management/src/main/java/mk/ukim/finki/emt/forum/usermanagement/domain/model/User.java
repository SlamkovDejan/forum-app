package mk.ukim.finki.emt.forum.usermanagement.domain.model;

import lombok.Getter;
import mk.ukim.finki.emt.forum.sharedkernel.domain.base.AbstractEntity;
import mk.ukim.finki.emt.forum.sharedkernel.domain.user.Username;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.Email;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.EncodedPassword;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.FullName;
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
    private EncodedPassword encodedPassword;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    public User() {
    }

    private User(@NonNull FullName fullName, @NonNull Username username,
                 @NonNull Email email, @NonNull EncodedPassword encodedPassword, @NonNull Role role) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.encodedPassword = encodedPassword;
        this.role = role;
    }

    public boolean login(@NonNull EncodedPassword encodedPassword){
        return this.encodedPassword.equals(encodedPassword);
    }

    public void changeFirstName(@NonNull String firstName){
        this.fullName = new FullName(firstName, this.fullName.getLastName());
    }

    public void changeLastName(@NonNull String lastName){
        this.fullName = new FullName(this.fullName.getFirstName(), lastName);
    }

    public void changeEmail(@NonNull Email email){
        this.email = email;
    }

    public void changePassword(EncodedPassword newPassword, EncodedPassword oldPassword) {
        if(!this.encodedPassword.equals(oldPassword)){
            throw new RuntimeException();
        }
        if(this.encodedPassword.equals(newPassword)){
            throw new RuntimeException();
        }
        this.encodedPassword = newPassword;
    }

    public static User signUp(@NonNull FullName fullName,@NonNull Username username,
                              @NonNull Email email,@NonNull EncodedPassword password,@NonNull Role role) {

        return new User(fullName, username, email, password, role);
    }

}
