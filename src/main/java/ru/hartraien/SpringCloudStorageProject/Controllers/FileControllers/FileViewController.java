package ru.hartraien.SpringCloudStorageProject.Controllers.FileControllers;


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
import ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage.DirectoryException;
import ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/viewfiles")
public class FileViewController extends AbstractFileController
{

    @Autowired
    public FileViewController( UserService userRepository, DirService dirService )
    {
        super( userRepository, dirService, FileViewController.class );
    }

    @GetMapping("/**")
    public String getPage( HttpServletRequest request, Authentication authentication, Model model )
    {
        String subPath = getSubPath( request );
        System.err.println( "SubPath = " + subPath );
        String backPath = getBackPath( subPath );
        UserEntity user = getCurrentUser( authentication );
        List<FileDTO> filesInDir;
        try
        {
            filesInDir = getDirService().getFilesInDir( user.getDir(), subPath );
        }
        catch ( DirectoryException e )
        {
            filesInDir = Collections.emptyList();
        }
        model.addAttribute( "backPath", backPath );
        model.addAttribute( "files", filesInDir );
        model.addAttribute( "path", subPath );
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


//    @PostMapping
//    public String uploadFile( @RequestParam("file") MultipartFile file, Authentication authentication )
//    {
//        dirService.uploadFile( file, getCurrentUser( authentication ) );
//        return "redirect:/uploadpage";
//    }

}
