package ru.hartraien.SpringCloudStorageProject.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>
{
    UserEntity findUserByUsername( String username );
}
