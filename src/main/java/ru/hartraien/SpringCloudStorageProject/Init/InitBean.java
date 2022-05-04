package ru.hartraien.SpringCloudStorageProject.Init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.hartraien.SpringCloudStorageProject.Entities.Role;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Services.StorageServicePackage.StorageService;
import ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage.UserService;
import ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage.UserServiceException;
import ru.hartraien.SpringCloudStorageProject.Utility.RandomStringProducer;
import ru.hartraien.SpringCloudStorageProject.Utility.StringProducer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Adds roles USER and ADMIN to DB on system startup
 * Also adds ADMIN user to db
 */
@Component
public class InitBean
{
    private final UserService userService;
    private final StorageService storageService;

    private final ApplicationContext context;
    private final Logger logger;
    private String adminUsername;
    private String adminPassword;

    private String adminEmail;


    @Autowired
    public InitBean( UserService userService
            , StorageService storageService
            , ApplicationContext context
            , @Value("${admin.username}") String adminUsername
            , @Value("${admin.password}") String adminPassword
            , @Value("${admin.email}") String adminEmail )
    {
        this.userService = userService;
        this.storageService = storageService;
        this.context = context;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
        this.adminEmail = adminEmail;
        logger = LoggerFactory.getLogger( InitBean.class );
    }

    /**
     * Waits for application stratup to add roles and users to db
     *
     * @param event - reference to listened event, ignored
     */
    //@EventListener(ApplicationStartedEvent.class)
    public void onApplicationStart( ApplicationStartedEvent event )
    {
        Role userRole = Role.Role_User;


        Role adminRole = Role.Role_Admin;


        UserEntity admin = generateAdminUser( userRole, adminRole );
        try
        {
            userService.save( admin );
        }
        catch ( UserServiceException e )
        {
            logger.error( "Could not create admin user, shutting down" );
            SpringApplication.exit( context );
        }

        int UserCount = 10;

        generateNRandomUsers( userRole, UserCount );

        logger.info( "admin directory = " + admin.getDir().getDirname() );

        fillAdminDir( admin );

        adminUsername = null;
        adminPassword = null;
        adminEmail = null;
    }

    private void fillAdminDir( UserEntity admin )
    {
        Path destFolder = Path.of( "storage", admin.getDir().getDirname() );
        Path from = Path.of( "AdminFolderContent" );
        try ( var files = Files.walk( from ) )
        {
            files.filter( path -> !path.equals( from ) )
                    .forEach( path -> fileCopy( destFolder, from, path ) );
        }
        catch ( IOException e )
        {
            logger.error( "Could not fill admin directory", e );
            e.printStackTrace();
        }
    }

    private void fileCopy( Path destFolder, Path from, Path path )
    {
        Path dest = Paths.get( destFolder.toString(), path.toString().substring( from.toString().length() ) );
        try
        {
            Files.copy( path, dest );
        }
        catch ( IOException e )
        {
            logger.error( "Could not copy file " + dest.getFileName() );
            e.printStackTrace();
        }
    }

    private UserEntity generateAdminUser( Role userRole, Role adminRole )
    {
        UserEntity admin = new UserEntity();
        admin.setEmail( adminEmail );
        admin.setUsername( adminUsername );
        admin.setPassword( adminPassword );
        admin.addRole( adminRole );
        admin.addRole( userRole );
        return admin;
    }

    private void generateNRandomUsers( Role userRole, int UserCount )
    {
        StringProducer stringProducer = new RandomStringProducer();
        List<UserEntity> entities = new ArrayList<>( UserCount );
        for ( int i = 0; i < UserCount; i++ )
        {
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername( stringProducer.getString( 5 ) );
            userEntity.setPassword( stringProducer.getString( 5 ) );
            userEntity.addRole( userRole );
            entities.add( userEntity );
        }

        userService.saveAll( entities );
    }
}
