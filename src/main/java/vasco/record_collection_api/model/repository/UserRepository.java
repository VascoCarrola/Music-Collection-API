package vasco.record_collection_api.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vasco.record_collection_api.model.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
