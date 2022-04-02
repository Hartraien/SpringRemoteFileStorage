package ru.hartraien.SpringCloudStorageProject.Services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;

import java.util.List;

public interface UserService
{
    void save( UserEntity user );

    UserEntity findByUsername( String username );

    List<UserEntity> getAllUsers();

    void saveAdmin( UserEntity admin );

    Page<UserEntity> getAllUsersPaging( Pageable request );

    void saveAll( List<UserEntity> entities );
}
