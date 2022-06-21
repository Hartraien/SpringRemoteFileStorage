package ru.hartraien.SpringRemoteFileStorage.Configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import ru.hartraien.SpringRemoteFileStorage.Handlers.MyAccessDeniedHandler;

@Configuration
public class BeanConfig
{
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler()
    {
        return new MyAccessDeniedHandler();
    }
}
