package ru.hartraien.SpringCloudStorageProject.Services.SecurityServicePackage;

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
import ru.hartraien.SpringCloudStorageProject.Repositories.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl( UserRepository userRepository )
    {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException
    {
        final UserEntity userByUsername = userRepository.findUserByUsername( username );
        if ( userByUsername == null )
            throw new UsernameNotFoundException( username );

        Set<GrantedAuthority> authorities = new HashSet<>();

        for ( Role role : userByUsername.getRoles() )
        {
            authorities.add( new SimpleGrantedAuthority( role.getName() ) );
        }

        return new User( userByUsername.getUsername(), userByUsername.getPassword(), authorities );

    }
}
