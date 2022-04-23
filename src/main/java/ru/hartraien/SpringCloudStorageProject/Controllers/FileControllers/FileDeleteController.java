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
import ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage.UserService;

@Controller
@RequestMapping("/delete")
public class FileDeleteController extends AbstractFileController
{
    @Autowired
    public FileDeleteController( UserService userRepository, DirService dirService )
    {
        super( userRepository, dirService, FileDeleteController.class );
    }

    @PostMapping
    public String deleteFile( @RequestParam("pathToFile") String pathToFile,
                              @RequestParam("redirectPath") String path,
                              RedirectAttributes redirectAttributes,
                              Authentication authentication )
    {
        UserEntity user = getCurrentUser( authentication );
        try
        {
            getDirService().delete( user.getDir(), pathToFile );
        }
        catch ( DirectoryException e )
        {
            redirectAttributes.addAttribute( "error", e.getMessage() );
        }
        return "redirect:/viewfiles/" + path;
    }
}
