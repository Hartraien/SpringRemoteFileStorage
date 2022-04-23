package ru.hartraien.SpringCloudStorageProject.Controllers.UserControllers;

import org.springframework.security.core.Authentication;

public abstract class AbstractUserController
{
    protected boolean isAuthenticated( Authentication authentication )
    {
        return authentication != null && authentication.isAuthenticated();
    }
}
