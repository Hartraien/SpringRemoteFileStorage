package ru.hartraien.SpringRemoteFileStorage.Services.StorageServicePackage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.hartraien.SpringRemoteFileStorage.DTOs.FileDTO;
import ru.hartraien.SpringRemoteFileStorage.DTOs.FileDTOComparator;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@inheritDoc}
 * Stores file using filesystem of OS
 */
@Service
public class StorageServiceImpl implements StorageService
{
    private final Path rootLocation;
    private final String storage;

    private final Logger logger;

    public StorageServiceImpl( @Value("storage") String storageValue ) throws StorageException
    {
        logger = LoggerFactory.getLogger( StorageServiceImpl.class );
        storage = storageValue;
        this.rootLocation = Path.of( storage );
        if ( !Files.exists( this.rootLocation ) )
            createDirForPath( this.rootLocation );
        else
            clearMainFolder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createDir( String directory ) throws StorageException
    {
        Path dirPath = this.getUserRoot( directory );
        createDirForPath( dirPath );
    }

    /**
     * {@inheritDoc}
     */
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
            String message = "Could not parse subdirectory " + subPath;
            logger.error( message );
            throw new StorageException( message, e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Resource getFile( String dirname, String filePath ) throws StorageException
    {
        Path full = getFullPath( dirname, filePath, " is not a subdirectory of user folder" );
        try
        {
            Resource resource = new UrlResource( full.toUri() );
            if ( resource.exists() || resource.isReadable() )
                return resource;
            else
                throw new StorageException( "Resource is inaccessible" );
        }
        catch ( MalformedURLException e )
        {
            String message = "Could not get file";
            logger.error( message + " " + dirname + "/" + filePath );
            throw new StorageException( message, e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void storeFile( String dirname, String subPath, MultipartFile file ) throws StorageException
    {
        Path full = getFullPath( dirname, subPath, " is not a subdirectory of user's folder" );
        Path destinationFile = full.resolve( file.getOriginalFilename() ).normalize();
        if ( !destinationFile.startsWith( full ) )
        {
            String message = "File " + file.getOriginalFilename() + " is not in directory " + subPath;
            logger.error( message );
            throw new StorageException( message );
        }
        try ( InputStream inputStream = file.getInputStream() )
        {
            Files.copy( inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING );
        }
        catch ( IOException e )
        {
            String message = "Could not save file";
            logger.error( message );
            throw new StorageException( message, e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createSubDir( String dirname, String subPath, String subDirName ) throws StorageException
    {
        subDirName = subDirName.trim();
        Path full = getFullPath( dirname, subPath, " is not a subdirectory of user's folder" );
        Path newDir = full.resolve( subDirName ).normalize();
        if ( !newDir.startsWith( full ) )
        {
            String message = "Directory " + subDirName + " is not in directory " + subPath;
            logger.error( message );
            throw new StorageException( message );
        }
        this.createDirForPath( newDir );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( String dirname, String pathToFile ) throws StorageException
    {
        Path full = getFullPath( dirname, pathToFile, " is not in directory" );
        if ( !Files.exists( full ) )
        {
            logger.error( "File " + full + " does not exists" );
            throw new StorageException( "No such file" );
        }
        try
        {
            if ( Files.isDirectory( full ) )
                FileSystemUtils.deleteRecursively( full );
            else
                Files.delete( full );
        }
        catch ( IOException e )
        {
            logger.error( "Could not delete file " + full.getFileName(), e );
            throw new StorageException( "Could not delete file", e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearFolder( String dir ) throws StorageException
    {
        Path full = getUserRoot( dir );
        try
        {
            Files.walkFileTree( full, new SimpleFileVisitor<>()
            {
                @Override
                public FileVisitResult visitFile( Path file, BasicFileAttributes attrs ) throws IOException
                {
                    Files.delete( file );
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory( Path dir, IOException exc ) throws IOException
                {
                    if ( !dir.equals( full ) )
                        Files.delete( dir );
                    return FileVisitResult.CONTINUE;
                }
            } );
        }
        catch ( IOException e )
        {
            String error_message = "Could not clear folder";
            logger.error( error_message, e );
            throw new StorageException( error_message, e );
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
        if ( !full.startsWith( relative ) )
        {
            logger.error( filePath + errorMessage );
            throw new StorageException( filePath + errorMessage );
        }
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
            final var message = "Could not create dir for " + dirPath;
            logger.error( message, e );
            throw new StorageException( message, e );
        }
    }

}
