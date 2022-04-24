package ru.hartraien.SpringCloudStorageProject.Services.SecurityServicePackage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.hartraien.SpringCloudStorageProject.Entities.Role;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage.UserService;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private final UserService userService;
    private final Logger logger;

    @Autowired
    public UserDetailsServiceImpl( UserService userService )
    {
        this.userService = userService;
        logger = LoggerFactory.getLogger( UserDetailsServiceImpl.class );
    }

    @Override
    public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException
    {
        final UserEntity userByUsername = userService.findByUsername( username );
        if ( userByUsername == null )
        {
            logger.error( "Could not find user with username='" + username + "'" );
            throw new UsernameNotFoundException( username );
        }

        Set<GrantedAuthority> authorities = new HashSet<>();

        for ( Role role : userByUsername.getRoles() )
        {
            authorities.add( new SimpleGrantedAuthority( role.getName() ) );
        }

        return new User( userByUsername.getUsername(), userByUsername.getPassword(), authorities );

    }
}
