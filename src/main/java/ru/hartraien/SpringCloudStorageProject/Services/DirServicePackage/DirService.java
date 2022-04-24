package ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.hartraien.SpringCloudStorageProject.DTOs.FileDTO;
import ru.hartraien.SpringCloudStorageProject.Entities.DirectoryEntity;

import java.util.List;

/**
 * Encapsulates working with user directories
 */
public interface DirService
{
    /**
     * generates new directory with unique name
     * @return created directory
     * @throws DirectoryException - if could not create new dir
     */
    DirectoryEntity generateNewDir() throws DirectoryException;

    /**
     * Fetches all files in directory
     * @param directory - directory of a user
     * @param subPath - path relative to directory
     * @return List of FileDTOs
     * @throws DirectoryException - if could not get al files in dir
     */
    List<FileDTO> getFilesInDir( DirectoryEntity directory, String subPath ) throws DirectoryException;

    /**
     * Fetches file content by path
     * @param directory - directory of a user
     * @param filePath - relative path to file
     * @return file as Resource
     * @throws DirectoryException - if could not get file
     */
    Resource getFile( DirectoryEntity directory, String filePath ) throws DirectoryException;

    /**
     * Saves file
     * @param directory - directory of a user
     * @param path - relative directory path
     * @param file - file to save
     * @throws DirectoryException - if could not store new file
     */
    void storeFile( DirectoryEntity directory, String path, MultipartFile file ) throws DirectoryException;

    /**
     * Create new subdirectory
     * @param directory - directory of a user
     * @param path - relative path
     * @param dirName - name of new dir
     * @throws DirectoryException - if could not create new subdir
     */
    void createDir( DirectoryEntity directory, String path, String dirName ) throws DirectoryException;

    /**
     * checks if base directory with given name exists
     * @param directory - directory to check
     * @return true if exists and false otherwise
     */
    boolean dirExists( DirectoryEntity directory );

    /**
     * Deletes file of subdirectory
     * @param directory - directory of a user
     * @param pathToFile - relative path
     * @throws DirectoryException - if could not delete file or directory
     */
    void delete( DirectoryEntity directory, String pathToFile ) throws DirectoryException;
}
