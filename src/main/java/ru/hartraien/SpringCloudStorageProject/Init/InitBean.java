package ru.hartraien.SpringCloudStorageProject.Init;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.hartraien.SpringCloudStorageProject.Entities.Role;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Services.RoleService;
import ru.hartraien.SpringCloudStorageProject.Services.UserService;

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
    }
}
