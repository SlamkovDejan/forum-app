package mk.ukim.finki.emt.forum.usermanagement.aplication;

import mk.ukim.finki.emt.forum.sharedkernel.domain.user.Username;
import mk.ukim.finki.emt.forum.usermanagement.aplication.dto.LoginDto;
import mk.ukim.finki.emt.forum.usermanagement.aplication.dto.UserDto;
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

import java.util.UUID;

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

    public User login(@NonNull LoginDto loginDto){
        User user = userRepository.findUserByUsername(new Username(loginDto.getUsernameOrEmail()))
                .orElse(userRepository.findUserByEmail(new Email(loginDto.getUsernameOrEmail())).orElseThrow(RuntimeException::new));


        EncodedPassword password = new EncodedPassword(passwordEncoder.encode(loginDto.getPassword()));
        if(!user.login(password)) {
            throw new RuntimeException();
        }

        return user;
    }

    public User register(@NonNull UserDto userDto){
        FullName fullName = new FullName(userDto.getFirstName(), userDto.getLastName());
        Username username = new Username(userDto.getUsername());
        Email email = new Email(userDto.getEmail());
        if(!EncodedPassword.validatePassword(userDto.getPassword())){
            throw new RuntimeException();
        }
        EncodedPassword password = new EncodedPassword(passwordEncoder.encode(userDto.getPassword()));
        Role role = roleRepository.findById(new RoleId(userDto.getRoleId())).orElseThrow(RuntimeException::new);
        User user = User.signUp(fullName, username, email, password, role);

        userRepository.save(user);
        return user;
    }

    public User changeFirstName(@NonNull UserId userId, @NonNull String name){
        User user = userRepository.findById(userId).orElseThrow(RuntimeException::new);
        user.changeFirstName(name);
        userRepository.save(user);
        return user;
    }

    public User changeLastName(@NonNull UserId userId, @NonNull String name){
        User user = userRepository.findById(userId).orElseThrow(RuntimeException::new);
        user.changeLastName(name);
        userRepository.save(user);
        return user;
    }

    public User changeEmail(@NonNull UserId userId, @NonNull String email){
        User user = userRepository.findById(userId).orElseThrow(RuntimeException::new);
        Email newEmail = new Email(email);
        user.changeEmail(newEmail);
        userRepository.save(user);
        return user;
    }

    public User changePassword(@NonNull UserId userId, @NonNull String newPassword, @NonNull String oldPassword){
        if(!EncodedPassword.validatePassword(newPassword) || !EncodedPassword.validatePassword(oldPassword)){
            throw new RuntimeException();
        }
        EncodedPassword newPw = new EncodedPassword(passwordEncoder.encode(newPassword));
        EncodedPassword oldPw = new EncodedPassword(passwordEncoder.encode(oldPassword));
        User user = userRepository.findById(userId).orElseThrow(RuntimeException::new);
        user.changePassword(newPw, oldPw);
        userRepository.save(user);
        return user;
    }

}
