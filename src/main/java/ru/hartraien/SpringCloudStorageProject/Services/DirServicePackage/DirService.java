package ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.hartraien.SpringCloudStorageProject.DTOs.FileDTO;
import ru.hartraien.SpringCloudStorageProject.Entities.DirectoryEntity;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;

import java.util.List;

public interface DirService
{
    DirectoryEntity generateNewDir();

    List<FileDTO> getFilesInDir( DirectoryEntity directory, String subPath );

    Resource getFile( UserEntity user, String filePath );

    void storeFile( UserEntity user, String path, MultipartFile file );
}
