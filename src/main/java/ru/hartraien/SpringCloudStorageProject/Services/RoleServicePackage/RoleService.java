package ru.hartraien.SpringCloudStorageProject.Services.RoleServicePackage;

import ru.hartraien.SpringCloudStorageProject.Entities.Role;

/**
 * Encapsulates working with Role DB
 */
public interface RoleService
{
    /**
     * Finds role by role name
     * @param name - name of role
     * @return - role with given name of null otherwise
     */
    Role findRoleByName( String name );

    /**
     * Saves new role to DB
     * @param role - role to save
     */
    void save( Role role );
}
