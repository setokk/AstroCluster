package edu.setokk.astrocluster.repository;

import edu.setokk.astrocluster.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query(value = "SELECT u FROM UserEntity u WHERE u.username=:username")
    Optional<UserEntity> findUserByUsername(@Param("username") String username);

    @Query(value = "SELECT u FROM UserEntity u WHERE u.email=:email")
    Optional<UserEntity> findUserByEmail(@Param("email") String email);
}
