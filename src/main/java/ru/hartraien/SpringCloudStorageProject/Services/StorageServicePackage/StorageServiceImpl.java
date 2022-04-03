package ru.hartraien.SpringCloudStorageProject.Services.StorageServicePackage;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.hartraien.SpringCloudStorageProject.Entities.DirectoryEntity;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class StorageServiceImpl implements StorageService
{
    private final Path rootLocation;

    public StorageServiceImpl()
    {
        this.rootLocation = Path.of( "storage" );
        if ( !Files.exists( this.rootLocation ) )
            createDirForPath( this.rootLocation );
        else
            clearMainFolder();
    }

    private void clearMainFolder()
    {
        FileSystemUtils.deleteRecursively( rootLocation.toFile() );
    }

    @Override
    public void uploadFile( MultipartFile file, DirectoryEntity dir )
    {
        Path userRoot = getUserRoot( dir );
        try
        {
            if ( !file.isEmpty() )
            {
                Path destinationPath = userRoot.resolve( Paths.get( file.getOriginalFilename() ) )
                        .normalize().toAbsolutePath();
                if ( !destinationPath.getParent().equals( userRoot.toAbsolutePath() ) )
                    return;
                try ( InputStream inp = file.getInputStream() )
                {
                    Files.copy( inp, destinationPath, StandardCopyOption.REPLACE_EXISTING );
                }
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    private Path getUserRoot( DirectoryEntity dir )
    {
        return rootLocation.resolve( dir.getDirname() );
    }

    @Override
    public Stream<Path> getAllFilesFromDir( DirectoryEntity dir ) throws IOException
    {
        Path userRoot = getUserRoot( dir );
        return Files.walk( userRoot, 1 )
                .filter( path -> !path.equals( userRoot ) )
                .map( this.rootLocation::relativize );
    }

    @Override
    public void createDir( DirectoryEntity directory )
    {
        Path dirPath = this.getUserRoot( directory );
        createDirForPath( dirPath );
    }

    private void createDirForPath( Path dirPath )
    {
        try
        {
            Files.createDirectories( dirPath );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    @Override
    public Resource loadAsResource( String filename, DirectoryEntity dir )
    {
        Path file = getUserRoot( dir ).resolve( filename );
        try
        {
            return new UrlResource( file.toUri() );
        }
        catch ( MalformedURLException e )
        {
            e.printStackTrace();
        }
        return null;
    }
}
