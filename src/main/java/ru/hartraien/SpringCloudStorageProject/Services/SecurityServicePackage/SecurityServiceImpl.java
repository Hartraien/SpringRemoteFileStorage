package ru.hartraien.SpringCloudStorageProject.Services.SecurityServicePackage;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService
{
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public SecurityServiceImpl( AuthenticationManager authenticationManager, UserDetailsService userDetailsService )
    {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public String findLoggedInUsername()
    {
        final Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
        if ( userDetails instanceof UserDetails )
            return ( (UserDetails) userDetails ).getUsername();

        return null;
    }

    @Override
    public void autologin( String username, String password )
    {
        UserDetails userDetails = userDetailsService.loadUserByUsername( username );
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken( userDetails, password, userDetails.getAuthorities() );
        authenticationManager.authenticate( token );

        if ( token.isAuthenticated() )
        {
            SecurityContextHolder.getContext().setAuthentication( ( token ) );
        }
    }
}
