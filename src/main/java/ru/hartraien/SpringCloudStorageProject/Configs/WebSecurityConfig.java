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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
    private final UserDetailsService userDetailsService;

    @Autowired
    public WebSecurityConfig( UserDetailsService userDetailsService )
    {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager customauthenticationManager() throws Exception
    {
        return authenticationManager();
    }

    @Override
    protected void configure( AuthenticationManagerBuilder auth ) throws Exception
    {
        auth.userDetailsService( userDetailsService ).passwordEncoder( passwordEncoder() );
    }

    @Override
    protected void configure( HttpSecurity http ) throws Exception
    {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers( "/lockedpage", "/uploadpage/**", "/download/**", "/viewfiles/**" ).hasAnyAuthority( "Role_Admin", "Role_User" )
                .antMatchers( "/userlist/**" ).hasAuthority( "Role_Admin" )
                .antMatchers( "/**" ).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage( "/login" )
                .defaultSuccessUrl( "/lockedpage" )
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
