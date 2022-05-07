package ru.hartraien.SpringCloudStorageProject.Controllers.UserControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage.UserService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/reset_password")
public class ResetPasswordController
{

    private final UserService userService;

    @Autowired
    public ResetPasswordController( UserService userService )
    {
        this.userService = userService;
    }

    @GetMapping
    public String showResetPasswordForm( @Param(value = "token") String token, Model model )
    {
        UserEntity user = userService.findUserByResetPasswordToken( token );
        model.addAttribute( "token", token );

        if ( user == null )
        {
            model.addAttribute( "message", "Invalid Token" );
            return "redirect:/";
        }

        return "reset_password";
    }

    @PostMapping
    public String processResetPassword( HttpServletRequest request, Model model )
    {
        String token = request.getParameter( "token" );
        String password = request.getParameter( "password" );

        UserEntity user = userService.findUserByResetPasswordToken( token );
        model.addAttribute( "title", "Reset your password" );

        if ( user == null )
        {
            model.addAttribute( "message", "Invalid Token" );
            return "redirect:/";
        }
        else
        {
            userService.updatePassword( user, password );
            model.addAttribute( "message", "You have successfully changed your password." );

        }

        return "redirect:/";
    }
}
