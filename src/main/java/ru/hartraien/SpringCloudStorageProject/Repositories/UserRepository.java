package ru.hartraien.SpringCloudStorageProject.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>
{
    UserEntity findUserByUsername( String username );
}
