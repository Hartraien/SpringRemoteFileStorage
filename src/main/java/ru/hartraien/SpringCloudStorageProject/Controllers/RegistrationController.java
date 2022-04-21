package ru.hartraien.SpringCloudStorageProject.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Services.SecurityServicePackage.SecurityService;
import ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage.UserService;
import ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage.UserServiceException;
import ru.hartraien.SpringCloudStorageProject.Validators.UserValidator;

@Controller
@RequestMapping("/register")
public class RegistrationController
{
    private final UserValidator userValidator;
    private final UserService userService;
    private final SecurityService securityService;

    @Autowired
    public RegistrationController( UserValidator userValidator, UserService userService, SecurityService securityService )
    {
        this.userValidator = userValidator;
        this.userService = userService;
        this.securityService = securityService;
    }

    @GetMapping
    public String getPage( Model model )
    {
        model.addAttribute( "userForm", new UserEntity() );
        return "register";
    }

    @PostMapping
    public String register( @ModelAttribute("userForm") UserEntity userForm, BindingResult bindingResult )
    {
        userValidator.validate( userForm, bindingResult );
        if ( bindingResult.hasErrors() )
        {
            bindingResult.getAllErrors().forEach( System.err::println );
            return "register";
        }

        try
        {
            userService.save( userForm );
        }
        catch ( UserServiceException e )
        {
            //TODO add error attribute
            return "register";
        }

        securityService.autologin( userForm.getUsername(), userForm.getPassword() );

        return "redirect:/";
    }
}
