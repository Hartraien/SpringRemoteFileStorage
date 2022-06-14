package ru.hartraien.SpringCloudStorageProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:credentials.properties")
@PropertySource("classpath:db.properties")
public class SpringCloudStorageProjectApplication
{

    public static void main( String[] args )
    {
        SpringApplication.run( SpringCloudStorageProjectApplication.class, args );
    }

}
