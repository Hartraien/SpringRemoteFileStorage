package ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public List<FileDTO> getFilesInDir( DirectoryEntity directory, String subPath )
    {
        return storageService.getAllFilesInDir(directory.getDirname(), subPath);
    }

}
