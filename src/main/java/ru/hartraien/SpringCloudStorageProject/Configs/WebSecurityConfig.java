package ru.hartraien.SpringCloudStorageProject.Configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public WebSecurityConfig( UserDetailsService userDetailsService, PasswordEncoder passwordEncoder )
    {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public AuthenticationManager customauthenticationManager() throws Exception
    {
        return authenticationManager();
    }

    @Override
    protected void configure( AuthenticationManagerBuilder auth ) throws Exception
    {
        auth.userDetailsService( userDetailsService ).passwordEncoder( passwordEncoder );
    }

    @Override
    protected void configure( HttpSecurity http ) throws Exception
    {
        http
                //.csrf().disable()
                .authorizeRequests()
                .antMatchers( "/lockedpage", "/uploadpage/**", "/download/**", "/viewfiles/**", "/userinfo/**" ).hasAnyAuthority( "Role_Admin", "Role_User" )
                .antMatchers( "/userlist/**" ).hasAuthority( "Role_Admin" )
                .antMatchers( "/", "/register" ).permitAll()
                .antMatchers( "/**" ).authenticated()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage( "/login" )
                .defaultSuccessUrl( "/" )
                .permitAll()
                .and()
                .logout()
                .logoutUrl( "/logout" )
                .logoutSuccessUrl( "/" )
                .invalidateHttpSession( true )
                .permitAll();

        http.headers().frameOptions().disable();

    }
}
