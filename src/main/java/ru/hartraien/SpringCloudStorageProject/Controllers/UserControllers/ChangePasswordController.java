package ru.hartraien.SpringCloudStorageProject.Controllers.UserControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage.UserService;

@Controller
@RequestMapping("/changepassword")
public class ChangePasswordController
{

    private final UserService userService;

    @Autowired
    public ChangePasswordController( UserService userService )
    {
        this.userService = userService;
    }

    @PostMapping
    public String changePassword(
            @RequestParam("previous_password") String prev_pass
            , @RequestParam("new_password") String new_pass
            , @RequestParam("new_password_copy") String new_pass_copy
            , Authentication authentication
            , RedirectAttributes redirectAttributes )
    {
        if ( new_pass.equals( new_pass_copy ) )
        {
            userService.updatePassword( getCurrentUser( authentication ), new_pass );
            redirectAttributes.addFlashAttribute( "change_password_message", "Successfully changed password!" );
        }
        else
        {
            redirectAttributes.addFlashAttribute( "change_password_error", "New password does not match" );
        }
        return "redirect:/userinfo";
    }

    private UserEntity getCurrentUser( Authentication authentication )
    {
        return userService.findByUsername( authentication.getName() );
    }

}
