package ru.hartraien.SpringCloudStorageProject.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.hartraien.SpringCloudStorageProject.DTOs.FileDTO;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Repositories.UserRepository;
import ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage.DirService;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/uploadpage")
public class FileViewController
{
    private final String basePath = "/uploadpage/";
    private final UserRepository userRepository;
    private final DirService dirService;

    @Autowired
    public FileViewController( UserRepository userRepository, DirService dirService )
    {
        this.userRepository = userRepository;
        this.dirService = dirService;
    }

    @GetMapping("/**")
    public String getPage( HttpServletRequest request, Authentication authentication, Model model )
    {
        String subPath = getSubPath( request );
        System.err.println( "SubPath = " + subPath );
        String backPath = getBackPath( subPath );
        UserEntity user = getCurrentUser( authentication );
        List<FileDTO> filesInDir = dirService.getFilesInDir( user.getDir(), subPath );
        model.addAttribute( "backPath", backPath );
        model.addAttribute( "files", filesInDir );
        return "uploadpage";
    }

    private String getBackPath( String subPath )
    {
        if ( "".equals( subPath ) )
            return null;
        else
        {
            int index = subPath.lastIndexOf( "/" );
            if ( index == -1 )
                return "";
            else
                return subPath.substring( 0, index );
        }
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

//    @GetMapping("/{filename:.+}")
//    public ResponseEntity<Resource> getFile( @PathVariable String filename, Authentication authentication )
//    {
//        UserEntity user = getCurrentUser( authentication );
//        Resource file = dirService.loadAsResource( filename, user );
//        ContentDisposition contentDisposition = ContentDisposition.builder( "attachment" )
//                .filename( filename, StandardCharsets.UTF_8 )
//                .build();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentDisposition( contentDisposition );
//        return ResponseEntity.ok()
//                .headers( headers )
//                .body( file );
//    }

    private UserEntity getCurrentUser( Authentication authentication )
    {
        return userRepository.findUserByUsername( authentication.getName() );
    }


//    @PostMapping
//    public String uploadFile( @RequestParam("file") MultipartFile file, Authentication authentication )
//    {
//        dirService.uploadFile( file, getCurrentUser( authentication ) );
//        return "redirect:/uploadpage";
//    }

}
