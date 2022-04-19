package ru.hartraien.SpringCloudStorageProject.Services.RoleServicePackage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.hartraien.SpringCloudStorageProject.Entities.Role;
import ru.hartraien.SpringCloudStorageProject.Repositories.RoleRepository;
import ru.hartraien.SpringCloudStorageProject.Utility.RandomStringProducer;
import ru.hartraien.SpringCloudStorageProject.Utility.StringProducer;

class RoleServiceImplTest
{
    private RoleRepository roleRepository;
    private RoleService roleService;

    @BeforeEach
    void init()
    {
        roleRepository = Mockito.mock( RoleRepository.class );
        roleService = new RoleServiceImpl( roleRepository );
    }

    @Test
    void findRoleByName()
    {
        String name = "Role_Test";
        Role role_test = new Role();
        role_test.setName( name );
        Mockito.when( roleRepository.findRoleByName( Mockito.anyString() ) ).then( invocationOnMock ->
        {
            if ( name.equals( invocationOnMock.getArgument( 0 ) ) )
                return role_test;
            return null;
        } );

        Assertions.assertSame( role_test, roleService.findRoleByName( name ) );

        StringProducer stringProducer = new RandomStringProducer();
        for ( int i = 0; i < 10; i++ )
        {
            String role_temp_name = stringProducer.getString( 5 );
            Assertions.assertNull( roleService.findRoleByName( role_temp_name ) );
        }
    }

    @Test
    void save()
    {
        Role role = new Role();
        role.setName( "role_test" );
        final int[] marker = { 0 };
        Mockito.doAnswer( invocationOnMock ->
        {
            if ( role == invocationOnMock.getArgument( 0 ) )
                marker[0] = -1;
            else
                marker[0]++;
            return null;
        } ).when( roleRepository ).save( Mockito.any( Role.class ) );

        StringProducer stringProducer = new RandomStringProducer();
        for ( int i = 0; i < 10; i++ )
        {
            String role_temp_name = stringProducer.getString( 5 );
            Role role_temp = new Role();
            roleService.save( role_temp );
            Assertions.assertEquals( i + 1, marker[0] );
        }

        roleService.save( role );
        Assertions.assertEquals( -1, marker[0] );
    }
}