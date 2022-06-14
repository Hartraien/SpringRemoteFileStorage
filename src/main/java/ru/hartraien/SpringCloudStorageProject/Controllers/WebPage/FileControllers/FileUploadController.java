package ru.hartraien.SpringCloudStorageProject.Controllers.WebPage.FileControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage.DirService;
import ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage.DirectoryException;
import ru.hartraien.SpringCloudStorageProject.Services.StorageServicePackage.StorageException;
import ru.hartraien.SpringCloudStorageProject.Services.StorageServicePackage.StorageService;
import ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage.UserService;

@Controller
@RequestMapping("/uploadpage")
public class FileUploadController extends AbstractFileController
{

    @Autowired
    public FileUploadController( UserService userRepository, DirService dirService, StorageService storageService )
    {
        super( userRepository, dirService, storageService, FileUploadController.class );
    }

    @PostMapping("")
    public String uploadFile( @RequestParam("file") MultipartFile file, @RequestParam("path") String path, Authentication authentication, RedirectAttributes redirectAttributes )
    {
        UserEntity user = getCurrentUser( authentication );
        try
        {
            getDirService().checkIfDirExistsOrThrow( user.getDir() );
            getStorageService().storeFile( user.getDir().getDirname(), path, file );
        }
        catch ( DirectoryException | StorageException e )
        {
            getLogger().warn( "Could not upload file " + file.getOriginalFilename(), e );
            redirectAttributes.addAttribute( "error", e.getMessage() );
        }
        return "redirect:/viewfiles/" + path;
    }
}
