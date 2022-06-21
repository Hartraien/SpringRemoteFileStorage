package ru.hartraien.SpringRemoteFileStorage.Controllers.WebPage.FileControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.hartraien.SpringRemoteFileStorage.Entities.UserEntity;
import ru.hartraien.SpringRemoteFileStorage.Services.DirServicePackage.DirService;
import ru.hartraien.SpringRemoteFileStorage.Services.DirServicePackage.DirectoryException;
import ru.hartraien.SpringRemoteFileStorage.Services.StorageServicePackage.StorageException;
import ru.hartraien.SpringRemoteFileStorage.Services.StorageServicePackage.StorageService;
import ru.hartraien.SpringRemoteFileStorage.Services.UserServicePackage.UserService;

import javax.validation.constraints.NotBlank;

@Controller
@RequestMapping("/makedir")
@Validated
public class DirMakerController extends AbstractFileController
{


    @Autowired
    public DirMakerController( UserService userRepository, DirService dirService, StorageService storageService )
    {
        super( userRepository, dirService, storageService, DirMakerController.class );
    }

    @PostMapping("")
    public String makeDir( @RequestParam("name") @NotBlank String dirName,
                           @RequestParam("path") String path,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes ) throws StorageException, DirectoryException
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
            throw e;
        }
        return "redirect:/viewfiles/" + path;
    }
}
