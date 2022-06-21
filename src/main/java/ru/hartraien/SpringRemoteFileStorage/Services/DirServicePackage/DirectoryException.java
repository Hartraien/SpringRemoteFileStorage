package ru.hartraien.SpringRemoteFileStorage.Services.DirServicePackage;

public class DirectoryException extends Exception
{
    public DirectoryException()
    {
    }

    public DirectoryException( String message )
    {
        super( message );
    }

    public DirectoryException( String message, Throwable cause )
    {
        super( message, cause );
    }
}
