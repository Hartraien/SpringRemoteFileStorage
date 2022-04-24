package ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hartraien.SpringCloudStorageProject.Entities.Role;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Repositories.UserRepository;
import ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage.DirService;
import ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage.DirectoryException;
import ru.hartraien.SpringCloudStorageProject.Services.RoleServicePackage.RoleService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final DirService dirService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl( UserRepository userRepository, RoleService roleService, DirService dirService, PasswordEncoder passwordEncoder )
    {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.dirService = dirService;
        this.passwordEncoder = passwordEncoder;
    }


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
            throw new UserServiceException( "User " + user.getUsername() + " already exists" );
    }

    @Override
    public UserEntity findByUsername( String username )
    {
        return userRepository.findUserByUsername( username );
    }

    @Override
    public List<UserEntity> getAllUsers()
    {
        return userRepository.findAll();
    }

    @Override
    public Page<UserEntity> getAllUsersPaging( Pageable request )
    {
        return userRepository.findAll( request );
    }

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
            user.setDir( dirService.generateNewDir() );
        }
        catch ( DirectoryException e )
        {
            //TODO add logger and UserService Exception
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
