package ru.hartraien.SpringCloudStorageProject.Controllers.FileControllers;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Repositories.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class AbstractFileController
{
    private final UserRepository userRepository;
    private final String basePath;

    public AbstractFileController( UserRepository userRepository, Class<? extends AbstractFileController> clazz )
    {
        this.userRepository = userRepository;
        this.basePath = AnnotationUtils.findAnnotation( clazz, RequestMapping.class ).path()[0] + "/";
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
}
