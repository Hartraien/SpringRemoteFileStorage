package ru.hartraien.SpringCloudStorageProject.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Repositories.RoleRepository;
import ru.hartraien.SpringCloudStorageProject.Repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

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
        user.setPassword( passwordEncoder.encode( user.getPassword() ) );
        user.addRole( roleRepository.findRoleByName( "Role_User" ) );
        userRepository.save( user );
    }

    @Override
    public UserEntity findByUsername( String username )
    {
        return userRepository.findUserByUsername( username );
    }

    @Override
    public List<UserEntity> getAllUsers()
    {
        List<UserEntity> result = new ArrayList<>();
        userRepository.findAll().forEach( result::add );
        return result;
    }

    @Override
    public void saveAdmin( UserEntity user )
    {
        user.setPassword( passwordEncoder.encode( user.getPassword() ) );
        userRepository.save( user );
    }
}
