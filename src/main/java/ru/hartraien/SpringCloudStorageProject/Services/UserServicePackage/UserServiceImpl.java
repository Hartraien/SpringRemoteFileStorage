package ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.hartraien.SpringCloudStorageProject.Entities.Role;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Repositories.RoleRepository;
import ru.hartraien.SpringCloudStorageProject.Repositories.UserRepository;
import ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage.DirService;
import ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage.DirectoryException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DirService dirService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl( UserRepository userRepository, RoleRepository roleRepository, DirService dirService, PasswordEncoder passwordEncoder )
    {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.dirService = dirService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void save( UserEntity user )
    {
        if ( userNotInDB( user ) )
        {
            processUser( user );
            userRepository.save( user );
        }
    }

    private UserEntity processUser( UserEntity user )
    {
        user.setPassword( passwordEncoder.encode( user.getPassword() ) );
        Role role_user = roleRepository.findRoleByName( "Role_User" );
        try
        {
            user.setDir( dirService.generateNewDir() );
        }
        catch ( DirectoryException e )
        {
            //TODO add logger and UserService Exception
            throw new RuntimeException( e );
        }
        if ( role_user != null )
            user.addRole( role_user );
        return user;
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
    public void saveAll( List<UserEntity> entities )
    {
        userRepository.saveAll(
                entities.stream().filter( this::userNotInDB )
                        .distinct()
                        .map( this::processUser )
                        .collect( Collectors.toList() )
        );
    }

    private boolean userNotInDB( UserEntity user )
    {
        return findByUsername( user.getUsername() ) == null;
    }
}
