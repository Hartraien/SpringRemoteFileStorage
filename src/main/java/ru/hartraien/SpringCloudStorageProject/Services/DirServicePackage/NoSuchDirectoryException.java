package ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage;

public class NoSuchDirectoryException extends Exception
{
    public NoSuchDirectoryException()
    {
    }

    public NoSuchDirectoryException( String message )
    {
        super( message );
    }

    public NoSuchDirectoryException( String message, Throwable cause )
    {
        super( message, cause );
    }
}
