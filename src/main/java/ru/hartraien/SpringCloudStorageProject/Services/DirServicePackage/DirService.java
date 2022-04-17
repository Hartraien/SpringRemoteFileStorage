package ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.hartraien.SpringCloudStorageProject.DTOs.FileDTO;
import ru.hartraien.SpringCloudStorageProject.Entities.DirectoryEntity;

import java.util.List;

public interface DirService
{
    DirectoryEntity generateNewDir();

    List<FileDTO> getFilesInDir( DirectoryEntity directory, String subPath ) throws NoSuchDirectoryException;

    Resource getFile( DirectoryEntity directory, String filePath ) throws NoSuchDirectoryException;

    void storeFile( DirectoryEntity directory, String path, MultipartFile file ) throws NoSuchDirectoryException;

    void createDir( DirectoryEntity directory, String path, String dirName ) throws NoSuchDirectoryException;

    boolean dirExists( DirectoryEntity directory);
}
