package spring.repositories;

import org.springframework.data.repository.CrudRepository;
import spring.entities.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUserName(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUserNameAndPassword(String userName, String password);

    boolean existsByUserName(String username);

    boolean existsByEmail(String email);
}
