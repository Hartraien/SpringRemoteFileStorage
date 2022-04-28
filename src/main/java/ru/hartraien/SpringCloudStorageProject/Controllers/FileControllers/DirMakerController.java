package ru.hartraien.SpringCloudStorageProject.Controllers.FileControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage.DirService;
import ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage.DirectoryException;
import ru.hartraien.SpringCloudStorageProject.Services.StorageServicePackage.StorageException;
import ru.hartraien.SpringCloudStorageProject.Services.StorageServicePackage.StorageService;
import ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage.UserService;

@Controller
@RequestMapping("/makedir")
public class DirMakerController extends AbstractFileController
{


    @Autowired
    public DirMakerController( UserService userRepository, DirService dirService, StorageService storageService )
    {
        super( userRepository, dirService, storageService, DirMakerController.class );
    }

    @PostMapping("")
    public String makeDir( @RequestParam("name") String dirName,
                           @RequestParam("path") String path,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes )
    {
        UserEntity user = getCurrentUser( authentication );
        try
        {
            getDirService().checkIfDirExistsOrThrow( user.getDir() );
            getStorageService().createSubDir( user.getDir().getDirname(), path, dirName );
        }
        catch ( DirectoryException | StorageException e )
        {
            getLogger().warn( "Could not create directory " + dirName, e );
            redirectAttributes.addAttribute( "error", e.getMessage() );
        }
        return "redirect:/viewfiles/" + path;
    }
}
