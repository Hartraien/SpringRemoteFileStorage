package ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.hartraien.SpringCloudStorageProject.DTOs.FileDTO;
import ru.hartraien.SpringCloudStorageProject.Entities.DirectoryEntity;

import java.util.List;

public interface DirService
{
    DirectoryEntity generateNewDir() throws DirectoryException;

    List<FileDTO> getFilesInDir( DirectoryEntity directory, String subPath ) throws DirectoryException;

    Resource getFile( DirectoryEntity directory, String filePath ) throws DirectoryException;

    void storeFile( DirectoryEntity directory, String path, MultipartFile file ) throws DirectoryException;

    void createDir( DirectoryEntity directory, String path, String dirName ) throws DirectoryException;

    boolean dirExists( DirectoryEntity directory);

    void delete( DirectoryEntity dir, String pathToFile ) throws DirectoryException;
}
