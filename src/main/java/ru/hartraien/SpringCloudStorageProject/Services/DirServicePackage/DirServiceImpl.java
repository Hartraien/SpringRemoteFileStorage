package ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * {@inheritDoc}
 */
@Service
@Transactional(readOnly = true)
public class DirServiceImpl implements DirService
{
    private final DirRepository dirRepository;
    private final StorageService storageService;

    private final Logger logger;

    private final int dirNameLength = 5;

    @Autowired
    public DirServiceImpl( DirRepository dirRepository, StorageService storageService )
    {
        this.dirRepository = dirRepository;
        this.storageService = storageService;
        logger = LoggerFactory.getLogger( DirServiceImpl.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
                    final var error_message = "Could not create directory ";
                    logger.error( error_message, e );
                    throw new DirectoryException( error_message, e );
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FileDTO> getFilesInDir( DirectoryEntity directory, String subPath ) throws DirectoryException
    {
        checkIfDirExistsOrThrow( directory );
        try
        {
            return storageService.getAllFilesInDir( directory.getDirname(), subPath );
        }
        catch ( StorageException e )
        {
            final var message = "Could not get files in dir";
            logger.error( message, e );
            throw new DirectoryException( message, e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Resource getFile( DirectoryEntity directory, String filePath ) throws DirectoryException
    {
        checkIfDirExistsOrThrow( directory );
        try
        {
            return storageService.getFile( directory.getDirname(), filePath );
        }
        catch ( StorageException e )
        {
            final var message = "Could not get file " + filePath;
            logger.error( message, e );
            throw new DirectoryException( message, e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void storeFile( DirectoryEntity directory, String path, MultipartFile file ) throws DirectoryException
    {
        checkIfDirExistsOrThrow( directory );
        try
        {
            storageService.storeFile( directory.getDirname(), path, file );
        }
        catch ( StorageException e )
        {
            final var message = "Could not store file " + file.getOriginalFilename();
            logger.error( message, e );
            throw new DirectoryException( message, e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createDir( DirectoryEntity directory, String path, String dirName ) throws DirectoryException
    {
        checkIfDirExistsOrThrow( directory );
        try
        {
            storageService.createSubDir( directory.getDirname(), path, dirName );
        }
        catch ( StorageException e )
        {
            final var message = "Could not get create subdirectory " + dirName;
            logger.error( message, e );
            throw new DirectoryException( message, e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean dirExists( DirectoryEntity directory )
    {
        return dirRepository.findByDirname( directory.getDirname() ) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( DirectoryEntity directory, String pathToFile ) throws DirectoryException
    {
        checkIfDirExistsOrThrow( directory );
        try
        {
            storageService.delete( directory.getDirname(), pathToFile );
        }
        catch ( StorageException e )
        {
            final var message = "Could not delete " + pathToFile;
            logger.error( message, e );
            throw new DirectoryException( message, e );
        }
    }

    private void checkIfDirExistsOrThrow( DirectoryEntity directory ) throws DirectoryException
    {
        if ( !dirExists( directory ) )
        {
            String mainDirExceptionMessage = "No main directory for current user";
            final var message = mainDirExceptionMessage + " " + directory.getDirname();
            logger.error( message );
            throw new DirectoryException( message );
        }
    }

}
