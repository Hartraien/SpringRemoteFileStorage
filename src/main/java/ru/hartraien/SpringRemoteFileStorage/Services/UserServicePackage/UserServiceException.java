package ru.hartraien.SpringRemoteFileStorage.Services.UserServicePackage;

public class UserServiceException extends Exception
{
    public UserServiceException()
    {
    }

    public UserServiceException( String message )
    {
        super( message );
    }

    public UserServiceException( String message, Throwable cause )
    {
        super( message, cause );
    }
}
