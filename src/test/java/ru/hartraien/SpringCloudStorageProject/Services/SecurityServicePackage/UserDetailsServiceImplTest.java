package ru.hartraien.SpringCloudStorageProject.Services.SecurityServicePackage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.hartraien.SpringCloudStorageProject.Entities.Role;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Repositories.UserRepository;

import java.util.stream.Collectors;

class UserDetailsServiceImplTest
{
    private UserRepository userRepository;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void init()
    {
        userRepository = Mockito.mock( UserRepository.class );
        userDetailsService = new UserDetailsServiceImpl( userRepository );
    }

    @Test
    void loadUserByUsername()
    {
        String username = "username";
        String password = "password";
        String roleName = "Role_User";
        Role role = new Role();
        role.setName( roleName );

        UserEntity user = new UserEntity();
        user.setUsername( username );
        user.setPassword( password );
        user.addRole( role );

        Mockito.when( userRepository.findUserByUsername( username ) ).thenReturn( user );

        UserDetails userFromService = userDetailsService.loadUserByUsername( username );
        Assertions.assertEquals( user.getUsername(), userFromService.getUsername() );
        Assertions.assertEquals( user.getPassword(), userFromService.getPassword() );
        Assertions.assertEquals(
                user.getRoles().stream().map( Role::getName ).collect( Collectors.toSet() ),
                userFromService.getAuthorities().stream().map( GrantedAuthority::getAuthority ).collect( Collectors.toSet() )
        );
    }
}