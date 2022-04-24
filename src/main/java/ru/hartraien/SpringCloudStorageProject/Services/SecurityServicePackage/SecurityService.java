package ru.hartraien.SpringCloudStorageProject.Services.SecurityServicePackage;

/**
 * Utility methods for spring security
 */
public interface SecurityService
{
    /**
     * Finds username of logged user
     *
     * @return return name of user
     */
    String findLoggedInUsername();

    /**
     * automatically logins user by username and password, used after registration
     *
     * @param username - username of user
     * @param password - password of user
     */
    void autologin( String username, String password );
}
