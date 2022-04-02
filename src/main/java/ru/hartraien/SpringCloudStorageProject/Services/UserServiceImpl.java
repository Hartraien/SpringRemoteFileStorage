package ru.hartraien.SpringCloudStorageProject.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Repositories.RoleRepository;
import ru.hartraien.SpringCloudStorageProject.Repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl( UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder )
    {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void save( UserEntity user )
    {
        if ( userNotExists( user ) )
        {
            encodeAndAddUserRole( user );
            userRepository.save( user );
        }
    }

    private UserEntity encodeAndAddUserRole( UserEntity user )
    {
        user.setPassword( passwordEncoder.encode( user.getPassword() ) );
        user.addRole( roleRepository.findRoleByName( "Role_User" ) );
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
    public void saveAdmin( UserEntity user )
    {
        user.setPassword( passwordEncoder.encode( user.getPassword() ) );
        userRepository.save( user );
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
                entities.stream().filter( this::userNotExists )
                        .distinct()
                        .map( this::encodeAndAddUserRole )
                        .collect( Collectors.toList() )
        );
    }

    private boolean userNotExists( UserEntity user )
    {
        return findByUsername( user.getUsername() ) == null;
    }
}
