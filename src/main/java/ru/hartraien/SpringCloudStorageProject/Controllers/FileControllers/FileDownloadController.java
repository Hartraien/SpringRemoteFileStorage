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
import ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage.DirService;
import ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage.DirectoryException;
import ru.hartraien.SpringCloudStorageProject.Services.StorageServicePackage.StorageException;
import ru.hartraien.SpringCloudStorageProject.Services.StorageServicePackage.StorageService;
import ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage.UserService;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/download")
public class FileDownloadController extends AbstractFileController
{

    @Autowired
    public FileDownloadController( UserService userRepository, DirService dirService, StorageService storageService )
    {
        super( userRepository, dirService, storageService, FileDownloadController.class );
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
            getDirService().checkIfDirExistsOrThrow( user.getDir() );
            file = getStorageService().getFile( user.getDir().getDirname(), filePath );
            ContentDisposition contentDisposition = ContentDisposition.builder( "attachment" )
                    .filename( URLDecoder.decode( file.getFilename(), StandardCharsets.UTF_8 ), StandardCharsets.UTF_8 )
                    .build();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition( contentDisposition );
            return ResponseEntity.ok()
                    .headers( headers )
                    .body( file );
        }
        catch ( DirectoryException | StorageException e )
        {
            getLogger().warn( "Could not get file + " + filePath, e );
            return ResponseEntity.notFound().build();
        }
    }

}
