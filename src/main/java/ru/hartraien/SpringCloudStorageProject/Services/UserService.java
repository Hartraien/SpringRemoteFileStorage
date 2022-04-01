package ru.hartraien.SpringCloudStorageProject.Services;

import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;

import java.util.List;

public interface UserService
{
    void save( UserEntity user );

    UserEntity findByUsername( String username );

    List<UserEntity> getAllUsers();

    void saveAdmin( UserEntity admin );
}
