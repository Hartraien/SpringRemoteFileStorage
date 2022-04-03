package ru.hartraien.SpringCloudStorageProject.Services.RoleServicePackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hartraien.SpringCloudStorageProject.Entities.Role;
import ru.hartraien.SpringCloudStorageProject.Repositories.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService
{
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl( RoleRepository roleRepository )
    {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findRoleByName( String name )
    {
        return roleRepository.findRoleByName( name );
    }

    @Override
    public void save( Role role )
    {
        roleRepository.save( role );
    }
}
