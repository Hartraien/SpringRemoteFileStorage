package ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hartraien.SpringCloudStorageProject.DTOs.FileDTO;
import ru.hartraien.SpringCloudStorageProject.Entities.DirectoryEntity;
import ru.hartraien.SpringCloudStorageProject.Init.RandomStringProducer;
import ru.hartraien.SpringCloudStorageProject.Init.RandomStringProducerImpl;
import ru.hartraien.SpringCloudStorageProject.Repositories.DirRepository;
import ru.hartraien.SpringCloudStorageProject.Services.StorageServicePackage.StorageService;

import java.util.List;

@Service
public class DirServiceImpl implements DirService
{
    private final DirRepository dirRepository;
    private final StorageService storageService;
    private final int dirNameLength = 5;

    @Autowired
    public DirServiceImpl( DirRepository dirRepository, StorageService storageService )
    {
        this.dirRepository = dirRepository;
        this.storageService = storageService;
    }

    @Override
    public DirectoryEntity generateNewDir()
    {
        RandomStringProducer randomStringProducer = new RandomStringProducerImpl();
        while ( true )
        {
            String name = randomStringProducer.getString( dirNameLength );
            if ( dirRepository.findByDirname( name ) == null )
            {
                DirectoryEntity directoryEntity = new DirectoryEntity();
                directoryEntity.setDirname( name );
                storageService.createDir( directoryEntity.getDirname() );
                return directoryEntity;
            }
        }
    }

    @Override
    public List<FileDTO> getFilesInDir( DirectoryEntity directory, String subPath ) throws NoSuchDirectoryException
    {
        if ( dirExists( directory ) )
            return storageService.getAllFilesInDir( directory.getDirname(), subPath );
        else
            throw new NoSuchDirectoryException( "No such directory" );
    }

    @Override
    public Resource getFile( DirectoryEntity directory, String filePath ) throws NoSuchDirectoryException
    {
        if ( dirExists( directory ) )
            return storageService.getFile( directory.getDirname(), filePath );
        else
            throw new NoSuchDirectoryException( "No such directory" );
    }

    @Override
    public void storeFile( DirectoryEntity directory, String path, MultipartFile file ) throws NoSuchDirectoryException
    {
        if ( dirExists( directory ) )
            storageService.storeFile( directory.getDirname(), path, file );
        else
            throw new NoSuchDirectoryException( "No such directory" );
    }

    @Override
    public void createDir( DirectoryEntity directory, String path, String dirName ) throws NoSuchDirectoryException
    {
        if ( dirExists( directory ) )
            storageService.createSubDir( directory.getDirname(), path, dirName );
        else
            throw new NoSuchDirectoryException( "No such directory" );
    }


    @Override
    public boolean dirExists( DirectoryEntity directory )
    {
        return dirRepository.findByDirname( directory.getDirname() ) != null;
    }

}
