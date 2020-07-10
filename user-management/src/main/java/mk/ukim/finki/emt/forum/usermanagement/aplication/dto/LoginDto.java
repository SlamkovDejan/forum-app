package mk.ukim.finki.emt.forum.usermanagement.aplication.dto;

import javax.validation.constraints.NotNull;

import java.io.Serializable;

public class LoginDto implements Serializable {

    @NotNull
    private String usernameOrEmail;

    @NotNull
    private String password;

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
