package ru.hartraien.SpringCloudStorageProject.ConfigsForTest;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;

@TestConfiguration
public class TestConfig
{

    @Bean
    public UserDetailsService userDetailsService()
    {
        return username -> new User( username, null, new ArrayList<>() );
    }
}
