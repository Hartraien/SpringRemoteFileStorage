package ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.hartraien.SpringCloudStorageProject.DTOs.FileDTO;
import ru.hartraien.SpringCloudStorageProject.Entities.DirectoryEntity;
import ru.hartraien.SpringCloudStorageProject.Repositories.DirRepository;
import ru.hartraien.SpringCloudStorageProject.Services.StorageServicePackage.StorageException;
import ru.hartraien.SpringCloudStorageProject.Services.StorageServicePackage.StorageService;
import ru.hartraien.SpringCloudStorageProject.Utility.RandomStringProducer;
import ru.hartraien.SpringCloudStorageProject.Utility.StringProducer;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class DirServiceImplTest
{
    private final String storageName = "testStorage";
    private DirRepository dirRepository;
    private StorageService storageService;
    private DirService dirService;

    @BeforeEach
    void init()
    {
        dirRepository = Mockito.mock( DirRepository.class );
        storageService = Mockito.mock( StorageService.class );
        dirService = new DirServiceImpl( dirRepository, storageService );
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
        try
        {
            DirectoryEntity directoryEntity = dirService.generateNewDir();
            Assertions.assertEquals( dirName[0], directoryEntity.getDirname() );
        }
        catch ( DirectoryException e )
        {
            Assertions.fail( "Could not generate new directory: " + e.getMessage() );
        }
    }

    @Test
    void getFilesInDir()
    {
        StringProducer stringProducer = new RandomStringProducer();
        List<FileDTO> files = new ArrayList<>();
        for ( int i = 0; i < 10; i++ )
        {
            files.add( new FileDTO( Path.of( storageName ), Path.of( stringProducer.getString( 5 ) ) ) );
        }

        Mockito.when( dirRepository.findByDirname( Mockito.anyString() ) ).thenReturn( new DirectoryEntity() );
        try
        {
            Mockito.when( storageService.getAllFilesInDir( Mockito.anyString(), Mockito.anyString() ) ).thenReturn( files );
            DirectoryEntity stub = new DirectoryEntity();
            stub.setDirname( "" );
            List<FileDTO> getFiles = dirService.getFilesInDir( stub, "" );
            Assertions.assertSame( getFiles, files );
        }
        catch ( StorageException e )
        {
            Assertions.fail( "Could not mock storage service: " + e.getMessage() );
        }
        catch ( DirectoryException e )
        {
            Assertions.fail( "Failed to get all files in dirService: " + e.getMessage() );
        }
    }

    @Test
    void getFile()
    {
        Resource file = new MockMultipartFile( "test",
                "test",
                "text/plain",
                "testcontent".getBytes( StandardCharsets.UTF_8 ) )
                .getResource();

        Mockito.when( dirRepository.findByDirname( Mockito.anyString() ) ).thenReturn( new DirectoryEntity() );
        try
        {
            Mockito.when( storageService.getFile( Mockito.anyString(), Mockito.anyString() ) ).thenReturn( file );
            DirectoryEntity stub = new DirectoryEntity();
            stub.setDirname( "" );
            Resource fileFromStorage = dirService.getFile( stub, "" );

            Assertions.assertSame( file, fileFromStorage );
        }
        catch ( StorageException e )
        {
            Assertions.fail( "Could not mock storage service: " + e.getMessage() );
        }
        catch ( DirectoryException e )
        {
            Assertions.fail( "Failed to get file in dirService: " + e.getMessage() );
        }

    }

    @Test
    void storeFile()
    {
        MultipartFile file = new MockMultipartFile( "test",
                "test",
                "text/plain",
                "testcontent".getBytes( StandardCharsets.UTF_8 ) );

        Mockito.when( dirRepository.findByDirname( Mockito.anyString() ) ).thenReturn( new DirectoryEntity() );
        try
        {
            final boolean[] marker = { false };
            Mockito.doAnswer( invocationOnMock ->
            {
                if ( file == invocationOnMock.getArgument( 2 ) )
                    marker[0] = true;
                return null;
            } ).when( storageService ).storeFile( Mockito.anyString(), Mockito.anyString(), Mockito.any( MultipartFile.class ) );
            DirectoryEntity stub = new DirectoryEntity();
            stub.setDirname( "" );
            dirService.storeFile( stub, "", file );

            Assertions.assertTrue( marker[0] );
        }
        catch ( StorageException e )
        {
            Assertions.fail( "Could not mock storage service: " + e.getMessage() );
        }
        catch ( DirectoryException e )
        {
            Assertions.fail( "Failed to get file in dirService: " + e.getMessage() );
        }
    }

    @Test
    void createDir()
    {
        String dirName = "testSubDirName";
        Mockito.when( dirRepository.findByDirname( Mockito.anyString() ) ).thenReturn( new DirectoryEntity() );
        try
        {
            final boolean[] marker = { false };
            Mockito.doAnswer( invocationOnMock ->
            {
                if ( dirName == invocationOnMock.getArgument( 2 ) )
                    marker[0] = true;
                return null;
            } ).when( storageService ).createSubDir( Mockito.anyString(), Mockito.anyString(), Mockito.anyString() );
            DirectoryEntity stub = new DirectoryEntity();
            stub.setDirname( "" );
            dirService.createDir( stub, "", dirName );

            Assertions.assertTrue( marker[0] );
        }
        catch ( StorageException e )
        {
            Assertions.fail( "Could not mock storage service: " + e.getMessage() );
        }
        catch ( DirectoryException e )
        {
            Assertions.fail( "Failed to get file in dirService: " + e.getMessage() );
        }
    }

    @Test
    void dirExists()
    {
        String dirName = "testDirName";
        Mockito.when( dirRepository.findByDirname( Mockito.anyString() ) ).thenAnswer( invocationOnMock ->
        {
            if ( dirName.equals( invocationOnMock.getArgument( 0 ) ) )
                return new DirectoryEntity();
            else
                return null;
        } );

        DirectoryEntity testDir = new DirectoryEntity();
        testDir.setDirname( dirName );

        Assertions.assertTrue( dirService.dirExists( testDir ) );

        StringProducer stringProducer = new RandomStringProducer();
        for ( int i = 0; i < 10; i++ )
        {
            DirectoryEntity dir = new DirectoryEntity();
            dir.setDirname( stringProducer.getString( 5 ) );
            Assertions.assertFalse( dirService.dirExists( dir ) );
        }
    }

    @Test
    void delete()
    {
        String toDelete = "testSubDirName";
        Mockito.when( dirRepository.findByDirname( Mockito.anyString() ) ).thenReturn( new DirectoryEntity() );
        try
        {
            final boolean[] marker = { false };
            Mockito.doAnswer( invocationOnMock ->
            {
                if ( toDelete.equals( invocationOnMock.getArgument( 1 ) ) )
                    marker[0] = true;
                return null;
            } ).when( storageService ).delete( Mockito.anyString(), Mockito.anyString() );
            DirectoryEntity stub = new DirectoryEntity();
            stub.setDirname( "" );
            dirService.delete( stub, toDelete );

            Assertions.assertTrue( marker[0] );
        }
        catch ( StorageException e )
        {
            Assertions.fail( "Could not mock storage service: " + e.getMessage() );
        }
        catch ( DirectoryException e )
        {
            Assertions.fail( "Failed to delete file in dirService: " + e.getMessage() );
        }
    }
}