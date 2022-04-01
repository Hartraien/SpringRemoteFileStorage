package ru.hartraien.SpringCloudStorageProject.Repositories;

import org.springframework.data.repository.CrudRepository;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long>
{
    UserEntity findUserByUsername( String username );
}
