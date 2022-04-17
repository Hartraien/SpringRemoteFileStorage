package ru.hartraien.SpringCloudStorageProject.Controllers.FileControllers;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Repositories.UserRepository;
import ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage.DirService;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class AbstractFileController
{
    private final DirService dirService;
    private final UserRepository userRepository;
    private final String basePath;

    public AbstractFileController( UserRepository userRepository, DirService dirService, Class<? extends AbstractFileController> clazz )
    {
        this.userRepository = userRepository;
        this.dirService = dirService;
        this.basePath = getBasePath( clazz );
    }

    private String getBasePath( Class<? extends AbstractFileController> clazz )
    {
        final RequestMapping annotation = AnnotationUtils.findAnnotation( clazz, RequestMapping.class );
        if ( annotation != null )
            return annotation.path()[0] + "/";
        else
            return "";
    }


    protected UserEntity getCurrentUser( Authentication authentication )
    {
        return userRepository.findUserByUsername( authentication.getName() );
    }

    protected String getSubPath( HttpServletRequest request )
    {
        String[] parts = request.getRequestURL().toString().split( basePath, 2 );
        if ( parts.length == 2 )
        {
            System.err.println( parts[1] );
            return URLDecoder.decode( parts[1], StandardCharsets.UTF_8 );
        }
        else
            return "";
    }


    protected DirService getDirService()
    {
        return dirService;
    }
}
