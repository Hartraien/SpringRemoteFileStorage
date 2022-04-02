package ru.hartraien.SpringCloudStorageProject.Init;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import ru.hartraien.SpringCloudStorageProject.Entities.Role;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Services.RoleService;
import ru.hartraien.SpringCloudStorageProject.Services.UserService;

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


    public InitBean( RoleService roleService, UserService userService )
    {
        this.roleService = roleService;
        this.userService = userService;
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

        UserEntity admin = new UserEntity();
        admin.setUsername( "admin" );
        admin.setPassword( "1234" );
        admin.addRole( adminRole );
        admin.addRole( userRole );
        userService.saveAdmin( admin );

        int UserCount = 200;


        generateNRandomUsers( userRole, UserCount );


    }

    private void generateNRandomUsers( Role userRole, int UserCount )
    {
        RandomStringProducer randomStringProducer = new RandomStringProducerImpl();
        List<UserEntity> entities = new ArrayList<>(UserCount);
        for ( int i = 0; i < UserCount; i++ )
        {
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername( randomStringProducer.getString( 5 ) );
            userEntity.setPassword( randomStringProducer.getString( 5 ) );
            userEntity.addRole( userRole );
            entities.add( userEntity );
        }

        userService.saveAll(entities);
    }
}
