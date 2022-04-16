package ru.hartraien.SpringCloudStorageProject.Services.StorageServicePackage;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.hartraien.SpringCloudStorageProject.DTOs.FileDTO;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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

    private Path getUserRoot( String dir )
    {
        return rootLocation.resolve( dir ).normalize();
    }

    @Override
    public void createDir( String directory )
    {
        Path dirPath = this.getUserRoot( directory );
        createDirForPath( dirPath );
    }

    @Override
    public List<FileDTO> getAllFilesInDir( String dirname, String subPath )
    {
        Path relative = getUserRoot( dirname );
        Path full = relative.resolve( subPath ).normalize();
        if ( full.startsWith( relative ) )
        {
            try
            {
                try ( var stream = getFilesInPath( full ) )
                {
                    return stream.map( path -> new FileDTO( path, relative ) )
                            .collect( Collectors.toList() );
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    @Override
    public Resource getFile( String dirname, String filePath )
    {
        Path relative = getUserRoot( dirname );
        Path full = relative.resolve( filePath ).normalize();
        if ( full.startsWith( relative ) )
        {
            try
            {
                Resource resource = new UrlResource( full.toUri() );
                if ( resource.exists() || resource.isReadable() )
                    return resource;
            }
            catch ( MalformedURLException e )
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void storeFile( String dirname, String subPath, MultipartFile file )
    {
        Path relative = getUserRoot( dirname );
        Path full = relative.resolve( subPath ).normalize();
        if ( full.startsWith( relative ) )
        {
            if ( file.isEmpty() )
                return;
            Path destinationFile = full.resolve( file.getOriginalFilename() ).normalize();
            if ( !destinationFile.startsWith( full ) )
                return;
            try ( InputStream inputStream = file.getInputStream() )
            {
                Files.copy( inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING );
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
        }
    }

    private Stream<Path> getFilesInPath( Path full ) throws IOException
    {
        return Files.walk( full, 1 )
                .filter( path -> !full.equals( path ) );
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

}
