package ru.hartraien.SpringCloudStorageProject.Services.RoleServicePackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hartraien.SpringCloudStorageProject.Entities.Role;
import ru.hartraien.SpringCloudStorageProject.Repositories.RoleRepository;

/**
 * {@inheritDoc}
 */
@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService
{
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl( RoleRepository roleRepository )
    {
        this.roleRepository = roleRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Role findRoleByName( String name )
    {
        return roleRepository.findRoleByName( name );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void save( Role role )
    {
        roleRepository.save( role );
    }
}
