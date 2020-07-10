package mk.ukim.finki.emt.forum.usermanagement.aplication.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

import java.io.Serializable;

@Data
public class LoginForm implements Serializable {

    @NotNull
    private String usernameOrEmail;

    @NotNull
    private String password;

}
