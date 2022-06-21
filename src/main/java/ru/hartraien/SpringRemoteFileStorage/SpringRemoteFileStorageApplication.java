package ru.hartraien.SpringRemoteFileStorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:credentials.properties")
@PropertySource("classpath:db.properties")
public class SpringRemoteFileStorageApplication
{

    public static void main( String[] args )
    {
        SpringApplication.run( SpringRemoteFileStorageApplication.class, args );
    }

}
