package mingu.spring.paseto.repository;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import mingu.spring.paseto.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT DISTINCT u FROM app_user u WHERE u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);

    @Query("SELECT u FROM app_user u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM app_user u WHERE u.username = :username AND u.enabled = true")
    Optional<User> findActiveByUsername(@Param("username") String username);

    @Query("SELECT u FROM app_user u WHERE u.email = :email AND u.enabled = true")
    Optional<User> findActiveByEmail(@Param("email") String email);

    // JPA
//    Optional<User> findDistinctByUsername(String username);
//    Optional<User> findByEmail(String email);
//    Optional<User> findByUsernameAndEnabledIsTrue(String username);
//    Optional<User> findByEmailAndEnabledIsTrue(String email);
}
