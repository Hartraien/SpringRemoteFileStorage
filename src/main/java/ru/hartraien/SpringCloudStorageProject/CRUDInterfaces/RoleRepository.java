package ru.hartraien.SpringCloudStorageProject.CRUDInterfaces;

import org.springframework.data.repository.CrudRepository;
import ru.hartraien.SpringCloudStorageProject.POJO.Role;

public interface RoleRepository extends CrudRepository<Role, Long>
{
    Role findRoleByName( String name );
}
