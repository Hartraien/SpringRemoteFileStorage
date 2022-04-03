package ru.hartraien.SpringCloudStorageProject.Services.SecurityServicePackage;

public interface SecurityService
{
    String findLoggedInUsername();

    void autologin( String username, String password );
}
