package ru.hartraien.SpringCloudStorageProject.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.hartraien.SpringCloudStorageProject.Entities.Role;

@Transactional(propagation = Propagation.MANDATORY)
public interface RoleRepository extends JpaRepository<Role, Long>
{
    Role findRoleByName( String name );
}
