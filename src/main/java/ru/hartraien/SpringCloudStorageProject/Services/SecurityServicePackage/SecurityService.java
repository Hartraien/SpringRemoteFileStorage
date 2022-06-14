package ru.hartraien.SpringCloudStorageProject.Services.SecurityServicePackage;

/**
 * Utility methods for spring security
 */
public interface SecurityService
{

    /**
     * automatically logins user by username and password, used after registration
     *
     * @param username - username of user
     * @param password - password of user
     */
    void autologin( String username, String password );
}
