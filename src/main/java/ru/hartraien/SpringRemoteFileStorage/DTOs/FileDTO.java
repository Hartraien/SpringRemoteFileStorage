package ru.hartraien.SpringRemoteFileStorage.DTOs;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileDTO
{
    private final String filename;
    private final boolean isDirectory;
    private final String relPath;


    public FileDTO( Path path, Path relativePath )
    {
        filename = path.getFileName().toString();
        isDirectory = Files.isDirectory( path );
        relPath = relativePath.relativize( path ).toString().replace( "\\", "/" );
    }

    public String getFileName()
    {
        return filename;
    }

    public boolean isDirectory()
    {
        return isDirectory;
    }

    public String getRelPath()
    {
        return relPath;
    }
}
