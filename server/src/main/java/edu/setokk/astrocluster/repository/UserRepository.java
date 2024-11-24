package edu.setokk.astrocluster.repository;

import edu.setokk.astrocluster.model.UserJpo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserJpo, Long> {
    @Query(value = "SELECT u FROM UserJpo u WHERE u.username=:username")
    Optional<UserJpo> findUserByUsername(@Param("username") String username);

    @Query(value = "SELECT u FROM UserJpo u WHERE u.email=:email")
    Optional<UserJpo> findUserByEmail(@Param("email") String email);
}
