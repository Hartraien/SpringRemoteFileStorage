package ru.hartraien.SpringCloudStorageProject.Controllers.FileControllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage.DirService;
import ru.hartraien.SpringCloudStorageProject.Services.StorageServicePackage.StorageService;
import ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage.UserService;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class AbstractFileController
{
    private final DirService dirService;
    private final UserService userService;
    private final StorageService storageService;
    private final String basePath;

    private final Logger logger;

    public AbstractFileController( UserService userService, DirService dirService, StorageService storageService, Class<? extends AbstractFileController> clazz )
    {
        this.userService = userService;
        this.dirService = dirService;
        this.storageService = storageService;
        this.basePath = getBasePath( clazz );
        logger = LoggerFactory.getLogger( clazz );
    }

    protected StorageService getStorageService()
    {
        return storageService;
    }

    protected Logger getLogger()
    {
        return logger;
    }

    protected UserEntity getCurrentUser( Authentication authentication )
    {
        return userService.findByUsername( authentication.getName() );
    }

    protected String getSubPath( HttpServletRequest request )
    {
        String[] parts = request.getRequestURL().toString().split( basePath, 2 );
        if ( parts.length == 2 )
        {
            return URLDecoder.decode( parts[1], StandardCharsets.UTF_8 );
        }
        else
            return "";
    }

    protected DirService getDirService()
    {
        return dirService;
    }


    private String getBasePath( Class<? extends AbstractFileController> clazz )
    {
        final RequestMapping annotation = AnnotationUtils.findAnnotation( clazz, RequestMapping.class );
        if ( annotation != null )
            return annotation.path()[0] + "/";
        else
            return "";
    }
}
