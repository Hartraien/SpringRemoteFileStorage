package ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage;

import ru.hartraien.SpringCloudStorageProject.DTOs.FileDTO;
import ru.hartraien.SpringCloudStorageProject.Entities.DirectoryEntity;

import java.util.List;

public interface DirService
{
    DirectoryEntity generateNewDir();

    List<FileDTO> getFilesInDir( DirectoryEntity directory, String subPath );
}
