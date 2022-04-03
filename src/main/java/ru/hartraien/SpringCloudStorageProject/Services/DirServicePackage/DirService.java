package ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.hartraien.SpringCloudStorageProject.Entities.DirectoryEntity;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;

import java.util.List;

public interface DirService
{
    List<String> getFilesByUser( UserEntity user );

    void uploadFile( MultipartFile file, UserEntity currentUser );

    DirectoryEntity generateNewDir();

    Resource loadAsResource( String filename, UserEntity user );
}
