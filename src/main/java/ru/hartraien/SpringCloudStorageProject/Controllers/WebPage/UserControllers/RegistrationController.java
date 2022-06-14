package ru.hartraien.SpringCloudStorageProject.Controllers.WebPage.UserControllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.hartraien.SpringCloudStorageProject.DTOs.UserEntityDTO;
import ru.hartraien.SpringCloudStorageProject.Services.SecurityServicePackage.SecurityService;
import ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage.UserService;
import ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage.UserServiceException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/register")
public class RegistrationController extends AbstractUserController
{
    private final UserService userService;
    private final SecurityService securityService;
    private final Logger logger;

    @Autowired
    public RegistrationController( UserService userService, SecurityService securityService )
    {
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
    public String register(@Valid @ModelAttribute("userForm") UserEntityDTO userForm
            , BindingResult bindingResult
            , RedirectAttributes redirectAttributes
            , Model model
    , HttpServletResponse response)
    {
        if ( bindingResult.hasErrors() )
        {
            logger.warn( "Could not validate user" );
            model.addAttribute("error_message", "Inserted data is not valid, see text to the right of the fields for clarity");
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return "register";
        }

        try
        {
            userService.save( userForm.toUserEntity() );
        }
        catch ( UserServiceException e )
        {
            logger.warn( "Could not save new user", e );
            model.addAttribute("error_message", "Could not save user, due to " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return "register";
        }

        securityService.autologin( userForm.getUsername(), userForm.getPassword() );

        redirectAttributes.addFlashAttribute("success_message", "Registration was successful");

        return "redirect:/";
    }
}
