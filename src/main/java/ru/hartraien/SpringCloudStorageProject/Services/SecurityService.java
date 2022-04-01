package ru.hartraien.SpringCloudStorageProject.Services;

public interface SecurityService
{
    String findLoggedInUsername();

    void autologin( String username, String password );
}
