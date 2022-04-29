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
import ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage.UserService;

import java.util.stream.Collectors;

class UserDetailsServiceImplTest
{
    private UserService userRepository;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void init()
    {
        userRepository = Mockito.mock( UserService.class );
        userDetailsService = new UserDetailsServiceImpl( userRepository );
    }

    @Test
    void loadUserByUsername()
    {
        String username = "username";
        String password = "password";
        Role role = Role.Role_User;

        UserEntity user = new UserEntity();
        user.setUsername( username );
        user.setPassword( password );
        user.addRole( role );

        Mockito.when( userRepository.findByUsername( username ) ).thenReturn( user );

        UserDetails userFromService = userDetailsService.loadUserByUsername( username );
        Assertions.assertEquals( user.getUsername(), userFromService.getUsername() );
        Assertions.assertEquals( user.getPassword(), userFromService.getPassword() );
        Assertions.assertEquals(
                user.getRoles().stream().map( Role::name ).collect( Collectors.toSet() ),
                userFromService.getAuthorities().stream().map( GrantedAuthority::getAuthority ).collect( Collectors.toSet() )
        );
    }
}