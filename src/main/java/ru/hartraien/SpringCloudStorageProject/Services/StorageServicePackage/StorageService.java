package ru.hartraien.SpringCloudStorageProject.Services.StorageServicePackage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.hartraien.SpringCloudStorageProject.DTOs.FileDTO;

import java.util.List;

public interface StorageService
{

    void createDir( String directory );

    List<FileDTO> getAllFilesInDir( String dirname, String subPath ) throws StorageException;

    Resource getFile( String dirname, String filePath ) throws StorageException;

    void storeFile( String dirname, String path, MultipartFile file ) throws StorageException;

    void createSubDir( String dirname, String path, String dirName ) throws StorageException;
}
