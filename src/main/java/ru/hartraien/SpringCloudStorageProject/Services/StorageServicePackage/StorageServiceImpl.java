package ru.hartraien.SpringCloudStorageProject.Services.StorageServicePackage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.hartraien.SpringCloudStorageProject.DTOs.FileDTO;
import ru.hartraien.SpringCloudStorageProject.DTOs.FileDTOComparator;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StorageServiceImpl implements StorageService
{
    private final Path rootLocation;
    private final String storage;

    public StorageServiceImpl( @Value("storage") String storageValue ) throws StorageException
    {
        storage = storageValue;
        this.rootLocation = Path.of( storage );
        if ( !Files.exists( this.rootLocation ) ) createDirForPath( this.rootLocation );
        else clearMainFolder();
    }

    @Override
    public void createDir( String directory ) throws StorageException
    {
        Path dirPath = this.getUserRoot( directory );
        createDirForPath( dirPath );
    }

    @Override
    public List<FileDTO> getAllFilesInDir( String dirname, String subPath ) throws StorageException
    {
        Path relative = getUserRoot( dirname );
        Path full = getFullPath( dirname, subPath, " is not a subdirectory of user folder" );
        try ( var stream = getFilesInPath( full ) )
        {
            return stream.map( path -> new FileDTO( path, relative ) ).sorted( new FileDTOComparator() ).collect( Collectors.toList() );
        }
        catch ( IOException e )
        {
            throw new StorageException( "Could not parse subdirectory " + subPath, e );
        }
    }

    @Override
    public Resource getFile( String dirname, String filePath ) throws StorageException
    {
        Path full = getFullPath( dirname, filePath, " is not a subdirectory of user folder" );
        try
        {
            Resource resource = new UrlResource( full.toUri() );
            if ( resource.exists() || resource.isReadable() ) return resource;
            else throw new StorageException( "Resource is inaccessible" );
        }
        catch ( MalformedURLException e )
        {
            throw new StorageException( "Coudl not get file", e );
        }
    }

    @Override
    public void storeFile( String dirname, String subPath, MultipartFile file ) throws StorageException
    {
        Path full = getFullPath( dirname, subPath, " is not a subdirectory of user's folder" );
        Path destinationFile = full.resolve( file.getOriginalFilename() ).normalize();
        if ( !destinationFile.startsWith( full ) )
            throw new StorageException( "File " + file.getOriginalFilename() + " is not in directory " + subPath );
        try ( InputStream inputStream = file.getInputStream() )
        {
            Files.copy( inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING );
        }
        catch ( IOException e )
        {
            throw new StorageException( "Could not save file", e );
        }
    }

    @Override
    public void createSubDir( String dirname, String subPath, String dirName ) throws StorageException
    {
        Path full = getFullPath( dirname, subPath, " is not a subdirectory of user's folder" );
        Path newDir = full.resolve( dirName ).normalize();
        if ( !newDir.startsWith( full ) )
            throw new StorageException( "Directory " + dirName + " is not in directory " + subPath );
        this.createDirForPath( newDir );
    }

    @Override
    public void delete( String dirname, String pathToFile ) throws StorageException
    {
        Path full = getFullPath( dirname, pathToFile, " is not in directory" );
        if ( !Files.exists( full ) ) throw new StorageException( "No such file" );
        try
        {
            if ( Files.isDirectory( full ) ) FileSystemUtils.deleteRecursively( full );
            else Files.delete( full );
        }
        catch ( IOException e )
        {
            throw new StorageException( "Could not delete file", e );
        }
    }

    private void clearMainFolder()
    {
        FileSystemUtils.deleteRecursively( rootLocation.toFile() );
    }

    private Path getUserRoot( String dir )
    {
        return rootLocation.resolve( dir ).normalize();
    }

    private Path getFullPath( String dirname, String filePath, String errorMessage ) throws StorageException
    {
        Path relative = getUserRoot( dirname );
        Path full = relative.resolve( filePath ).normalize();
        if ( !full.startsWith( relative ) ) throw new StorageException( filePath + errorMessage );
        return full;
    }

    private Stream<Path> getFilesInPath( Path full ) throws IOException
    {
        return Files.walk( full, 1 ).filter( path -> !full.equals( path ) );
    }

    private void createDirForPath( Path dirPath ) throws StorageException
    {
        try
        {
            Files.createDirectories( dirPath );
        }
        catch ( IOException e )
        {
            throw new StorageException( "Could not create dir for " + dirPath );
        }
    }

}
