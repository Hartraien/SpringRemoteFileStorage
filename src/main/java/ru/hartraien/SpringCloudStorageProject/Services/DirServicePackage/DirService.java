package ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage;

import ru.hartraien.SpringCloudStorageProject.Entities.DirectoryEntity;

/**
 * Encapsulates working with user directories
 */
public interface DirService
{
    /**
     * generates new directory with unique name
     *
     * @return created directory
     * @throws DirectoryException - if could not create new dir
     */
    DirectoryEntity generateNewDir();

    /**
     * checks if base directory with given name exists
     *
     * @param directory - directory to check
     * @return true if exists and false otherwise
     */
    boolean dirExists( DirectoryEntity directory );

    /**
     * Checks if directory exists or throws DirectoryException
     *
     * @param directory - directory to check
     * @throws DirectoryException thrown if directory does not exists
     */
    void checkIfDirExistsOrThrow( DirectoryEntity directory ) throws DirectoryException;
}
