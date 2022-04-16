package ru.hartraien.SpringCloudStorageProject.Controllers.FileControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Repositories.UserRepository;
import ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage.DirService;

@Controller
@RequestMapping("/uploadpage")
public class FileUploadController extends FileContollerAbstract
{
    private final DirService dirService;

    @Autowired
    public FileUploadController( UserRepository userRepository, DirService dirService )
    {
        super( userRepository, FileUploadController.class );
        this.dirService = dirService;
    }

    @PostMapping("")
    public String uploadFile( @RequestParam("file") MultipartFile file, @RequestParam("path") String path, Authentication authentication )
    {
        UserEntity user = getCurrentUser( authentication );
        dirService.storeFile( user, path, file );
        return "redirect:/viewfiles/" + path;
    }
}
