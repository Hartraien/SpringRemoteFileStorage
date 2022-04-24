package ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;

import java.util.List;

/**
 * service that encapsulates working with UserEntityRepository
 */
public interface UserService
{
    /**
     * Saves given user to db if no user with given useranme exists
     *
     * @param user - user to save
     * @throws UserServiceException if could not save user
     */
    void save( UserEntity user ) throws UserServiceException;

    /**
     * Seeks user by username in repository
     *
     * @param username - username of a user
     * @return user by username from db or null otherwise
     */
    UserEntity findByUsername( String username );

    /**
     * Returns all users from db
     *
     * @return users in db
     */
    List<UserEntity> getAllUsers();

    /**
     * Returns users from db according to request
     *
     * @param request - specifies number of users per page and number of page
     * @return page containing user for given @request
     */
    Page<UserEntity> getAllUsersPaging( Pageable request );

    /**
     * Saves multiple user using saveAll method of repository
     *
     * @param entities - list of users to save
     */
    void saveAll( List<UserEntity> entities );
}
