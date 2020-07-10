package mk.ukim.finki.emt.forum.usermanagement.domain.repository;


import mk.ukim.finki.emt.forum.usermanagement.domain.model.User;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UserId> {

    User findUserByUsernameOrEmail(String usernameOrEmail);
}

