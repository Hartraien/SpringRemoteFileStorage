package ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hartraien.SpringCloudStorageProject.DTOs.FileDTO;
import ru.hartraien.SpringCloudStorageProject.Entities.DirectoryEntity;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
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

    @Override
    public Resource getFile( UserEntity user, String filePath )
    {
        return storageService.getFile(user.getDir().getDirname(), filePath);
    }

    @Override
    public void storeFile( UserEntity user, String path, MultipartFile file )
    {
        storageService.storeFile(user.getDir().getDirname(), path, file);
    }

}
