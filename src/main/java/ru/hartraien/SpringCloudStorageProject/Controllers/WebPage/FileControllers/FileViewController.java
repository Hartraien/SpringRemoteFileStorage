package ru.hartraien.SpringCloudStorageProject.Controllers.WebPage.FileControllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.hartraien.SpringCloudStorageProject.DTOs.FileDTO;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage.DirService;
import ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage.DirectoryException;
import ru.hartraien.SpringCloudStorageProject.Services.StorageServicePackage.StorageException;
import ru.hartraien.SpringCloudStorageProject.Services.StorageServicePackage.StorageService;
import ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/viewfiles")
public class FileViewController extends AbstractFileController
{
    @Autowired
    public FileViewController( UserService userRepository, DirService dirService, StorageService storageService )
    {
        super( userRepository, dirService, storageService, FileViewController.class );
    }

    @GetMapping("/**")
    public String getPage( HttpServletRequest request, Authentication authentication, Model model )
    {
        String subPath = getSubPath( request );
        String backPath = getBackPath( subPath );
        UserEntity user = getCurrentUser( authentication );
        List<FileDTO> filesInDir;
        try
        {
            getDirService().checkIfDirExistsOrThrow( user.getDir() );
            filesInDir = getStorageService().getAllFilesInDir( user.getDir().getDirname(), subPath );
        }
        catch ( DirectoryException | StorageException e )
        {
            getLogger().warn( "Could not get files in directory", e );
            filesInDir = Collections.emptyList();
        }
        model.addAttribute( "backPath", backPath );
        model.addAttribute( "files", filesInDir );
        model.addAttribute( "path", subPath );
        return "uploadpage";
    }

    private String getBackPath( String subPath )
    {
        if ( "".equals( subPath ) )
            return null;
        else
        {
            int index = subPath.lastIndexOf( "/" );
            if ( index == -1 )
                return "";
            else
                return subPath.substring( 0, index );
        }
    }

}
