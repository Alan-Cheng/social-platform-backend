package backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import backend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUserPhone(@Param("userPhone") String userPhone);

    Optional<User> findById(Long userId);
}
