package ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hartraien.SpringCloudStorageProject.Entities.DirectoryEntity;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Init.RandomStringProducer;
import ru.hartraien.SpringCloudStorageProject.Init.RandomStringProducerImpl;
import ru.hartraien.SpringCloudStorageProject.Repositories.DirRepository;
import ru.hartraien.SpringCloudStorageProject.Services.StorageServicePackage.StorageService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DirServiceImpl implements DirService
{
    private final DirRepository dirRepository;
    private final StorageService storageService;

    @Autowired
    public DirServiceImpl( DirRepository dirRepository, StorageService storageService )
    {
        this.dirRepository = dirRepository;
        this.storageService = storageService;
    }

    @Override
    public List<String> getFilesByUser( UserEntity user )
    {
        try ( var files = storageService.getAllFilesFromDir( user.getDir() ) )
        {
            return files
                    .map( path -> path.getFileName().toString() )
                    .collect( Collectors.toList() );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void uploadFile( MultipartFile file, UserEntity currentUser )
    {
        storageService.uploadFile( file, currentUser.getDir() );
    }

    @Override
    public DirectoryEntity generateNewDir()
    {
        RandomStringProducer randomStringProducer = new RandomStringProducerImpl();
        while ( true )
        {
            String name = randomStringProducer.getString( 5 );
            if ( dirRepository.findByDirname( name ) == null )
            {
                DirectoryEntity directoryEntity = new DirectoryEntity();
                directoryEntity.setDirname( name );
                storageService.createDir( directoryEntity );
                return directoryEntity;
            }
        }
    }

    @Override
    public Resource loadAsResource( String filename, UserEntity user )
    {
        return storageService.loadAsResource( filename, user.getDir() );
    }
}
