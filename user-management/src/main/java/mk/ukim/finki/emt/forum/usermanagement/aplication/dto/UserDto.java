package mk.ukim.finki.emt.forum.usermanagement.aplication.dto;

import lombok.Getter;
import lombok.Setter;
import mk.ukim.finki.emt.forum.sharedkernel.domain.user.Username;
import mk.ukim.finki.emt.forum.usermanagement.domain.model.Role;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.Email;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.Password;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
public class UserDto implements Serializable {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String username;

    @NotNull
    private String email;

    @NotNull
    private String password;

    //@NotNull
    //private Role role;
}
