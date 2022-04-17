package ru.hartraien.SpringCloudStorageProject.Controllers.FileControllers;

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
import ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage.DirectoryException;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/download")
public class FileDownloadController extends AbstractFileController
{
    private final DirService dirService;


    @Autowired
    public FileDownloadController( UserRepository userRepository, DirService dirService )
    {
        super( userRepository, FileDownloadController.class );
        this.dirService = dirService;
    }

    @GetMapping("/**")
    @ResponseBody
    public ResponseEntity<Resource> getFile( HttpServletRequest request, Authentication authentication )
    {
        UserEntity user = getCurrentUser( authentication );
        String filePath = getSubPath( request );
        Resource file;
        try
        {
            file = dirService.getFile( user.getDir(), filePath );
            ContentDisposition contentDisposition = ContentDisposition.builder( "attachment" )
                    .filename( file.getFilename(), StandardCharsets.UTF_8 )
                    .build();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition( contentDisposition );
            return ResponseEntity.ok()
                    .headers( headers )
                    .body( file );
        }
        catch ( DirectoryException e )
        {
            return ResponseEntity.notFound().build();
        }
    }

}
