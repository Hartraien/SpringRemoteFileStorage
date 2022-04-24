package ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hartraien.SpringCloudStorageProject.Entities.DirectoryEntity;
import ru.hartraien.SpringCloudStorageProject.Repositories.DirRepository;
import ru.hartraien.SpringCloudStorageProject.Utility.RandomStringProducer;
import ru.hartraien.SpringCloudStorageProject.Utility.StringProducer;

/**
 * {@inheritDoc}
 */
@Service
@Transactional(readOnly = true)
public class DirServiceImpl implements DirService
{
    private final DirRepository dirRepository;

    private final Logger logger;

    private final int dirNameLength = 5;

    @Autowired
    public DirServiceImpl( DirRepository dirRepository )
    {
        this.dirRepository = dirRepository;
        logger = LoggerFactory.getLogger( DirServiceImpl.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DirectoryEntity generateNewDir()
    {
        StringProducer stringProducer = new RandomStringProducer();
        while ( true )
        {
            String name = stringProducer.getString( dirNameLength );
            if ( dirRepository.findByDirname( name ) == null )
            {
                DirectoryEntity directoryEntity = new DirectoryEntity();
                directoryEntity.setDirname( name );
                return directoryEntity;
            }
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
    public void checkIfDirExistsOrThrow( DirectoryEntity directory ) throws DirectoryException
    {
        if ( !dirExists( directory ) )
        {
            String mainDirExceptionMessage = "No main directory with name";
            final var message = mainDirExceptionMessage + " " + directory.getDirname();
            logger.error( message );
            throw new DirectoryException( message );
        }
    }

}
