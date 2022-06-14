package ru.hartraien.SpringCloudStorageProject.Controllers.WebPage.FileControllers;

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
@RequestMapping("/delete")
public class FileDeleteController extends AbstractFileController
{
    @Autowired
    public FileDeleteController( UserService userRepository, DirService dirService, StorageService storageService )
    {
        super( userRepository, dirService, storageService, FileDeleteController.class );
    }

    @PostMapping
    public String deleteFile( @RequestParam("pathToFile") String pathToFile,
                              @RequestParam("redirectPath") String path,
                              RedirectAttributes redirectAttributes,
                              Authentication authentication ) throws StorageException, DirectoryException
    {
        UserEntity user = getCurrentUser( authentication );
        try
        {
            getDirService().checkIfDirExistsOrThrow( user.getDir() );
            getStorageService().delete( user.getDir().getDirname(), pathToFile );
        }
        catch ( DirectoryException | StorageException e )
        {
            getLogger().warn( "Could not delete file = " + pathToFile, e );
            throw e;
        }
        return "redirect:/viewfiles/" + path;
    }
}
