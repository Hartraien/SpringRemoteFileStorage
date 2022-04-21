package ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hartraien.SpringCloudStorageProject.DTOs.FileDTO;
import ru.hartraien.SpringCloudStorageProject.Entities.DirectoryEntity;
import ru.hartraien.SpringCloudStorageProject.Repositories.DirRepository;
import ru.hartraien.SpringCloudStorageProject.Services.StorageServicePackage.StorageException;
import ru.hartraien.SpringCloudStorageProject.Services.StorageServicePackage.StorageService;
import ru.hartraien.SpringCloudStorageProject.Utility.RandomStringProducer;
import ru.hartraien.SpringCloudStorageProject.Utility.StringProducer;

import java.util.List;

@Service
public class DirServiceImpl implements DirService
{
    private final DirRepository dirRepository;
    private final StorageService storageService;
    private final int dirNameLength = 5;
    private final String mainDirExceptionMessage = "No main directory for current user";

    @Autowired
    public DirServiceImpl( DirRepository dirRepository, StorageService storageService )
    {
        this.dirRepository = dirRepository;
        this.storageService = storageService;
    }

    @Override
    @Transactional(readOnly = true)
    public DirectoryEntity generateNewDir() throws DirectoryException
    {
        StringProducer stringProducer = new RandomStringProducer();
        while ( true )
        {
            String name = stringProducer.getString( dirNameLength );
            if ( dirRepository.findByDirname( name ) == null )
            {
                DirectoryEntity directoryEntity = new DirectoryEntity();
                directoryEntity.setDirname( name );
                try
                {
                    storageService.createDir( directoryEntity.getDirname() );
                    return directoryEntity;
                }
                catch ( StorageException e )
                {
                    throw new DirectoryException( "Could not create directory ", e );
                }
            }
        }
    }

    @Override
    public List<FileDTO> getFilesInDir( DirectoryEntity directory, String subPath ) throws DirectoryException
    {
        if ( dirExists( directory ) )
        {
            try
            {
                return storageService.getAllFilesInDir( directory.getDirname(), subPath );
            }
            catch ( StorageException e )
            {
                throw new DirectoryException( "Could not get files in dir", e );
            }
        }
        else
            throw new DirectoryException( mainDirExceptionMessage );
    }

    @Override
    public Resource getFile( DirectoryEntity directory, String filePath ) throws DirectoryException
    {
        if ( dirExists( directory ) )
        {
            try
            {
                return storageService.getFile( directory.getDirname(), filePath );
            }
            catch ( StorageException e )
            {
                throw new DirectoryException( "Could not get file", e );
            }
        }
        else
            throw new DirectoryException( mainDirExceptionMessage );
    }

    @Override
    public void storeFile( DirectoryEntity directory, String path, MultipartFile file ) throws DirectoryException
    {
        if ( dirExists( directory ) )
        {
            try
            {
                storageService.storeFile( directory.getDirname(), path, file );
            }
            catch ( StorageException e )
            {
                throw new DirectoryException( "Could not store file", e );
            }
        }
        else
            throw new DirectoryException( mainDirExceptionMessage );
    }

    @Override
    public void createDir( DirectoryEntity directory, String path, String dirName ) throws DirectoryException
    {
        if ( dirExists( directory ) )
        {
            try
            {
                storageService.createSubDir( directory.getDirname(), path, dirName );
            }
            catch ( StorageException e )
            {
                throw new DirectoryException( "Could not get create subdirectory", e );
            }
        }
        else
            throw new DirectoryException( mainDirExceptionMessage );
    }


    @Override
    @Transactional(readOnly = true)
    public boolean dirExists( DirectoryEntity directory )
    {
        return dirRepository.findByDirname( directory.getDirname() ) != null;
    }

    @Override
    public void delete( DirectoryEntity dir, String pathToFile ) throws DirectoryException
    {
        if ( dirExists( dir ) )
        {
            try
            {
                storageService.delete( dir.getDirname(), pathToFile );
            }
            catch ( StorageException e )
            {
                throw new DirectoryException( "Could not delete", e );
            }
        }
        else
            throw new DirectoryException( mainDirExceptionMessage );
    }

}
