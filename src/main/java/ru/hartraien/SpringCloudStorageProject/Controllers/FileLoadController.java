package ru.hartraien.SpringCloudStorageProject.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Repositories.UserRepository;
import ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage.DirService;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/download")
public class FileLoadController
{
    private final UserRepository userRepository;
    private final DirService dirService;
    private final String basePath = "/download/";


    @Autowired
    public FileLoadController( UserRepository userRepository, DirService dirService )
    {
        this.userRepository = userRepository;
        this.dirService = dirService;
    }

    @GetMapping("/**")
    @ResponseBody
    public ResponseEntity<Resource> getFile( HttpServletRequest request, Authentication authentication )
    {
        UserEntity user = getCurrentUser( authentication );
        String filePath = getSubPath( request );
        Resource file = dirService.getFile( user, filePath );
        ContentDisposition contentDisposition = ContentDisposition.builder( "attachment" )
                .filename( file != null ? file.getFilename() : "", StandardCharsets.UTF_8 )
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition( contentDisposition );
        return ResponseEntity.ok()
                .headers( headers )
                .body( file );
    }

    private UserEntity getCurrentUser( Authentication authentication )
    {
        return userRepository.findUserByUsername( authentication.getName() );
    }

    private String getSubPath( HttpServletRequest request )
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
