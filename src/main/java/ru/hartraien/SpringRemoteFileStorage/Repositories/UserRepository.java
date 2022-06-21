package ru.hartraien.SpringRemoteFileStorage.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.hartraien.SpringRemoteFileStorage.Entities.UserEntity;

@Transactional(propagation = Propagation.MANDATORY)
public interface UserRepository extends JpaRepository<UserEntity, Long>
{
    UserEntity findUserByUsername( String username );

    @Query("SELECT u FROM UserEntity u WHERE u.email = ?1")
    UserEntity findOneByEmail( String email );

    UserEntity findUserByResetPasswordToken( String resetPasswordToken );
}
