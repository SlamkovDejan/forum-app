package mk.ukim.finki.emt.forum.usermanagement.aplication;

import mk.ukim.finki.emt.forum.sharedkernel.domain.user.Username;
import mk.ukim.finki.emt.forum.usermanagement.aplication.dto.LoginDto;
import mk.ukim.finki.emt.forum.usermanagement.aplication.dto.UserDto;
import mk.ukim.finki.emt.forum.usermanagement.domain.model.Role;
import mk.ukim.finki.emt.forum.usermanagement.domain.model.User;
import mk.ukim.finki.emt.forum.usermanagement.domain.repository.UserRepository;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.Email;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.Password;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.UserId;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User login(@NonNull LoginDto loginDto){
        User user = userRepository.findUserByUsernameOrEmail(loginDto.getUsernameOrEmail());
        if(user == null){
            throw new RuntimeException();
        }

        Password password = new Password(passwordEncoder.encode(loginDto.getPassword()));
        if(user.login(password)){
            return user;
        }
        throw new RuntimeException();
        //TODO: discuss later
    }

    public User register(@NonNull UserDto userDto){
        Username username = new Username(userDto.getUsername());
        Email email = new Email(userDto.getEmail());
        if(!Password.validatePassword(userDto.getPassword())){
            throw new RuntimeException();
        }
        Password password = new Password(passwordEncoder.encode(userDto.getPassword()));
        User user = User.signUp(userDto.getFirstName(), userDto.getLastName(),
                                username, email, password, new Role()); //TODO: discuss later

        userRepository.save(user);
        return user;
    }

    public User changeFirstName(@NonNull UserId userId, @NonNull String name){
        User user = userRepository.findById(userId).orElseThrow(RuntimeException::new);
        user.changeFirstName(name); // it can be void?
        userRepository.save(user);
        return user;
    }

    public User changeLastName(@NonNull UserId userId, @NonNull String name){
        User user = userRepository.findById(userId).orElseThrow(RuntimeException::new);
        user.changeLastName(name); // change return type to void?
        userRepository.save(user);
        return user;
    }

    public User changeEmail(@NonNull UserId userId, @NonNull String email){
        User user = userRepository.findById(userId).orElseThrow(RuntimeException::new);
        Email newEmail = new Email(email);
        user.changeEmail(newEmail);
        userRepository.save(user); // change return type to void?
        return user;
    }

}
