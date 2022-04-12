package ru.hartraien.SpringCloudStorageProject.Services.StorageServicePackage;

import org.springframework.core.io.Resource;
import ru.hartraien.SpringCloudStorageProject.DTOs.FileDTO;

import java.util.List;

public interface StorageService
{

    void createDir( String directory );

    List<FileDTO> getAllFilesInDir( String dirname, String subPath );

    Resource getFile( String dirname, String filePath );
}
