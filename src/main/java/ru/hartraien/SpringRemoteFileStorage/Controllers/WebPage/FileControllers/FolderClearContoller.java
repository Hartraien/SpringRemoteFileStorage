package ru.hartraien.SpringRemoteFileStorage.Controllers.WebPage.FileControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.hartraien.SpringRemoteFileStorage.Entities.UserEntity;
import ru.hartraien.SpringRemoteFileStorage.Services.DirServicePackage.DirService;
import ru.hartraien.SpringRemoteFileStorage.Services.DirServicePackage.DirectoryException;
import ru.hartraien.SpringRemoteFileStorage.Services.StorageServicePackage.StorageException;
import ru.hartraien.SpringRemoteFileStorage.Services.StorageServicePackage.StorageService;
import ru.hartraien.SpringRemoteFileStorage.Services.UserServicePackage.UserService;

@Controller
@RequestMapping("/clear_folder")
public class FolderClearContoller extends AbstractFileController
{
    @Autowired
    public FolderClearContoller( UserService userService
            , DirService dirService
            , StorageService storageService )
    {
        super( userService, dirService, storageService, FolderClearContoller.class );
    }

    @PostMapping
    public String clearFolder( Authentication authentication, RedirectAttributes redirectrAttributes )
    {
        UserEntity user = getCurrentUser( authentication );

        try
        {
            getDirService().checkIfDirExistsOrThrow( user.getDir() );
            getStorageService().clearFolder( user.getDir().getDirname() );
        }
        catch ( DirectoryException e )
        {
            String dir_error = "Folder for usre does not exists";
            getLogger().error( dir_error, e );
            redirectrAttributes.addFlashAttribute( "clear_folder_error", dir_error );
        }
        catch ( StorageException e )
        {
            String storage_error = "Could not clear user's folder";
            getLogger().error( storage_error, e );
            redirectrAttributes.addFlashAttribute( "clear_folder_error", storage_error );
        }
        return "redirect:/userinfo";
    }
}
