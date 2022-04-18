package ru.hartraien.SpringCloudStorageProject.Init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.hartraien.SpringCloudStorageProject.Entities.Role;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Services.RoleServicePackage.RoleService;
import ru.hartraien.SpringCloudStorageProject.Services.StorageServicePackage.StorageService;
import ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage.UserService;
import ru.hartraien.SpringCloudStorageProject.Utility.StringProducer;
import ru.hartraien.SpringCloudStorageProject.Utility.RandomStringProducer;

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
    private final RoleService roleService;
    private final UserService userService;
    private final StorageService storageService;


    @Autowired
    public InitBean( RoleService roleService, UserService userService, StorageService storageService )
    {
        this.roleService = roleService;
        this.userService = userService;
        this.storageService = storageService;
    }

    /**
     * Waits for application stratup to add roles and users to db
     *
     * @param event - refrence to listened event, ignored
     */
    @EventListener(ApplicationStartedEvent.class)
    public void onApplicationStart( ApplicationStartedEvent event )
    {
        Role userRole = new Role();
        userRole.setName( "Role_User" );

        roleService.save( userRole );

        Role adminRole = new Role();
        adminRole.setName( "Role_Admin" );

        roleService.save( adminRole );

        UserEntity admin = generateAdminUser( userRole, adminRole );
        userService.save( admin );

        int UserCount = 10;

        generateNRandomUsers( userRole, UserCount );

        System.err.println( admin.getDir().getDirname() );

        fillAdminDir( admin );
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
            e.printStackTrace();
        }
    }

    private UserEntity generateAdminUser( Role userRole, Role adminRole )
    {
        UserEntity admin = new UserEntity();
        admin.setUsername( "admin" );
        admin.setPassword( "1234" );
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
