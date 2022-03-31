package ru.hartraien.SpringCloudStorageProject.DAOs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hartraien.SpringCloudStorageProject.CRUDInterfaces.RoleRepository;
import ru.hartraien.SpringCloudStorageProject.POJO.Role;

@Service
public class RoleDAO
{
    private final RoleRepository roleRepository;

    @Autowired
    public RoleDAO( RoleRepository roleRepository )
    {
        this.roleRepository = roleRepository;
    }

    public Role findRoleByName(String name)
    {
        return roleRepository.findRoleByName( name );
    }
}
