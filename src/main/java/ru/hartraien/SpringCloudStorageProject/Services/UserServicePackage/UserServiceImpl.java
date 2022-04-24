package ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hartraien.SpringCloudStorageProject.Entities.DirectoryEntity;
import ru.hartraien.SpringCloudStorageProject.Entities.Role;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Repositories.UserRepository;
import ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage.DirService;
import ru.hartraien.SpringCloudStorageProject.Services.RoleServicePackage.RoleService;
import ru.hartraien.SpringCloudStorageProject.Services.StorageServicePackage.StorageException;
import ru.hartraien.SpringCloudStorageProject.Services.StorageServicePackage.StorageService;

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
    private final RoleService roleService;
    private final DirService dirService;

    private final StorageService storageService;
    private final PasswordEncoder passwordEncoder;

    private final Logger logger;

    @Autowired
    public UserServiceImpl( UserRepository userRepository, RoleService roleService, DirService dirService, StorageService storageService, PasswordEncoder passwordEncoder )
    {
        this.userRepository = userRepository;
        this.roleService = roleService;
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

    private void processUser( UserEntity user ) throws UserServiceException
    {
        user.setPassword( passwordEncoder.encode( user.getPassword() ) );
        Role role_user = roleService.findRoleByName( "Role_User" );
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
        if ( role_user != null )
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
