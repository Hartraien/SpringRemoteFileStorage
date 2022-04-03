package ru.hartraien.SpringCloudStorageProject.Services.StorageServicePackage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.hartraien.SpringCloudStorageProject.Entities.DirectoryEntity;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService
{
    void uploadFile( MultipartFile file, DirectoryEntity dir );

    Stream<Path> getAllFilesFromDir( DirectoryEntity dir ) throws IOException;

    void createDir( DirectoryEntity directory );

    Resource loadAsResource( String filename, DirectoryEntity dir );
}
