package ru.hartraien.SpringCloudStorageProject.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.hartraien.SpringCloudStorageProject.CRUDInterfaces.RoleRepository;
import ru.hartraien.SpringCloudStorageProject.CRUDInterfaces.UserRepository;
import ru.hartraien.SpringCloudStorageProject.POJO.Role;
import ru.hartraien.SpringCloudStorageProject.POJO.UserEntity;

@Component
public class InitializationBean
{
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public InitializationBean( RoleRepository roleRepository, UserRepository userRepository )
    {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }


    @EventListener(ApplicationStartedEvent.class)
    public void onApplicationFinish( ApplicationStartedEvent event )
    {
        Role admin = new Role("admin");
        Role user = new Role("user");
        roleRepository.save( admin );
        roleRepository.save( user );

        UserEntity adminUser = new UserEntity("admin", "1234", admin);
        userRepository.save( adminUser );
    }

}
