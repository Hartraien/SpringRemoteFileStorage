package ru.hartraien.SpringCloudStorageProject.Services;

import ru.hartraien.SpringCloudStorageProject.Entities.Role;

public interface RoleService
{
    Role findRoleByName( String name );

    void save( Role role );
}
