package ru.hartraien.SpringCloudStorageProject.Services.StorageServicePackage;

import ru.hartraien.SpringCloudStorageProject.DTOs.FileDTO;

import java.util.List;

public interface StorageService
{

    void createDir( String directory );

    List<FileDTO> getAllFilesInDir( String dirname, String subPath );
}
