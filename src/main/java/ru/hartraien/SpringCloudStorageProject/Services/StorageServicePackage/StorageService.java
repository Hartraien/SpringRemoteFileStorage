package ru.hartraien.SpringCloudStorageProject.Services.StorageServicePackage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.hartraien.SpringCloudStorageProject.DTOs.FileDTO;

import java.util.List;

/**
 * Provides methods to work with files
 */
public interface StorageService
{

    /**
     * Create directory with given name
     * Used when creating users
     *
     * @param directory - name of directory to create
     * @throws StorageException - if could not create a directory
     */
    void createDir( String directory ) throws StorageException;

    /**
     * List all files in directory
     *
     * @param dirname - name of user directory
     * @param subPath - path inside user directory
     * @return list of FileDTOs containing info about files
     * @throws StorageException - if could not get all files
     */
    List<FileDTO> getAllFilesInDir( String dirname, String subPath ) throws StorageException;

    /**
     * Get file from user directory
     *
     * @param dirname  - user directory
     * @param filePath - path to file relative to user directory
     * @return File as a Resource
     * @throws StorageException - if could not get file
     */
    Resource getFile( String dirname, String filePath ) throws StorageException;

    /**
     * Saves file
     *
     * @param dirname - name of user directory
     * @param path    - path to subdirectory relative to dirname
     * @param file    - file to store
     * @throws StorageException - if could not store file
     */
    void storeFile( String dirname, String path, MultipartFile file ) throws StorageException;

    /**
     * Create subdirectory
     *
     * @param dirname - name of users directory
     * @param path    - path where to create new directory, relative to dirname
     * @param dirName - name of new directory
     * @throws StorageException - if could not create subdirectory
     */
    void createSubDir( String dirname, String path, String dirName ) throws StorageException;

    /**
     * Delete file or directory by path
     *
     * @param dirname    - name of user directory
     * @param pathToFile - path to file relative to dirname
     * @throws StorageException - if could not delete given file
     */
    void delete( String dirname, String pathToFile ) throws StorageException;
}
