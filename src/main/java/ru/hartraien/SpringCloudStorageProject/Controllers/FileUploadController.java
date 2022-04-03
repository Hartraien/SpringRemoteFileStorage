package ru.hartraien.SpringCloudStorageProject.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Repositories.UserRepository;
import ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage.DirService;

import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/uploadpage")
public class FileUploadController
{
    private final UserRepository userRepository;
    private final DirService dirService;

    @Autowired
    public FileUploadController( UserRepository userRepository, DirService dirService )
    {
        this.userRepository = userRepository;
        this.dirService = dirService;
    }

    @GetMapping
    public String getPage( Authentication authentication, Model model )
    {
        UserEntity user = getCurrentUser( authentication );
        model.addAttribute( "files", dirService.getFilesByUser( user ) );
        return "uploadpage";
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getFile( @PathVariable String filename, Authentication authentication )
    {
        UserEntity user = getCurrentUser( authentication );
        Resource file = dirService.loadAsResource( filename, user );
        ContentDisposition contentDisposition = ContentDisposition.builder( "attachment" )
                .filename( filename, StandardCharsets.UTF_8 )
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition( contentDisposition );
        return ResponseEntity.ok()
                .headers( headers )
                .body( file );
    }

    private UserEntity getCurrentUser( Authentication authentication )
    {
        return userRepository.findUserByUsername( authentication.getName() );
    }


    @PostMapping
    public String uploadFile( @RequestParam("file") MultipartFile file, Authentication authentication )
    {
        dirService.uploadFile( file, getCurrentUser( authentication ) );
        return "redirect:/uploadpage";
    }

}
