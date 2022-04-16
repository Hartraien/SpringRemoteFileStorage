package ru.hartraien.SpringCloudStorageProject.Controllers.FileControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Repositories.UserRepository;
import ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage.DirService;

@Controller
@RequestMapping("/makedir")
public class DirMakerController extends AbstractFileController
{
    private final DirService dirService;

    @Autowired
    public DirMakerController( UserRepository userRepository, DirService dirService )
    {
        super( userRepository, DirMakerController.class );
        this.dirService = dirService;
    }

    @PostMapping("")
    public String makeDir( @RequestParam("name") String dirName,
                           @RequestParam("path") String path,
                           Authentication authentication )
    {
        UserEntity user = getCurrentUser( authentication );
        dirService.createDir( user, path, dirName );
        return "redirect:/viewfiles/" + path;
    }
}
