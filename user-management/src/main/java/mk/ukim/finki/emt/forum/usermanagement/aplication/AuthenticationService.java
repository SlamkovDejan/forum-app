package mk.ukim.finki.emt.forum.usermanagement.aplication;

import mk.ukim.finki.emt.forum.sharedkernel.domain.user.Username;
import mk.ukim.finki.emt.forum.usermanagement.aplication.form.LoginForm;
import mk.ukim.finki.emt.forum.usermanagement.aplication.form.UserForm;
import mk.ukim.finki.emt.forum.usermanagement.domain.exception.PasswordException;
import mk.ukim.finki.emt.forum.usermanagement.domain.exception.RoleNotFoundException;
import mk.ukim.finki.emt.forum.usermanagement.domain.exception.UserNotFoundException;
import mk.ukim.finki.emt.forum.usermanagement.domain.model.Role;
import mk.ukim.finki.emt.forum.usermanagement.domain.model.User;
import mk.ukim.finki.emt.forum.usermanagement.domain.repository.RoleRepository;
import mk.ukim.finki.emt.forum.usermanagement.domain.repository.UserRepository;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.Email;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.EncodedPassword;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.FullName;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.UserId;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.*;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User login(@NonNull LoginForm loginForm){
        User user = userRepository.findUserByUsername(new Username(loginForm.getUsernameOrEmail()))
                .orElse(userRepository.findUserByEmail(new Email(loginForm.getUsernameOrEmail())).orElseThrow(UserNotFoundException::new));


        EncodedPassword password = new EncodedPassword(passwordEncoder.encode(loginForm.getPassword()));
        if(!user.login(password)) {
            throw new PasswordException("Wrong password");
        }

        return user;
    }

    public User register(@NonNull UserForm userForm){
        FullName fullName = new FullName(userForm.getFirstName(), userForm.getLastName());
        Username username = new Username(userForm.getUsername());
        Email email = new Email(userForm.getEmail());
        if(!EncodedPassword.validatePassword(userForm.getPassword())){
            throw new PasswordException("Invalid password");
        }
        EncodedPassword password = new EncodedPassword(passwordEncoder.encode(userForm.getPassword()));
        Role role = roleRepository.findById(new RoleId(userForm.getRoleId())).orElseThrow(RoleNotFoundException::new);
        User user = User.signUp(fullName, username, email, password, role);

        userRepository.save(user);
        return user;
    }

    public User changeFirstName(@NonNull UserId userId, @NonNull String name){
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.changeFirstName(name);
        userRepository.save(user);
        return user;
    }

    public User changeLastName(@NonNull UserId userId, @NonNull String name){
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.changeLastName(name);
        userRepository.save(user);
        return user;
    }

    public User changeEmail(@NonNull UserId userId, @NonNull String email){
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Email newEmail = new Email(email);
        user.changeEmail(newEmail);
        userRepository.save(user);
        return user;
    }

    public User changePassword(@NonNull UserId userId, @NonNull String newPassword, @NonNull String oldPassword){
        if(!EncodedPassword.validatePassword(newPassword) || !EncodedPassword.validatePassword(oldPassword)){
            throw new PasswordException("Invalid password");
        }
        EncodedPassword newPw = new EncodedPassword(passwordEncoder.encode(newPassword));
        EncodedPassword oldPw = new EncodedPassword(passwordEncoder.encode(oldPassword));
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.changePassword(newPw, oldPw);
        userRepository.save(user);
        return user;
    }

}
