package ro.galati.cityissues.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.galati.cityissues.model.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}