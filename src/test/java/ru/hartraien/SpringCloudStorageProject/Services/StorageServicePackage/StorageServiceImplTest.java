package ru.hartraien.SpringCloudStorageProject.Services.StorageServicePackage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.util.FileSystemUtils;
import ru.hartraien.SpringCloudStorageProject.DTOs.FileDTO;
import ru.hartraien.SpringCloudStorageProject.Utility.RandomStringProducer;
import ru.hartraien.SpringCloudStorageProject.Utility.StringProducer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

class StorageServiceImplTest
{
    private StorageService storageService;

    private final String storageName = "testStorage";

    @BeforeEach
    void beforeEach() throws StorageException
    {
        storageService = new StorageServiceImpl( storageName );
    }

    @AfterEach
    void clearFolder()
    {
        try
        {
            FileSystemUtils.deleteRecursively( Path.of( storageName ) );
        }
        catch ( IOException ignored )
        {

        }
    }

    @Test
    void createDir()
    {
        StringProducer stringProducer = new RandomStringProducer();
        String dirName = stringProducer.getString( 10 );
        try
        {
            storageService.createDir( dirName );
        }
        catch ( StorageException e )
        {
            Assertions.fail( "Could not create dir " + e.getMessage() );
        }
        Assertions.assertTrue( Files.exists( Path.of( storageName, dirName ) ) );
    }

    @Test
    void getAllFilesInDir()
    {
        StringProducer stringProducer = new RandomStringProducer();
        String dirName = stringProducer.getString( 10 );
        try
        {
            storageService.createDir( dirName );
        }
        catch ( StorageException e )
        {
            Assertions.fail( "Could not create dir " + e.getMessage() );
        }
        List<String> filenames = new ArrayList<>();
        Path dirPath = Path.of( storageName, dirName );
        for ( int i = 0; i < 5; i++ )
        {
            while ( true )
            {
                String filename = stringProducer.getString( 10 );
                if ( !filenames.contains( filename ) )
                {
                    filenames.add( stringProducer.getString( 10 ) );
                    break;
                }
            }
            try
            {
                Files.createFile( dirPath.resolve( filenames.get( filenames.size() - 1 ) ) );
            }
            catch ( IOException e )
            {
                Assertions.fail( "Could not create file " + e.getMessage() );
            }
        }
        try
        {
            var files = storageService.getAllFilesInDir( dirName, "" ).stream().map( FileDTO::getFileName ).collect( Collectors.toList() );
            Assertions.assertTrue( files.size() == filenames.size() && files.containsAll( filenames ) );
        }
        catch ( StorageException e )
        {
            Assertions.fail( "Could not get all files in dir due to " + e.getMessage() );
        }
    }

    @Test
    void getFile()
    {
        StringProducer stringProducer = new RandomStringProducer();
        String dirName = stringProducer.getString( 10 );
        try
        {
            storageService.createDir( dirName );
        }
        catch ( StorageException e )
        {
            Assertions.fail( "Could not create dir " + e.getMessage() );
        }
        String filename = generateRandomFileInDir( dirName );
        try
        {
            Resource file = storageService.getFile( dirName, filename );
            Assertions.assertEquals( file.getFilename(), filename );
            Assertions.assertTrue( compareFiles( file.getFile(), Path.of( storageName,dirName, filename ).toFile() ) );
        }
        catch ( StorageException e )
        {
            Assertions.fail( "Could not load file " + e.getMessage() );
        }
        catch ( IOException e )
        {
            Assertions.fail( "Could not get file from resource " + e.getMessage() );
        }
    }

    private boolean compareFiles( File fileFirst, File fileSecond ) throws IOException
    {
        try (
                BufferedInputStream fs1 = new BufferedInputStream( new FileInputStream( fileFirst ) );
                BufferedInputStream fs2 = new BufferedInputStream( new FileInputStream( fileSecond ) )
        )
        {
            final int length = 1024;
            int chFirst;
            int chSecond;
            byte[] bufferFirst = new byte[length];
            byte[] bufferSecond = new byte[length];
            while ( true )
            {
                chFirst = fs1.read( bufferFirst );
                chSecond = fs2.read( bufferSecond );
                if ( chFirst == -1 && chSecond == -1 )
                    break;
                if ( chFirst != chSecond || !Arrays.equals( bufferFirst, bufferSecond ) )
                    return false;
            }
        }
        catch ( IOException e )
        {
            throw e;
        }
        return true;
    }

    private String generateRandomFileInDir( String dirName )
    {
        StringProducer stringProducer =new RandomStringProducer();
        String filename = stringProducer.getString( 10 );
        String content = stringProducer.getString( ThreadLocalRandom.current().nextInt(1, 1000) );
        Path filePath = Path.of( storageName, dirName, filename );
        try
        {
            Files.createFile( filePath );
            Files.write( filePath, content.getBytes( StandardCharsets.UTF_8 ) );
        }
        catch ( IOException e )
        {
            //TODO add normal exception thrown
            throw new RuntimeException( e );
        }
        return filename;
    }

    @Test
    void storeFile()
    {
    }

    @Test
    void createSubDir()
    {
    }

    @Test
    void delete()
    {
    }
}