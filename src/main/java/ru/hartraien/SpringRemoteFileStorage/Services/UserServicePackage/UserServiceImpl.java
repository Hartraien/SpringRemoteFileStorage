package ru.hartraien.SpringRemoteFileStorage.Services.UserServicePackage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hartraien.SpringRemoteFileStorage.Entities.DirectoryEntity;
import ru.hartraien.SpringRemoteFileStorage.Entities.Role;
import ru.hartraien.SpringRemoteFileStorage.Entities.UserEntity;
import ru.hartraien.SpringRemoteFileStorage.Repositories.UserRepository;
import ru.hartraien.SpringRemoteFileStorage.Services.DirServicePackage.DirService;
import ru.hartraien.SpringRemoteFileStorage.Services.StorageServicePackage.StorageException;
import ru.hartraien.SpringRemoteFileStorage.Services.StorageServicePackage.StorageService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * {@inheritDoc}
 */
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;
    private final DirService dirService;

    private final StorageService storageService;
    private final PasswordEncoder passwordEncoder;

    private final Logger logger;

    @Autowired
    public UserServiceImpl( UserRepository userRepository, DirService dirService, StorageService storageService, PasswordEncoder passwordEncoder )
    {
        this.userRepository = userRepository;
        this.dirService = dirService;
        this.storageService = storageService;
        this.passwordEncoder = passwordEncoder;
        logger = LoggerFactory.getLogger( UserServiceImpl.class );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void save( UserEntity user ) throws UserServiceException
    {
        if ( userNotInDB( user ) )
        {
            processUser( user );
            userRepository.save( user );
        }
        else
        {
            String message = "User " + user.getUsername() + " already exists";
            logger.error( message );
            throw new UserServiceException( message );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity findByUsername( String username )
    {
        return userRepository.findUserByUsername( username );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserEntity> getAllUsers()
    {
        return userRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<UserEntity> getAllUsersPaging( Pageable request )
    {
        return userRepository.findAll( request );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void saveAll( List<UserEntity> entities )
    {
        List<UserEntity> users = entities.stream().filter( this::userNotInDB )
                .distinct()
                .map( this::processUserAndReturnOrNull )
                .filter( Objects::nonNull )
                .collect( Collectors.toList() );
        userRepository.saveAll( users );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void updateResetPasswordToken( String token, String email ) throws UserServiceException
    {
        UserEntity user = userRepository.findOneByEmail( email );
        if ( user != null )
        {
            user.setResetPasswordToken( token );
            update( user );
        }
        else
            throw new UserServiceException( "Could not find user with email " + email );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity findUserByResetPasswordToken( String token )
    {
        return userRepository.findUserByResetPasswordToken( token );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void updatePassword( UserEntity user, String newPassword )
    {
        user.setPassword( passwordEncoder.encode( newPassword ) );
        user.setResetPasswordToken( null );
        update( user );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void update( UserEntity user )
    {
        userRepository.save( user );
    }

    private void processUser( UserEntity user ) throws UserServiceException
    {
        user.setPassword( passwordEncoder.encode( user.getPassword() ) );
        Role role_user = Role.Role_User;
        try
        {
            final DirectoryEntity directory = dirService.generateNewDir();
            user.setDir( directory );
            storageService.createDir( directory.getDirname() );

        }
        catch ( StorageException e )
        {
            logger.error( "Could not create directory for user: " + user.getUsername() );
            throw new UserServiceException( "Could not create directory for user", e );
        }
        user.addRole( role_user );
    }

    private UserEntity processUserAndReturnOrNull( UserEntity user )
    {
        try
        {
            processUser( user );
        }
        catch ( UserServiceException e )
        {
            return null;
        }
        return user;
    }

    private boolean userNotInDB( UserEntity user )
    {
        return findByUsername( user.getUsername() ) == null;
    }
}
