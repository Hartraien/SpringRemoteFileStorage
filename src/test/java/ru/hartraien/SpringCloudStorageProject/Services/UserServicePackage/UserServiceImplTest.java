package ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.hartraien.SpringCloudStorageProject.Entities.DirectoryEntity;
import ru.hartraien.SpringCloudStorageProject.Entities.Role;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Repositories.UserRepository;
import ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage.DirService;
import ru.hartraien.SpringCloudStorageProject.Services.StorageServicePackage.StorageService;
import ru.hartraien.SpringCloudStorageProject.Utility.RandomStringProducer;
import ru.hartraien.SpringCloudStorageProject.Utility.StringProducer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

class UserServiceImplTest
{
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private DirService dirService;
    private StorageService storageService;

    private UserService userService;

    @BeforeEach
    void init()
    {
        passwordEncoder = Mockito.mock( PasswordEncoder.class );
        userRepository = Mockito.mock( UserRepository.class );
        dirService = Mockito.mock( DirService.class );
        storageService = Mockito.mock( StorageService.class );
        userService = new UserServiceImpl( userRepository, dirService, storageService, passwordEncoder );
    }

    @Test
    void save()
    {
        String username = "username";
        String password = "password";
        String dirName = "testDirName";
        String encode_addition = "encoded_";
        String roleName = "Role_User";

        UserEntity user = new UserEntity();
        user.setUsername( username );
        user.setPassword( password );

        Role role = Role.Role_User;

        DirectoryEntity directory = new DirectoryEntity();
        directory.setDirname( dirName );

        final boolean[] marker = { false };
        Mockito.when( userRepository.findUserByUsername( username ) ).thenReturn( null );
        Mockito.when( userRepository.save( user ) ).then( invocationOnMock ->
        {
            marker[0] = true;
            return null;
        } );
        Mockito.when( passwordEncoder.encode( password ) ).thenReturn( encode_addition + password );
        Mockito.when( dirService.generateNewDir() ).thenReturn( directory );

        try
        {
            userService.save( user );
        }
        catch ( UserServiceException e )
        {
            Assertions.fail( "Could not save user: " + e.getMessage() );
        }

        Mockito.verify( userRepository ).save( user );
        Mockito.verify( userRepository ).findUserByUsername( username );
        Mockito.verify( passwordEncoder ).encode( password );
        Mockito.verify( dirService ).generateNewDir();

        Assertions.assertTrue( marker[0] );
        Assertions.assertEquals( username, user.getUsername() );
        Assertions.assertEquals( encode_addition + password, user.getPassword() );
        Assertions.assertEquals( dirName, user.getDir().getDirname() );
        Assertions.assertTrue( user.getRoles() != null && user.getRoles().size() == 1 );
    }

    @Test
    void findByUsername()
    {
        String username = "userName";
        int times = 10;

        UserEntity user = new UserEntity();
        user.setUsername( username );
        Mockito.when( userRepository.findUserByUsername( Mockito.anyString() ) ).then( invocationOnMock ->
        {
            if ( username.equals( invocationOnMock.getArgument( 0 ) ) )
            {
                return user;
            }
            return null;
        } );

        Assertions.assertEquals( user, userService.findByUsername( username ) );

        StringProducer stringProducer = new RandomStringProducer();
        for ( int i = 0; i < times; i++ )
        {
            Assertions.assertNull( userService.findByUsername( stringProducer.getString( 10 ) ) );
        }

        Mockito.verify( userRepository, Mockito.times( times + 1 ) ).findUserByUsername( Mockito.anyString() );
    }

    @Test
    void getAllUsers()
    {
        int times = 10;

        List<UserEntity> users = generateListOfUsers( times );

        Mockito.when( userRepository.findAll() ).thenReturn( users );

        List<UserEntity> extractedUsers = userService.getAllUsers();

        Mockito.verify( userRepository, Mockito.times( 1 ) ).findAll();
        Assertions.assertEquals( users, extractedUsers );

        for ( int i = 0; i < times; i++ )
        {
            Assertions.assertEquals( users.get( i ), extractedUsers.get( i ) );
            Assertions.assertEquals( users.get( i ).getUsername(), extractedUsers.get( i ).getUsername() );
        }
    }

    @Test
    void getAllUsersPaging()
    {
        int times = 10;

        List<UserEntity> users = generateListOfUsers( times );

        int size = ThreadLocalRandom.current().nextInt( 1, users.size() + 1 );


        Mockito.when( userRepository.findAll( Mockito.any( Pageable.class ) ) ).thenAnswer( invocationOnMock ->
        {
            Pageable page = invocationOnMock.getArgument( 0 );
            int pageNumber = page.getPageNumber();
            int pageSize = page.getPageSize();
            return new PageImpl<>( getSubList( users, pageNumber, pageSize ) );
        } );

        for ( int i = 0; i < times; i++ )
        {
            Pageable request = PageRequest.of( ThreadLocalRandom.current().nextInt( 0, users.size() ), size );
            var subList = getSubList( users, request.getPageNumber(), request.getPageSize() );
            var subListSecond = userService.getAllUsersPaging( request );
            Assertions.assertEquals( subList.size(), subListSecond.getContent().size() );
            for ( int j = 0; j < subList.size(); j++ )
            {
                Assertions.assertEquals( subList.get( j ).getUsername(), subListSecond.getContent().get( j ).getUsername() );
            }
        }

        Mockito.verify( userRepository, Mockito.times( times ) ).findAll( Mockito.any( Pageable.class ) );
    }

    @Test
    void saveAll()
    {
        int times = 10;
        List<UserEntity> users = generateListOfUsers( times );

        DirectoryEntity directory = new DirectoryEntity();
        directory.setDirname( "name" );
        Mockito.when( dirService.generateNewDir() ).thenReturn( directory );
        userService.saveAll( users );

        Mockito.verify( userRepository, Mockito.times( 1 ) ).saveAll( Mockito.any() );
    }

    private List<UserEntity> generateListOfUsers( int times )
    {
        List<UserEntity> users = new ArrayList<>();

        StringProducer stringProducer = new RandomStringProducer();

        for ( int i = 0; i < times; i++ )
        {
            UserEntity user = new UserEntity();
            user.setUsername( stringProducer.getString( 10 ) );
            users.add( user );
        }
        return users;
    }

    private List<UserEntity> getSubList( List<UserEntity> list, int pageNumber, int pageSize )
    {
        List<UserEntity> result = new ArrayList<>();
        for ( int i = pageNumber * pageSize; i < ( pageNumber + 1 ) * pageSize && i < list.size(); i++ )
        {
            result.add( list.get( i ) );
        }
        return result;
    }
}