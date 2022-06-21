package ru.hartraien.SpringRemoteFileStorage.Services.DirServicePackage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.hartraien.SpringRemoteFileStorage.Entities.DirectoryEntity;
import ru.hartraien.SpringRemoteFileStorage.Repositories.DirRepository;
import ru.hartraien.SpringRemoteFileStorage.Utility.RandomStringProducer;
import ru.hartraien.SpringRemoteFileStorage.Utility.StringProducer;

class DirServiceImplTest
{
    private DirRepository dirRepository;
    private DirService dirService;

    @BeforeEach
    void init()
    {
        dirRepository = Mockito.mock( DirRepository.class );
        dirService = new DirServiceImpl( dirRepository );
    }

    @Test
    void dirExists()
    {
        String name = "dirName";
        DirectoryEntity directoryValid = new DirectoryEntity();
        directoryValid.setDirname( name );
        Mockito.when( dirRepository.findByDirname( Mockito.anyString() ) ).thenAnswer( invocationOnMock ->
        {
            if ( name.equals( invocationOnMock.getArgument( 0 ) ) )
                return new DirectoryEntity();
            return null;
        } );

        StringProducer stringProducer = new RandomStringProducer();

        Assertions.assertTrue( dirService.dirExists( directoryValid ) );
        for ( int i = 0; i < 10; i++ )
        {
            DirectoryEntity directoryInvalid = new DirectoryEntity();
            directoryInvalid.setDirname( stringProducer.getString( 10 ) );
            Assertions.assertFalse( dirService.dirExists( directoryInvalid ) );
        }
    }

    @Test
    void checkIfDirExistsOrThrow()
    {
        String name = "dirName";
        DirectoryEntity directoryValid = new DirectoryEntity();
        directoryValid.setDirname( name );
        Mockito.when( dirRepository.findByDirname( Mockito.anyString() ) ).thenAnswer( invocationOnMock ->
        {
            if ( name.equals( invocationOnMock.getArgument( 0 ) ) )
                return new DirectoryEntity();
            return null;
        } );

        StringProducer stringProducer = new RandomStringProducer();

        Assertions.assertDoesNotThrow( () -> dirService.checkIfDirExistsOrThrow( directoryValid ) );

        for ( int i = 0; i < 10; i++ )
        {
            DirectoryEntity directoryInvalid = new DirectoryEntity();
            directoryInvalid.setDirname( stringProducer.getString( 10 ) );
            Assertions.assertThrows( DirectoryException.class, () -> dirService.checkIfDirExistsOrThrow( directoryInvalid ) );
        }
    }

    @Test
    void generateNewDir()
    {
        final String[] dirName = new String[1];
        Mockito.when( dirRepository.findByDirname( Mockito.anyString() ) ).thenAnswer( invocationOnMock ->
        {
            dirName[0] = invocationOnMock.getArgument( 0 );
            return null;
        } );
        DirectoryEntity directoryEntity = dirService.generateNewDir();
        Assertions.assertEquals( dirName[0], directoryEntity.getDirname() );
    }


}