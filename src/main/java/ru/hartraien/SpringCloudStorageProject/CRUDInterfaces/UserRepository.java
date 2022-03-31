package ru.hartraien.SpringCloudStorageProject.CRUDInterfaces;

import org.springframework.data.repository.CrudRepository;
import ru.hartraien.SpringCloudStorageProject.POJO.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long>
{
    UserEntity findUserByUsername( String name);
}
