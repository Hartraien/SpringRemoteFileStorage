package ru.hartraien.SpringCloudStorageProject.Controllers.WebPage.UserControllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.hartraien.SpringCloudStorageProject.DTOs.UserEntityDTO;
import ru.hartraien.SpringCloudStorageProject.Services.SecurityServicePackage.SecurityService;
import ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage.UserService;
import ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage.UserServiceException;
import ru.hartraien.SpringCloudStorageProject.Validators.UserValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegistrationController extends AbstractUserController
{
    private final UserValidator userValidator;
    private final UserService userService;
    private final SecurityService securityService;
    private final Logger logger;

    @Autowired
    public RegistrationController( UserValidator userValidator, UserService userService, SecurityService securityService )
    {
        this.userValidator = userValidator;
        this.userService = userService;
        this.securityService = securityService;
        logger = LoggerFactory.getLogger( RegistrationController.class );
    }

    @GetMapping
    public String getPage( Authentication authentication, Model model )
    {
        if ( isAuthenticated( authentication ) )
            return "redirect:/userinfo";
        model.addAttribute( "userForm", new UserEntityDTO() );
        return "register";
    }

    @PostMapping
    public String register( @Valid @ModelAttribute("userForm") UserEntityDTO userForm, BindingResult bindingResult )
    {
        if ( bindingResult.hasErrors() )
        {
            logger.warn( "Could not validate user" );
            //bindingResult.getAllErrors().forEach( objectError -> logger.warn( objectError.toString() ) );
            return "register";
        }

        try
        {
            userService.save( userForm.toUserEntity() );
        }
        catch ( UserServiceException e )
        {
            logger.warn( "Could not save new user", e );
            return "register";
        }

        securityService.autologin( userForm.getUsername(), userForm.getPassword() );

        return "redirect:/";
    }
}
