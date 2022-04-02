package ru.hartraien.SpringCloudStorageProject.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ru.hartraien.SpringCloudStorageProject.Entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long>
{
    Role findRoleByName( String name );
}
